/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.ldir.report.formatter.GarbageExcelFormatter;
import ro.ldir.report.formatter.GenericXlsxFormatter;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.ImageInfo;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;

/**
 * 
 * @author dan.grigore
 */
public class AdminGarbageManagerBean {

	private static final Logger log4j = Logger.getLogger(LoginBean.class
			.getCanonicalName());
	private WSInterface wsi;
	/* variabile afisare */
	private User userDetails = new User();
	private List<Garbage> garbageList;
	private CountyArea[] countyAreas;
	private String userId;
	private String gridId;
	private String countyId;
	private Date addDate;
	private boolean noFilter = true;
	private List<String> thumbnails = new ArrayList<String>();
	private List<String> posters = new ArrayList<String>();
	private List<Integer> posterHeights = new ArrayList<Integer>();
	/* pentru editare detalii morman */
	private Garbage selectedGarbage = new Garbage();
	private int selectedImgIndex = 0;

	/**
	 * Creates a new instance of AdminGarbageManagerBean
	 * 
	 * @throws NamingException
	 */
	public AdminGarbageManagerBean() throws NamingException {
		wsi = new WSInterface();

		countyAreas = wsi.getCountyList();
		userDetails = (User) JsfUtils.getHttpSession().getAttribute(
				"USER_DETAILS");
	}

	public void actionApplyFilter() {
		initGarbageList();
	}

	public void actionToVote() {

		if (selectedGarbage != null) {
			boolean toVote = false;
			toVote = selectedGarbage.isToVote();
			wsi.setGarbageToVote(selectedGarbage.getGarbageId(), !toVote);
			selectedGarbage.setToVote(!toVote);
		}
	}

	public void actionToClean() {

		if (selectedGarbage != null) {
			boolean toClean = false;
			toClean = selectedGarbage.isToClean();
			wsi.setGarbageToClean(selectedGarbage.getGarbageId(), !toClean);
			selectedGarbage.setToClean(!toClean);
		}

	}

	public void actionSelectGarbage(ActionEvent event) {
		int garbageId = AppUtils.parseToInt(JsfUtils.getHttpRequest()
				.getParameter("garbageId"));

		/* identificare morman */
		for (Garbage garbage : garbageList) {
			if (garbageId == garbage.getGarbageId().intValue()) {
				selectedGarbage = garbage;
				break;
			}
		}

		if (event.getComponent().getClientId().indexOf("delete") >= 0) {
			return;
		}
		
		if (event.getComponent().getClientId().indexOf("popupLinkNominate") >= 0) {
			return;
		}
		
		
		if (event.getComponent().getClientId().indexOf("popupLinkToClean") >= 0) {
			return;
		}		
		
		/* resetare parametri folositi */
		posters = new ArrayList<String>();
		thumbnails = new ArrayList<String>();
		posterHeights = new ArrayList<Integer>();
		selectedImgIndex = 0;

		/* obtinere poze */
		for (int i = 0; i < selectedGarbage.getPictures().size(); i++) {
			int height = 0;

			try {
				/**
				 * thumbnail
				 */
				File tempFile = wsi.getPicture(userDetails, selectedGarbage, i,
						false);
				log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

				String relativePath = "temp/" + userDetails.getUserId()
						+ "/preview_" + garbageId + "_" + i + "_thumbnail.jpg";
				String previewFilePath = JsfUtils.makeContextPath(relativePath);
				File previewFile = new File(previewFilePath);
				if (!previewFile.getParentFile().isDirectory()) {
					if (!previewFile.getParentFile().mkdirs()) {
						log4j.warn("nu s-a putut crea drectorul pentru preview: "
								+ previewFile.getParentFile().getAbsolutePath());
					}
				}
				if (previewFile.isFile()) {
					if (!previewFile.delete()) {
						log4j.warn("nu s-a putut sterge fisierul existent: "
								+ previewFile.getAbsolutePath());
					}
				}
				if (!tempFile.renameTo(previewFile)) {
					log4j.warn("nu s-a putut redenumi fisierul temporar: "
							+ tempFile.getAbsolutePath());
				} else {
					thumbnails.add(relativePath);
				}

				/**
				 * imagine full
				 */
				tempFile = wsi
						.getPicture(userDetails, selectedGarbage, i, true);
				log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

				relativePath = "temp/" + userDetails.getUserId() + "/preview_"
						+ garbageId + "_" + i + "_full.jpg";
				previewFilePath = JsfUtils.makeContextPath(relativePath);
				previewFile = new File(previewFilePath);
				if (!previewFile.getParentFile().isDirectory()) {
					if (!previewFile.getParentFile().mkdirs()) {
						log4j.warn("nu s-a putut crea drectorul pentru preview: "
								+ previewFile.getParentFile().getAbsolutePath());
					}
				}
				if (previewFile.isFile()) {
					if (!previewFile.delete()) {
						log4j.warn("nu s-a putut sterge fisierul existent: "
								+ previewFile.getAbsolutePath());
					}
				}
				if (!tempFile.renameTo(previewFile)) {
					log4j.warn("nu s-a putut redenumi fisierul temporar: "
							+ tempFile.getAbsolutePath());
				} else {
					posters.add(relativePath);
					ImageInfo imageInfo = ImageInfo.getImageInfo(
							previewFile.getAbsolutePath(), false);
					if (imageInfo.getHeight() > 550) {
						height = 550;
					}
					posterHeights.add(height);
				}
			} catch (Exception ex) {
				log4j.fatal("Eroare obtinere imagine: "
						+ AppUtils.printStackTrace(ex));
			}
		}
	}

