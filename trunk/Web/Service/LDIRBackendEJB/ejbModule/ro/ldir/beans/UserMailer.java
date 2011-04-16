package ro.ldir.beans;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ro.ldir.dto.User;

/**
 * A bean to send messages to users
 * 
 */
@Stateless
@Asynchronous
public class UserMailer {
	/** The buffer size used while reading templates. */
	private static final int BUF_SIZE = 4096;

	/** Generic email subject. */
	private static final String SUBJECT = "Let's do it!";

	/** The name of the welcome email template. */
	private static final String WELCOME = "welcome.html";

	@Resource(name = "mail/ldir")
	private Session mailSession;

	@Resource
	private String mailTemplates;

	@EJB
	UserManager userManager;

	public UserMailer() {
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
		return result;
	}

	/**
	 * Sends a welcome email to the user.
	 * 
	 * @param user
	 *            The user to notify
	 */
	public void sendWelcomeMessage(String email) {
		Transport transport;
		User user = userManager.getUser(email);
		try {
			transport = mailSession.getTransport();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return;
		}
		Message msg = new MimeMessage(mailSession);
		try {
			msg.setSubject(SUBJECT);
			msg.setRecipient(RecipientType.TO,
					new InternetAddress(user.getEmail(), user.getFirstName()
							+ " " + user.getLastName()));
			msg.setContent(processTemplate(WELCOME, user), "text/html");
		} catch (MessagingException e) {
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			transport.connect();
			transport.sendMessage(msg,
					msg.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
