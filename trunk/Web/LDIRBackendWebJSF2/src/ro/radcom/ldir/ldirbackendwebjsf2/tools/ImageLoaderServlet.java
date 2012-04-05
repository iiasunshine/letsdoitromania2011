package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.ldir.beans.GarbageManagerLocal;

/**
 * Servlet implementation class ImageLoaderServlet
 */
@WebServlet(urlPatterns = { "/ImageLoaderServlet" }, initParams = {
		@WebInitParam(name = "garbageID", value = "0"),
		@WebInitParam(name = "imageIndex", value = "0"),
		@WebInitParam(name = "display", value = "0") })
public class ImageLoaderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = Logger
			.getLogger(ImageLoaderServlet.class.getCanonicalName());
	private GarbageManagerLocal garbageManager = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageLoaderServlet() {

		super();

		InitialContext ic = null;
		try {
			ic = new InitialContext();
		} catch (NamingException e) {
			log4j.debug("[SERVLET] - ERROR" + e);
		}
		try {
			garbageManager = (GarbageManagerLocal) ic
					.lookup("java:global/LDIRBackend/LDIRBackendEJB/GarbageManager!ro.ldir.beans.GarbageManager");
		} catch (NamingException e) {
			log4j.debug("[SERVLET] - ERROR" + e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FileInputStream fis = null;
		String fileName = null;
		String garbageIDStringParam = request.getParameter("garbageID");
		String pictureIndexParam = request.getParameter("imageIndex");
		String displayParam = request.getParameter("display");

		log4j.debug("[SERVLET] " + garbageIDStringParam + "/"
				+ pictureIndexParam);

		if (garbageIDStringParam == null || pictureIndexParam == null){
			response.setStatus(HttpServletResponse.SC_CONTINUE);
			return;
		}

		int gid = AppUtils.parseToInt(garbageIDStringParam);
		int pIndex = AppUtils.parseToInt(pictureIndexParam);

		try{
		if (displayParam == null) {
			// is thumbnail;
			log4j.debug("[SERVLET] - is thumbnail");
			fileName = getThumbnailPath(gid, pIndex);
		}

		else {
			log4j.debug("[SERVLET] - is display");
			fileName = getDisplayPath(gid, pIndex);
		}
		log4j.debug("[SERVLET] - filename+" + fileName);
		}catch(Exception err){
			fileName=null;
		}
		
		if(fileName==null){
			response.setStatus(HttpServletResponse.SC_CONTINUE);
			return;
		}
		String mimeType = getServletContext().getMimeType(fileName);
		log4j.debug("[SERVLET] - mimetime: " + mimeType);
		if (mimeType == null) {
			// eroare
			response.setStatus(HttpServletResponse.SC_CONTINUE);
			return;
		}

		response.setContentType(mimeType);
		ServletOutputStream output = response.getOutputStream();
		try {
			fis = new FileInputStream(new File(fileName));
			int size = fis.available();
			byte[] buf = new byte[size];
			fis.read(buf);
			output.write(buf);

		} finally {
			fis.close();
			output.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String getThumbnailPath(int garbageId, int imageId) {
		return garbageManager.getImageThumbnailPath(garbageId, imageId);
	}

	public String getDisplayPath(int garbageId, int imageId) {
		return garbageManager.getImageDisplayPath(garbageId, imageId);
	}

}