	public void actionSetSelectedImage() {
		log4j.debug("---> imagine selectata");
		selectedImgIndex = AppUtils.parseToInt(JsfUtils
				.getRequestParameter("imgIndex"));
	}

	public void actionDeleteGarbage() {
		/*
		 * int garbageId =
		 * AppUtils.parseToInt(JsfUtils.getHttpRequest().getParameter
		 * ("garbageId"));
		 */

		/* identificare morman */
		Garbage garbage = selectedGarbage;
		/*
		 * for (int i = 0; i < garbageList.length; i++) { if (garbageId ==
		 * garbageList[i].getGarbageId().intValue()) { garbage = garbageList[i];
		 * break; } }
		 */
		if (garbage == null) {
			return;
		}

		/* stergere */
		wsi.deleteGarbage(null, selectedGarbage);

		initGarbageList();
	}

	private void initGarbageList() {
		if ((countyId == null) && (gridId == null || gridId.length() == 0)
				&& (userId == null || userId.length() == 0)
				&& (addDate == null)) {
			garbageList = new ArrayList<Garbage>();
			noFilter = true;
			return;
		} else {
			noFilter = false;
		}

		if ((countyId.equals("Toate") == true || countyId.equals("") == true)
				&& userDetails.getRole().equals("ADMIN"))
			countyId = null;

		garbageList = wsi.getGarbageListByFilters(userDetails, countyId,
				AppUtils.parseToInt(gridId, null),
				AppUtils.parseToInt(userId, null), addDate, null);
	}

	public String encodeUrl(String arg) {

		try {
			arg = URLEncoder.encode(arg, "UTF-8");
			log4j.debug("---> encode: " + arg);
		} catch (UnsupportedEncodingException uee) {
			log4j.debug("---> encode error: " + uee.getMessage());
		}
		return arg;
	}

	public void actionGenerateExcel() throws IOException {
		GarbageExcelFormatter fmt = new GarbageExcelFormatter(garbageList);
		byte report[] = new GenericXlsxFormatter(fmt).getBytes();

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc
				.getExternalContext().getResponse();
		OutputStream out = response.getOutputStream();
		out.write(report);
		out.flush();

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.addHeader("Content-Disposition",
				"attachment; filename=garbagereport.xlsx");
		out.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public List<SelectItem> getCountyItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		try {
			for (int i = 0; i < countyAreas.length; i++) {
				CountyArea ca = countyAreas[i];

				items.add(new SelectItem(ca.getName(), ca.getName()));
			}
		} catch (Exception ex) {
			log4j.fatal("eroare: " + ex);
		}

		return items;
	}

	/**
	 * @return the userDetails
	 */
	public User getUserDetails() {
		return userDetails;
	}

	/**
	 * @return the garbageList
	 */
	public List<Garbage> getGarbageList() {
		return garbageList;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the gridId
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * @param gridId
	 *            the gridId to set
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @return the countyId
	 */
	public String getCountyId() {
		return countyId;
	}

	/**
	 * @param countyId
	 *            the countyId to set
	 */
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	/**
	 * @return the addDate
	 */
	public Date getAddDate() {
		return addDate;
	}

	/**
	 * @param addDate
	 *            the addDate to set
	 */
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	/**
	 * @return the noFilter
	 */
	public boolean isNoFilter() {
		return noFilter;
	}

	/**
	 * @return the selectedGarbage
	 */
	public Garbage getSelectedGarbage() {
		return selectedGarbage;
	}

	/**
	 * @param selectedGarbage
	 *            the selectedGarbage to set
	 */
	public void setSelectedGarbage(Garbage selectedGarbage) {
		this.selectedGarbage = selectedGarbage;
	}

	/**
	 * @return the thumbnails
	 */
	public List<String> getThumbnails() {
		return thumbnails;
	}

	/**
	 * @return the posters
	 */
	public List<String> getPosters() {
		return posters;
	}

	/**
	 * @return the posterHeights
	 */
	public List<Integer> getPosterHeights() {
		return posterHeights;
	}

	/**
	 * @return the selectedImgIndex
	 */
	public int getSelectedImgIndex() {
		return selectedImgIndex;
	}
}
