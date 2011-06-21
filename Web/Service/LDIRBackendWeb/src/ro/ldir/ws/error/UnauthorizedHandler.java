package ro.ldir.ws.error;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import ro.ldir.beans.UserManagerLocal;

import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.core.util.Base64;

/**
 * Servlet implementation class UnauthorizedHandler
 */
@WebServlet(name = "UnauthorizedHandler", urlPatterns = "/error/unauthorized")
public class UnauthorizedHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserManagerLocal userManager;

	/*
	 * @see HttpServlet#HttpServlet()
	 */
	public UnauthorizedHandler() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMethod(req, resp);
	}

	/*
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMethod(req, resp);
	}

	private void doMethod(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter w = resp.getWriter();
		resp.setContentType(MediaType.TEXT_PLAIN);
		resp.setStatus(Status.UNAUTHORIZED.getStatusCode());

		String authHeader = null;
		Enumeration<String> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			if (header.toLowerCase().equals("authorization")) {
				authHeader = header;
				break;
			}
		}
		if (authHeader == null) {
			w.println("You have not supplied any credentials for this request.");
			return;
		}

		String credentials = new String(Base64.decode(req.getHeader(authHeader)
				.substring(6)));
		int p = credentials.indexOf(":");
		if (p == -1) {
			w.println("Invalid credentials: \"" + credentials + "\".");
			return;
		}

		String username = credentials.substring(0, p);
		w.println("Invalid credentials or user not found for \"" + username
				+ "\".");

		userManager.invalidAccess(username);
	}

	/*
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 * response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMethod(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doMethod(req, resp);
	}
}
