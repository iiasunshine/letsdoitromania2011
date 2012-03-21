/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidTeamOperationException;
import ro.ldir.exceptions.NoCountyException;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.ImageInfo;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.MyGarbageComparator;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 * 
 * @author dan.grigore
 */

public class MormanManagerBean {

	private static final Logger log4j = Logger.getLogger(LoginBean.class
			.getCanonicalName());
	private WSInterface wsi;

	/* variabile afisare */
	private MyGarbage myGarbage = new MyGarbage(new Garbage());
	private Garbage garbageSimplu;
	private List<MyGarbage> myGarbageList = new ArrayList<MyGarbage>();
	private User userDetails = new User();
	private Team teamSelected = null;
	private String latitudine = "0.0";
	private String lat_grd = "0";
	private String lat_min = "0";
	private String lat_sec = "0.0";
	private String longitudine = "0.0";
	private String long_grd = "0";
	private String long_min = "0";
	private String long_sec = "0.0";
	private List<String> thumbnails = new ArrayList<String>();
	private List<String> posters = new ArrayList<String>();
	private List<Integer> posterHeights = new ArrayList<Integer>();

	private boolean coord_zecimale = true;
	private boolean coord_grade = false;
	private boolean allocable=false;
	private UploadedFile uploadedFile1 = null;
	private UploadedFile uploadedFile2 = null;
	private UploadedFile uploadedFile3 = null;
	private int selectedImgIndex = 0;

	private boolean mormanAlocat = false;

