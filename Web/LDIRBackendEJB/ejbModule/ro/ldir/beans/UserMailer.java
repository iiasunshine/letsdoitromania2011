package ro.ldir.beans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.xpath.XPathExpressionException;

import ro.ldir.chartpackage.GarbagePackageBuilder;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;

import com.itextpdf.text.DocumentException;

/**
 * A bean to send messages to users
 * 
 */
@Stateless
@Asynchronous
public class UserMailer {
	/** The buffer size used while reading templates. */
	private static final int BUF_SIZE = 4096;
	private static Logger log = Logger.getLogger(UserMailer.class.getName());

	private static final String SUBJECT_CHARTAREAPACKAGE = "Pachet mormane ";
	private static final String SUBJECT_ERROR = "server error";
	private static final String SUBJECT_PASSWD = "Let's reset your password";
	private static final String SUBJECT_RESET = "Let's reset your account";
	private static final String SUBJECT_WELCOME = "Let's do it!";

	private static final String TEMPLATE_GARBAGEPACKAGE = "garbagepackage.html";
	private static final String TEMPLATE_PASSWD = "passwd.html";
	private static final String TEMPLATE_RESET = "reset.html";
	private static final String TEMPLATE_WELCOME = "welcome.html";

	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	@Resource
	private String errorRecipient;

	@Resource(name = "mail/ldir")
	private Session mailSession;

	@Resource
	private String mailTemplates;

	public UserMailer() {
	}

	/**
	 * @param email
	 * @return
	 */
	private User getUser(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		User user;
		try {
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			String msg = "Unable to send  email to " + email
					+ ". No DB entry found: " + e.getMessage();
			log.warning(msg);
			sendErrorNotification(msg);
			return null;
		}
		return user;
	}

	/**
	 * Processes a template by replacing all elements marked as {@code
	 * {{TO_REPLACE}}}} whith values from the {@link ro.ldir.dto.User} getters.
	 * 
	 * @param template
	 *            The template to process.
	 * @param user
	 *            The user to replace fields.
	 * @return A processed template.
	 * @throws IOException
	 *             When the file cannot be loaded.
	 */
	private String processTemplate(String template, User user)
			throws IOException {
		String pathSeparator = System.getProperty("file.separator");
		char buffer[] = new char[BUF_SIZE];
		StringBuffer sb = new StringBuffer();
		File file = new File(mailTemplates + pathSeparator + template);
		FileReader reader = new FileReader(file);
		try {
			while (reader.read(buffer) > 0)
				sb.append(buffer);
		} catch (IOException e) {
			throw e;
		} finally {
			reader.close();
		}
		String result = sb.toString();

		for (Method m : User.class.getMethods()) {
			String methodName = m.getName();
			if (!methodName.startsWith("get"))
				continue;
			String toReplace = "\\{\\{\\{"
					+ methodName.substring(3).toLowerCase() + "\\}\\}\\}";
			try {
				result = result
						.replaceAll(toReplace, m.invoke(user).toString());
			} catch (Exception e) {
				continue;
			}
		}
		log.exiting("UserMailer", "processTemplate", result);
		return result;
	}

	public void sendAccountResetMessage(String email) {
		log.fine("Sending account reset notice to " + email);
		User user = getUser(email);
		if (user == null)
			return;
		sendEmail(user, SUBJECT_RESET, TEMPLATE_RESET);
	}

	/**
	 * 
	 * @param user
	 * @param subject
	 * @param template
	 */
	private void sendEmail(User user, String subject, String template) {
		Transport transport;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		}
		Message msg = new MimeMessage(mailSession);
		try {
			msg.setSubject(subject);
			msg.setRecipient(RecipientType.TO,
					new InternetAddress(user.getEmail(), user.getFirstName()
							+ " " + user.getLastName()));
			msg.setContent(processTemplate(template, user), "text/html");
		} catch (MessagingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (UnsupportedEncodingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (IOException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		}

		try {
			transport.connect();
			transport.sendMessage(msg,
					msg.getRecipients(Message.RecipientType.TO));
			transport.close();
			log.info("Sent email to " + user.getEmail());
		} catch (MessagingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
		}
	}

	public void sendErrorNotification(String data) {
		Transport transport;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		}
		Message msg = new MimeMessage(mailSession);
		try {
			msg.setSubject(SUBJECT_ERROR);
			msg.setRecipient(RecipientType.TO, new InternetAddress(
					errorRecipient));
			msg.setContent(data, "text/plain");

			transport.connect();
			transport.sendMessage(msg,
					msg.getRecipients(Message.RecipientType.TO));
			transport.close();
			log.info("Sent error email to " + errorRecipient);
		} catch (MessagingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
		}
	}

	public void sendGarbagePackage(User user, String origin,
			Set<Garbage> garbages) {
		log.fine("Sending chart package to " + user.getEmail());

		Transport transport;
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		}
		Message msg = new MimeMessage(mailSession);
		try {
			msg.setSubject(SUBJECT_CHARTAREAPACKAGE);
			msg.setRecipient(RecipientType.TO,
					new InternetAddress(user.getEmail(), user.getFirstName()
							+ " " + user.getLastName()));

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart
					.setContent(processTemplate(TEMPLATE_GARBAGEPACKAGE, user),
							"text/html");

			MimeBodyPart attachmentPart = new MimeBodyPart();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GarbagePackageBuilder pb = new GarbagePackageBuilder(origin,
					garbages);
			pb.writePDF(out);
			DataSource source = new ByteArrayDataSource(out.toByteArray(),
					"application/pdf");
			attachmentPart.setDataHandler(new DataHandler(source));
			attachmentPart.setFileName("pachet_mormane.pdf");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentPart);
			msg.setContent(multipart);
		} catch (MessagingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (UnsupportedEncodingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (IOException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (XPathExpressionException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		} catch (DocumentException e) {
			log.warning("Cannot send mail: " + e.getMessage());
			return;
		}

		try {
			transport.connect();
			transport.sendMessage(msg,
					msg.getRecipients(Message.RecipientType.TO));
			transport.close();
			log.info("Sent email to " + user.getEmail());
		} catch (MessagingException e) {
			log.warning("Cannot send mail: " + e.getMessage());
		}
	}

	public void sendResetToken(User user) {
		if (user == null)
			return;
		log.fine("Sending reset mail to " + user.getEmail());
		sendEmail(user, SUBJECT_PASSWD, TEMPLATE_PASSWD);
	}

	/**
	 * Sends a welcome email to the user.
	 * 
	 * @param email
	 *            The user to notify
	 */
	public void sendWelcomeMessage(String email) {
		log.fine("Sending welcome mail to " + email);
		User user = getUser(email);
		if (user == null)
			return;
		sendEmail(user, SUBJECT_WELCOME, TEMPLATE_WELCOME);
	}
}