	/**
	 * Creates a new instance of MormanManagerBean
	 * 
	 * @throws NamingException
	 */
	public MormanManagerBean() throws NamingException {
		wsi = new WSInterface();

		/* adaugare mesaj info de pe sesiune daca exista */
		String infoMessage = (String) JsfUtils.getHttpSession().getAttribute(
				"INFO_MESSAGE");
		if (infoMessage != null) {
			JsfUtils.addInfoMessage(infoMessage);
			JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
		}

		/* adaugare mesaj warn de pe sesiune daca exista */
		String warnMessage = (String) JsfUtils.getHttpSession().getAttribute(
				"WARN_MESSAGE");
		if (warnMessage != null) {
			JsfUtils.addErrorMessage(warnMessage);
			JsfUtils.getHttpSession().removeAttribute("WARN_MESSAGE");
		}

		/* obtinere id garbage selectat din request (daca este cazul) */
		int garbageId = AppUtils.parseToInt(JsfUtils
				.getRequestParameter("garbageId"));
		try {
			userDetails = (User) JsfUtils.getHttpSession().getAttribute(
					"USER_DETAILS");
		} catch (Exception e) {
			// TODO: handle exception
		}
		int teamSelectedId = -1;
		if (JsfUtils.getHttpSession().getAttribute("TEAM_SELECTED") != null)
			teamSelectedId = (Integer) JsfUtils.getHttpSession().getAttribute(
					"TEAM_SELECTED");
		if (teamSelectedId != -1)
			reloadTeam(teamSelectedId);

		/* initializare detalii utilizator si lista mormane */
		if (garbageId == 0)
			if (JsfUtils.getHttpSession().getAttribute("garbageId") != null) {
				garbageId = (Integer) JsfUtils.getHttpSession().getAttribute(
						"garbageId");
				JsfUtils.getHttpSession().removeAttribute("garbageId");
			}

		if (garbageId > 0) {

			if (teamSelected != null) {
				Iterator<Garbage> iterator = teamSelected.getGarbages()
						.iterator();
				while (iterator.hasNext()) {
					Garbage g1 = iterator.next();
					if (g1.getGarbageId() == garbageId) {
						mormanAlocat = true;
						break;
					}

				}
			}
			;

			Garbage g = wsi.getGarbage(userDetails, garbageId);
			garbageSimplu = g;
			myGarbage = new MyGarbage(g);
			longitudine = "" + g.getX();
			latitudine = "" + g.getY();
			double lat_grd1 = Math.floor(g.getY());
			double long_grd1 = Math.floor(g.getX());

			double lat_min1 = (g.getY() - lat_grd1) * 60;
			double long_min1 = (g.getX() - long_grd1) * 60;

			double lat_sec1 = (lat_min1 - Math.floor(lat_min1)) * 60;
			double long_sec1 = (long_min1 - Math.floor(long_min1)) * 60;
			lat_min1 = Math.floor(lat_min1);
			long_min1 = Math.floor(long_min1);
			lat_grd = String.valueOf(lat_grd1);
			long_grd = String.valueOf(long_grd1);

			lat_min = String.valueOf(lat_min1);
			long_min = String.valueOf(long_min1);

			lat_sec = String.valueOf(lat_sec1);
			long_sec = String.valueOf(long_sec1);
			int latSecStringSize = lat_sec.length();
			int longSecStringSize = long_sec.length();
			int latIndex = lat_sec.indexOf(".");
			int longIndex = long_sec.indexOf(".");
			int diffLat = latSecStringSize - latIndex;
			int diffLong = longSecStringSize - longIndex;
			if (diffLat > 0)
				if (diffLat >= 3)
					lat_sec = lat_sec.substring(0, latIndex + 3);
				else
					lat_sec = lat_sec.substring(0, latIndex + diffLat);
			if (diffLong > 0)
				if (diffLong >= 3)
					long_sec = long_sec.substring(0, longIndex + 3);
				else
					long_sec = long_sec.substring(0, longIndex + diffLong);
			/* obtinere numar poze */
			for (int i = 0; i < g.getPictures().size(); i++) {
				int height = 0;
				try {
					/**
					 * thumbnail
					 */
					File tempFile = wsi.getPicture(userDetails, g, i, false);
					log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

					String relativePath = "temp/" + userDetails.getUserId()
							+ "/preview_" + garbageId + "_" + i
							+ "_thumbnail.jpg";
					String previewFilePath = JsfUtils
							.makeContextPath(relativePath);
					File previewFile = new File(previewFilePath);
					if (!previewFile.getParentFile().isDirectory()) {
						if (!previewFile.getParentFile().mkdirs()) {
							log4j.warn("nu s-a putut crea drectorul pentru preview: "
									+ previewFile.getParentFile()
											.getAbsolutePath());
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
					tempFile = wsi.getPicture(userDetails, g, i, true);
					log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

					relativePath = "temp/" + userDetails.getUserId()
							+ "/preview_" + garbageId + "_" + i + "_full.jpg";
					previewFilePath = JsfUtils.makeContextPath(relativePath);
					previewFile = new File(previewFilePath);
					if (!previewFile.getParentFile().isDirectory()) {
						if (!previewFile.getParentFile().mkdirs()) {
							log4j.warn("nu s-a putut crea drectorul pentru preview: "
									+ previewFile.getParentFile()
											.getAbsolutePath());
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

			String infoHtml = "<strong>"
					+ JsfUtils.getBundleMessage("details_morman") + " "
					+ g.getGarbageId() + "</strong><br/>";
			infoHtml += JsfUtils.getBundleMessage("details_area")
					+ " "
					+ (g.getChartedArea() != null ? g.getChartedArea()
							.getName() : "unknown") + "<br/>";
			infoHtml += JsfUtils.getBundleMessage("details_county")
					+ " "
					+ (g.getCounty() != null ? g.getCounty().getName()
							: "unknown") + "<br/>";
			infoHtml += JsfUtils.getBundleMessage("details_state")
					+ " "
					+ (g.getStatus() != null ? g.getStatus().name() : "unknown")
					+ "<br/><br/>";
			infoHtml += (g.getDescription() != null ? g.getDescription() : "")
					+ "<br/>";
			infoHtml += "<br/><a href=\"cartare-mormane-detalii.jsf?garbageId="
					+ g.getGarbageId() + "\" style=\"color: #4D751F;\">"
					+ JsfUtils.getBundleMessage("details_view_link") + "</a>";
			myGarbageList.add(new MyGarbage(g, infoHtml));
		} else
			init(garbageId);

	}

	private void init(int garbageId) {
		try {
			/* obtinere detalii utilizator */
			userDetails = (User) JsfUtils.getHttpSession().getAttribute(
					"USER_DETAILS");
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * parcurgere prelucrare si initializare garbage selectat daca este
		 * cazul
		 */
		if (userDetails != null) {
			if (userDetails.getGarbages() != null) {
				Iterator<Garbage> iterator = userDetails.getGarbages()
						.iterator();
				while (iterator.hasNext()) {
					Garbage g = iterator.next();

					if (g.getGarbageId() == garbageId) {
						myGarbage = new MyGarbage(g);
						longitudine = "" + g.getX();
						latitudine = "" + g.getY();

						/* obtinere numar poze */
						for (int i = 0; i < g.getPictures().size(); i++) {
							int height = 0;
							try {
								/**
								 * thumbnail
								 */

								File tempFile = wsi.getPicture(userDetails, g,
										i, false);
								log4j.debug("---> temp file: "
										+ tempFile.getAbsolutePath());

								String relativePath = "temp/"
										+ userDetails.getUserId() + "/preview_"
										+ garbageId + "_" + i
										+ "_thumbnail.jpg";
								String previewFilePath = JsfUtils
										.makeContextPath(relativePath);
								File previewFile = new File(previewFilePath);
								if (!previewFile.getParentFile().isDirectory()) {
									if (!previewFile.getParentFile().mkdirs()) {
										log4j.warn("nu s-a putut crea drectorul pentru preview: "
												+ previewFile.getParentFile()
														.getAbsolutePath());
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

								tempFile = wsi.getPicture(userDetails, g, i,
										true);
								log4j.debug("---> temp file: "
										+ tempFile.getAbsolutePath());

								relativePath = "temp/"
										+ userDetails.getUserId() + "/preview_"
										+ garbageId + "_" + i + "_full.jpg";
								previewFilePath = JsfUtils
										.makeContextPath(relativePath);
								previewFile = new File(previewFilePath);
								if (!previewFile.getParentFile().isDirectory()) {
									if (!previewFile.getParentFile().mkdirs()) {
										log4j.warn("nu s-a putut crea drectorul pentru preview: "
												+ previewFile.getParentFile()
														.getAbsolutePath());
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
									ImageInfo imageInfo = ImageInfo
											.getImageInfo(previewFile
													.getAbsolutePath(), false);
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
					String infoHtml = "<strong>"
							+ JsfUtils.getBundleMessage("details_morman") + " "
							+ g.getGarbageId() + "</strong><br/>";
					infoHtml += JsfUtils.getBundleMessage("details_area")
							+ " "
							+ (g.getChartedArea() != null ? g.getChartedArea()
									.getName() : "unknown") + "<br/>";
					infoHtml += JsfUtils.getBundleMessage("details_county")
							+ " "
							+ (g.getCounty() != null ? g.getCounty().getName()
									: "unknown") + "<br/>";
					infoHtml += JsfUtils.getBundleMessage("details_state")
							+ " "
							+ (g.getStatus() != null ? g.getStatus().name()
									: "unknown") + "<br/><br/>";
					infoHtml += (g.getDescription() != null ? g
							.getDescription() : "") + "<br/>";
					infoHtml += "<br/><a href=\"cartare-mormane-detalii.jsf?garbageId="
							+ g.getGarbageId()
							+ "\" style=\"color: #4D751F;\">"
							+ JsfUtils.getBundleMessage("details_view_link")
							+ "</a>";
					myGarbageList.add(new MyGarbage(g, infoHtml));
				}
				Collections.sort(myGarbageList, new MyGarbageComparator());
			}
		}
	}
	
	public void actionToVote(){
		if(myGarbage!=null){
			Garbage g=myGarbage.getGarbage();
			if(g!=null){
				boolean toVote=false;
				toVote=g.isToVote();
				wsi.setGarbageToVote(g.getGarbageId(), !toVote);
				g.setToVote(!toVote);
			}
		}
	}
	
	public void actionToClean(){
		boolean toClean=false;
		if(myGarbage!=null){
			Garbage g=myGarbage.getGarbage();
			if(g!=null){
				toClean=g.isToClean();
				wsi.setGarbageToClean(g.getGarbageId(),!toClean);
				g.setToClean(!toClean);
			}
		}
	}

	public void actionSetSelectedImage() {
		log4j.debug("---> imagine selectata");
		selectedImgIndex = AppUtils.parseToInt(JsfUtils
				.getRequestParameter("imgIndex"));
	}

	public String actionChangeStare() {
		myGarbage.getGarbage().setStatus(Garbage.GarbageStatus.CLEANED);
		wsi.setStatusGarbage(userDetails, myGarbage.getGarbage());
		/* cerere informatii user */
		JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE",
				"Starea mormanului updatata");
		JsfUtils.getHttpSession().setAttribute("garbageId",
				myGarbage.getGarbage().getGarbageId());
		return NavigationValues.MORMAN_STARE_UPDATE_SUCCES;
	}

	public String actionEditMorman() {
		/**
		 * validari
		 */
		Garbage garbage = myGarbage.getGarbage();
		if (garbage.getDescription() == null
				|| garbage.getDescription().trim().length() == 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			return NavigationValues.MORMAN_ADD_FAIL;
		}
		int totalPercents = garbage.getPercentageGlass()
				+ garbage.getPercentageMetal() + garbage.getPercentagePlastic()
				+ garbage.getPercentageWaste();
		if (totalPercents != 100 && totalPercents != 0) {
			JsfUtils.addWarnBundleMessage("chart_err_overflow_percents");
			return NavigationValues.MORMAN_ADD_FAIL;
		}
		/* latitudine */

		try {
			if (isCoord_zecimale()) {
				garbage.setY(Double.parseDouble(latitudine));
			} else {
				garbage.setY(Double.parseDouble(lat_grd)
						+ (Double.parseDouble(lat_min) / (double) 60)
						+ (Double.parseDouble(lat_sec) / (double) 3600));
			}

			if (garbage.getY() == 0) {
				JsfUtils.addWarnBundleMessage("chart_err_latitude");
				return NavigationValues.MORMAN_ADD_FAIL;
			}
		} catch (Exception ex) {
			JsfUtils.addWarnBundleMessage("chart_err_latitude");
			return NavigationValues.MORMAN_ADD_FAIL;
		}
		/* longitudine */
		try {
			if (isCoord_zecimale()) {
				garbage.setX(Double.parseDouble(longitudine));
			} else {
				garbage.setX(Double.parseDouble(long_grd)
						+ (Double.parseDouble(long_min) / (double) 60)
						+ (Double.parseDouble(long_sec) / (double) 3600));
			}
			if (garbage.getX() == 0) {
				JsfUtils.addWarnBundleMessage("chart_err_longitude");
				return NavigationValues.MORMAN_ADD_FAIL;
			}
		} catch (Exception ex) {
			JsfUtils.addWarnBundleMessage("chart_err_longitude");
			return NavigationValues.MORMAN_ADD_FAIL;
		}
		if (garbage.getGarbageId() == null || garbage.getGarbageId() == 0) {
			for (int i = 0; i < myGarbageList.size(); i++) {
				Garbage garbageItem = myGarbageList.get(i).getGarbage();
				if ((garbage.getX() == garbageItem.getX())
						&& (garbage.getY() == garbageItem.getY())) {
					JsfUtils.addWarnBundleMessage("chart_err_exists");
					return NavigationValues.MORMAN_ADD_FAIL;
				}
			}
		}

		/**
		 * procesare
		 */
		garbage.setRecordDate(new Date());
		//TODO to check if it change old status.
		garbage.setStatus(Garbage.GarbageStatus.IDENTIFIED);

		try {
			garbage.setGarbageId(wsi.addGarbage(userDetails, garbage));

			/* adaugare imagini */

			String warn_text = "";
			if (uploadedFile1 != null) {
				if (!uploadImage(uploadedFile1, garbage, 1)) {
					warn_text = JsfUtils
							.getBundleMessage("details_add_img_err")
							.replaceAll("\\{0\\}", "1");
				}
			}
			if (uploadedFile2 != null) {
				if (!uploadImage(uploadedFile2, garbage, 2)) {
					warn_text += warn_text.length() > 0 ? "; " : "";
					warn_text += JsfUtils.getBundleMessage(
							"details_add_img_err").replaceAll("\\{0\\}", "2");
				}
			}
			if (uploadedFile3 != null) {
				if (!uploadImage(uploadedFile3, garbage, 3)) {
					warn_text += warn_text.length() > 0 ? "; " : "";
					warn_text += JsfUtils.getBundleMessage(
							"details_add_img_err").replaceAll("\\{0\\}", "3");
				}
			}
			if (warn_text.length() > 0) {
				JsfUtils.getHttpSession().setAttribute("WARN_MESSAGE",
						warn_text);
			}

			/* cerere informatii user */
			if (userDetails != null)
				wsi.reinitUser(userDetails);
			/* mesaj info referitor la adaugarea/modificarea mormanului */
			String infoText = "";
			if (garbage.getGarbageId() != null && garbage.getGarbageId() > 0) {
				infoText = JsfUtils.getBundleMessage("details_modify_confirm");
				infoText = infoText.replaceAll("\\{0\\}",
						"" + garbage.getGarbageId());
				// test to see: nu functioneaza ceva. Toaate mormanele adaugate sunt vazute ca modificate. //TODO
				//init(garbage.getGarbageId());
			} else {
				init(0);
				garbage = myGarbageList.get(0).getGarbage();
				infoText = JsfUtils.getBundleMessage("details_add_confirm");
				infoText = infoText.replaceAll("\\{0\\}",
						"" + garbage.getGarbageId());
			}

			JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
			return NavigationValues.MORMAN_ADD_SUCCESS;
		} catch (NoCountyException e) {
			log4j.fatal("Eroare apelare WS - no county: "
					+ AppUtils.printStackTrace(e));
			JsfUtils.addWarnBundleMessage("chart_err_nocounty");
			if (userDetails != null) // do fwd to a jsp used in logged
										// enviroment
				return NavigationValues.MORMAN_ADD_FAIL;
			return NavigationValues.MORMAN_ADD_FAIL_FREE;
		} catch (Exception ex) {
			log4j.fatal("Eroare apelare WS: " + AppUtils.printStackTrace(ex));
			JsfUtils.addWarnBundleMessage("internal_err");
			if (userDetails != null) // do fwd to a jsp used in logged
										// enviroment
				return NavigationValues.MORMAN_ADD_FAIL;
			return NavigationValues.MORMAN_ADD_FAIL_FREE;
		}
	}

	private boolean uploadImage(UploadedFile uploadedFile, Garbage garbage,
			int imgNr) {
		try {
			/* salvare imagine in directorul temporar */
			File imageFile = AppUtils.saveToTempFile(uploadedFile, "tmp_"
					+ garbage.getGarbageId() + "_" + imgNr);

			/* timitre imagine la backend */
			wsi.addPicture(userDetails, garbage, imageFile);
			imageFile.delete();
			return true;
		} catch (Exception ex) {
			log4j.fatal("eroare procesare imagine[" + imgNr + "]: "
					+ AppUtils.printStackTrace(ex));
			return false;
		}
	}

	public String actionAssignMorman() {
		/* atribuire zona noua daca este cazul */
		if (JsfUtils.getHttpSession().getAttribute("TEAM_SELECTED") != null
				&& teamSelected == null) {
			int teamSelectedId = (Integer) JsfUtils.getHttpSession()
					.getAttribute("TEAM_SELECTED");
			reloadTeam(teamSelectedId);
		}
		;
		if (teamSelected == null) {
			JsfUtils.addWarnBundleMessage("internal_err");
			return NavigationValues.MORMAN_ALOCAT_FAIL;
		}
		;

		int addGarbageId = AppUtils.parseToInt(JsfUtils
				.getRequestParameter("addGarbageId"));

		Garbage garbage = new Garbage();
		garbage.setGarbageId(addGarbageId);

		if (addGarbageId > 0) {
			try {
				wsi.addGarbageToTeam(userDetails, teamSelected, garbage);
			} catch (InvalidTeamOperationException e) {
				return NavigationValues.MORMAN_ALOCAT_FAIL;
			}
			log4j.debug("garbage atribuit " + addGarbageId);

			String infoText = JsfUtils.getBundleMessage("garbage_add_confirm")
					.replaceAll("\\{0\\}", "" + addGarbageId);
			JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);

			JsfUtils.getHttpSession().setAttribute("LASTPOSITION",
					latitudine + "," + longitudine);
			return NavigationValues.MORMAN_ALOCAT_SUCCESS;

		}

		return NavigationValues.MORMAN_ALOCAT_FAIL;
	}

	public void reloadTeam(int id) {

		List<Team> teamList = userDetails.getManagedTeams();
		log4j.info("user teams:" + teamList);

		if (teamList != null && teamList.size() > 0)
			for (Team team : teamList) {

				// first team gets selected
				if (id == team.getTeamId()) {
					teamSelected = team;
					break;
				}
				;
			}
		;
	}

	public String actionDeleteMorman() {
		wsi.deleteGarbage(userDetails, myGarbage.getGarbage());
		wsi.reinitUser(userDetails);
		return NavigationValues.MORMAN_DELETE_SUCCESS;
	}

	public String actionRemoveMormanFromTeam() {

		// TO BE REWRITED FOR TEAMS AND WITH CORRECT wsi. call
		if (teamSelected == null) {
			JsfUtils.addWarnBundleMessage("internal_err");
			return NavigationValues.MORMAN_DEZALOCAT_FAIL;
		}

		int removeGarbageId = AppUtils.parseToInt(JsfUtils
				.getRequestParameter("removeGarbageId"));

		Garbage garbage = new Garbage();
		garbage.setGarbageId(removeGarbageId);
		wsi.deleteGarbageFromTeam(userDetails, teamSelected.getTeamId(),
				removeGarbageId);
		/* cerere informatii user */
		// cr = wsi.reinitUser(userDetails);
		// log4j.debug("---> Reinit User Garbage statusCode: " + statusCode +
		// " (" + cr.getClientResponseStatus() + ")");
		String infoText = "Mormanul " + removeGarbageId
				+ " a fost dealocat cu succes";
		JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
		return NavigationValues.MORMAN_DEZALOCAT_SUCCESS;

	}

	public List<SelectItem> getSaciNrItems() {
		List<SelectItem> saciNrItems = new ArrayList<SelectItem>();
		int i = 1;
		while (i <= 1000) {
			saciNrItems.add(new SelectItem(i, "" + i));
			if (i < 10) {
				i++;
				continue;
			} else {
				if (i < 50) {
					i += 5;
					continue;
				} else {
					if (i < 100) {
						i += 10;
						continue;
					} else {
						if (i < 1000) {

							i += 50;
							continue;
						}
					}
				}
			}
			i++;
		}

		return saciNrItems;
	}

	/**
	 * @return the userDetails
	 */
	public User getUserDetails() {
		return userDetails;
	}

	/**
	 * @param userDetails
	 *            the userDetails to set
	 */

	public boolean getMormanAlocat() {
		return this.mormanAlocat;
	}
	
	public boolean getAllocable(){
		this.allocable=!getMormanAlocat()&&this.myGarbage.getGarbage().isToVote();
		return this.allocable;
		}
	
	public boolean isAllocable(){
		this.allocable=!getMormanAlocat()&&this.myGarbage.getGarbage().isToVote();
		return this.allocable;
	}

	public void setAllocable(boolean a){
		this.allocable=a;
	}
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	/**
	 * @return the latitudine
	 */
	public String getLatitudine() {
		return latitudine;
	}

	/**
	 * @param latitudine
	 *            the latitudine to set
	 */
	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	/**
	 * @return the longitudine
	 */
	public String getLongitudine() {
		return longitudine;
	}

	/**
	 * @param longitudine
	 *            the longitudine to set
	 */
	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	/**
	 * @return the myGarbage
	 */
	public MyGarbage getMyGarbage() {
		return myGarbage;
	}

	/**
	 * @return the myGarbageList
	 */
	public List<MyGarbage> getMyGarbageList() {
		return myGarbageList;
	}

	/**
	 * @return the uploadedFile1
	 */
	public UploadedFile getUploadedFile1() {
		return uploadedFile1;
	}

	/**
	 * @param uploadedFile1
	 *            the uploadedFile1 to set
	 */
	public void setUploadedFile1(UploadedFile uploadedFile1) {
		this.uploadedFile1 = uploadedFile1;
	}

	/**
	 * @return the lat_grd
	 */
	public String getLat_grd() {
		return lat_grd;
	}

	/**
	 * @param lat_grd
	 *            the lat_grd to set
	 */
	public void setLat_grd(String lat_grd) {
		this.lat_grd = lat_grd;
	}

	/**
	 * @return the lat_min
	 */
	public String getLat_min() {
		return lat_min;
	}

	/**
	 * @param lat_min
	 *            the lat_min to set
	 */
	public void setLat_min(String lat_min) {
		this.lat_min = lat_min;
	}

	/**
	 * @return the lat_sec
	 */
	public String getLat_sec() {
		return lat_sec;
	}

	/**
	 * @param lat_sec
	 *            the lat_sec to set
	 */
	public void setLat_sec(String lat_sec) {
		this.lat_sec = lat_sec;
	}

	/**
	 * @return the long_grd
	 */
	public String getLong_grd() {
		return long_grd;
	}

	/**
	 * @param long_grd
	 *            the long_grd to set
	 */
	public void setLong_grd(String long_grd) {
		this.long_grd = long_grd;
	}

	/**
	 * @return the long_min
	 */
	public String getLong_min() {
		return long_min;
	}

	/**
	 * @param long_min
	 *            the long_min to set
	 */
	public void setLong_min(String long_min) {
		this.long_min = long_min;
	}

	/**
	 * @return the long_sec
	 */
	public String getLong_sec() {
		return long_sec;
	}

	/**
	 * @param long_sec
	 *            the long_sec to set
	 */
	public void setLong_sec(String long_sec) {
		this.long_sec = long_sec;
	}

	/**
	 * @return the coord_zecimale
	 */
	public boolean isCoord_zecimale() {
		return coord_zecimale;
	}

	/**
	 * @param coord_zecimale
	 *            the coord_zecimale to set
	 */
	public void setCoord_zecimale(boolean coord_zecimale) {
		this.coord_zecimale = coord_zecimale;
		this.coord_grade = !coord_zecimale;
	}

	public Boolean getCoord_zecimale() {
		return this.coord_zecimale;
	}

	/**
	 * @return the coord_grade
	 */

	public Boolean getCoord_grade() {
		return this.coord_grade;
	}

	/**
	 * @return the coord_grade
	 */
	public boolean isCoord_grade() {
		return this.coord_grade;
	}

	/**
	 * @param coord_grade
	 *            the coord_grade to set
	 */
	public void setCoord_grade(boolean coord_grade) {
		this.coord_grade = coord_grade;
		this.coord_zecimale = !coord_grade;

	}

	/**
	 * @return the thumbnails
	 */
	public List<String> getThumbnails() {
		return thumbnails;
	}

	/**
	 * @return the selectedImgIndex
	 */
	public int getSelectedImgIndex() {
		return selectedImgIndex;
	}

	/**
	 * @param selectedImgIndex
	 *            the selectedImgIndex to set
	 */
	public void setSelectedImgIndex(int selectedImgIndex) {
		this.selectedImgIndex = selectedImgIndex;
	}

	/**
	 * @return the posters
	 */
	public List<String> getPosters() {
		return posters;
	}

	/**
	 * @return the uploadedFile2
	 */
	public UploadedFile getUploadedFile2() {
		return uploadedFile2;
	}

	/**
	 * @param uploadedFile2
	 *            the uploadedFile2 to set
	 */
	public void setUploadedFile2(UploadedFile uploadedFile2) {
		this.uploadedFile2 = uploadedFile2;
	}

	/**
	 * @return the uploadedFile3
	 */
	public UploadedFile getUploadedFile3() {
		return uploadedFile3;
	}

	/**
	 * @param uploadedFile3
	 *            the uploadedFile3 to set
	 */
	public void setUploadedFile3(UploadedFile uploadedFile3) {
		this.uploadedFile3 = uploadedFile3;
	}

	/**
	 * @return the postersHeights
	 */

	public int getEnrollBags() {
		return myGarbage.getGarbage().getCountBagsEnrollments();
		// #{mormanManager.myGarbage.garbage.bagCount}
	}

	public Garbage getGarbageSimplu() {
		return garbageSimplu;
		// #{mormanManager.myGarbage.garbage.bagCount}
	}

	public void setEnrollBags(int t) {

	}

	public List<Integer> getPosterHeights() {
		return posterHeights;
	}

	public boolean isRadiusDisabled() {
		if (myGarbage != null) {
			if (myGarbage.getGarbage() != null)
				return !myGarbage.getGarbage().isDispersed();
		}
		return false;
	}
}
