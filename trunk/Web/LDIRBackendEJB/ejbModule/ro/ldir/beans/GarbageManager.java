/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2011 by the LDIR development team, further referred to 
 *  as "authors".
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Filename: GarbageManager.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Garbage.GarbageStatus;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.TownArea;
import ro.ldir.dto.User;
import ro.ldir.exceptions.NoCountyException;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Session bean managing garbages.
 * 
 * A garbage is defined by the {@link ro.ldir.dto.Garbage} entity bean.
 * 
 * The exported methods are defined by the
 * {@link ro.ldir.beans.GarbageManagerLocal} interface. Access to this bean is
 * exported by the webservice defined by the
 * {@link ro.ldir.ws.GarbageWebService}.
 * 
 * @see ro.ldir.dto.Garbage
 * @see ro.ldir.beans.GarbageManagerLocal
 * @see ro.ldir.ws.UserWebService
 */
@Stateless
@LocalBean
@DeclareRoles({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
public class GarbageManager implements GarbageManagerLocal {
	private static final String DISPLAY_PREFIX = "display";
	private static final String IMAGE_JPG = "JPEG";
	private static Logger logger = Logger.getLogger(GarbageManagerLocal.class
			.getName());
	private static final String THUMB_PREFIX = "thumb";

	@Resource
	private SessionContext ctx;

	@Resource
	private Integer displayHeight;

	@Resource
	private Integer displayWidth;

	@PersistenceContext(unitName = "ldir")
	EntityManager em;

	@EJB
	private GarbageGroupManager garbageGroupManager;

	@EJB
	private GeoManager geoManager;

	/**
	 * The size of the buffer used during image transfer from the temporary
	 * location to the final directory. It is loaded as an EJB environment
	 * entry.
	 */
	@Resource
	private Integer imgBufSize;
	/**
	 * The directory where images are stored. It is loaded as an EJB environment
	 * entry.
	 */
	@Resource
	private String imgLocation;
	@Resource
	private Float resizedQuality;
	@Resource
	private Integer thumbWidth;

	@EJB
	private UserManager userManager;

	/** Default constructor. */
	public GarbageManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#addNewImage(int, java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void addNewImage(int garbageId, File file, String originalName)
			throws FileNotFoundException, IOException {
		Garbage garbage = em.find(Garbage.class, garbageId);
		int size = garbage.getPictures().size();

		String pathSeparator = System.getProperty("file.separator");
		String directory = imgLocation + pathSeparator + garbageId
				+ pathSeparator;
		String destination = directory + size + "_" + originalName;

		InputStream imageStream = new BufferedInputStream(new FileInputStream(
				file));
		Image image = (Image) ImageIO.read(imageStream);

		double ratio = (double) thumbWidth.doubleValue() / image.getWidth(null);
		int height = (int) ((double) image.getHeight(null) * ratio);
		InputStream thumbIn = scaleImage(image, thumbWidth, height);

		File thumb = new File(directory + THUMB_PREFIX + "_" + size + "_"
				+ originalName);
		new File(thumb.getParent()).mkdir();
		copyFile(thumbIn, thumb);

		InputStream displayIn = scaleImage(image, displayWidth.intValue(),
				displayHeight.intValue());
		File display = new File(directory + DISPLAY_PREFIX + "_" + size + "_"
				+ originalName);
		copyFile(displayIn, display);

		File destFile = new File(destination);
		copyFile(file, destFile);

		garbage.getPictures().add(size + "_" + originalName);
		em.merge(garbage);
	}

	private void copyFile(File srcfile, File dstFile)
			throws FileNotFoundException, IOException {
		InputStream in = new FileInputStream(srcfile);
		OutputStream out = new FileOutputStream(dstFile);
		byte[] buf = new byte[imgBufSize];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	private void copyFile(InputStream in, File dstFile)
			throws FileNotFoundException, IOException {
		OutputStream out = new FileOutputStream(dstFile);
		byte[] buf = new byte[imgBufSize];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#deleteGarbage(int)
	 */
	@Override
	public void deleteGarbage(int garbageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		if (garbage.getPictures() != null)
			for (String filename : garbage.getPictures())
				deleteImageReplicas(garbageId, filename);

		garbage.getCounty().getGarbages().remove(garbage);
		em.merge(garbage.getCounty());

		if (garbage.getChartedArea() != null) {
			garbage.getChartedArea().getGarbages().remove(garbage);
			em.merge(garbage.getChartedArea());
		}

		if (garbage.getTown() != null) {
			garbage.getTown().getGarbages().remove(garbage);
			em.merge(garbage.getTown());
		}

		garbage.getInsertedBy().getGarbages().remove(garbage);
		em.merge(garbage.getInsertedBy());

		if (garbage.getGarbageEnrollements() != null)
			for (GarbageEnrollment enrollment : garbage
					.getGarbageEnrollements()) {
				enrollment.getTeam().getGarbageEnrollements()
						.remove(enrollment);
				em.merge(enrollment.getTeam());
			}
		garbageGroupManager.cleanupGroup(garbage);
		em.remove(garbage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#deleteImage(int, int)
	 */
	@Override
	public void deleteImage(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		deleteImageReplicas(garbageId, garbage.getPictures().get(imageId));
		garbage.getPictures().remove(imageId);
		em.merge(garbage);
	}

	private void deleteImageReplicas(int garbageId, String image) {
		String pathSeparator = System.getProperty("file.separator");
		String directory = imgLocation + pathSeparator + garbageId
				+ pathSeparator;
		File file = new File(directory + image);
		file.delete();
		file = new File(directory + THUMB_PREFIX + "_" + image);
		file.delete();
		file = new File(directory + DISPLAY_PREFIX + "_" + image);
		file.delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getGarbage(int)
	 */
	@Override
	public Garbage getGarbage(int garbageId) {
		return em.find(Garbage.class, garbageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getGarbages(double, double,
	 * double, double)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbages(double topLeftX, double topLeftY,
			double bottomRightX, double bottomRightY) {
		Query query = em.createQuery("SELECT g FROM Garbage g WHERE "
				+ "g.x BETWEEN :topLeftX AND :bottomRightX AND "
				+ "g.y BETWEEN :bottomRightY AND :topLeftY");
		query.setParameter("topLeftX", topLeftX);
		query.setParameter("topLeftY", topLeftY);
		query.setParameter("bottomRightX", bottomRightX);
		query.setParameter("bottomRightY", bottomRightY);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getGarbages(ro.ldir.dto.Garbage.
	 * GarbageStatus)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbages(Garbage.GarbageStatus status) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.status = :statusParam");
		query.setParameter("statusParam", status);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbagesByCounty(String county) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.county.name = :countyParam");
		query.setParameter("countyParam", county);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GarbageManagerLocal#getGarbagesByTown(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbagesByTown(String town) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.town.name = :townParam");
		query.setParameter("townParam", town);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getImageDisplayPath(int, int)
	 */
	@Override
	public String getImageDisplayPath(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		String pathSeparator = System.getProperty("file.separator");
		return imgLocation + pathSeparator + garbageId + pathSeparator
				+ DISPLAY_PREFIX + "_" + garbage.getPictures().get(imageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getImagePath(int, int)
	 */
	@Override
	public String getImagePath(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		String pathSeparator = System.getProperty("file.separator");
		return imgLocation + pathSeparator + garbageId + pathSeparator
				+ garbage.getPictures().get(imageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getImageThumbnailPath(int, int)
	 */
	@Override
	public String getImageThumbnailPath(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		String pathSeparator = System.getProperty("file.separator");
		return imgLocation + pathSeparator + garbageId + pathSeparator
				+ THUMB_PREFIX + "_" + garbage.getPictures().get(imageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#insertGarbage(ro.ldir.dto.Garbage)
	 */
	@Override
	public int insertGarbage(Garbage garbage) throws NoCountyException {
		setGeoFeatures(garbage);

		User user = SecurityHelper.getUser(userManager, ctx);
		if (user != null) {
			if (user.getGarbages() == null)
				user.setGarbages(new HashSet<Garbage>());
			user.getGarbages().add(garbage);
			garbage.setInsertedBy(user);
		}

		garbageGroupManager.findGroup(garbage, false);

		em.persist(garbage);
		em.flush();

		return garbage.getGarbageId().intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#report(java.util.Set,
	 * java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public List<Garbage> report(Set<String> counties,
			Set<String> chartedAreaNames, Set<Integer> userIds,
			Set<Date> insertDates) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Garbage> cq = cb.createQuery(Garbage.class);
		Root<Garbage> garbage = cq.from(Garbage.class);

		Predicate p = cb.conjunction();
		if (counties != null && counties.size() > 0) {
			Join<Garbage, CountyArea> garbageCounty = garbage.join("county");
			In<Object> countyExpression = cb.in(garbageCounty.get("name"));
			for (String county : counties)
				countyExpression = countyExpression.value(county);
			p = cb.and(p, countyExpression);
		}

		if (chartedAreaNames != null && chartedAreaNames.size() > 0) {
			Join<Garbage, ChartedArea> garbageChartedArea = garbage
					.join("chartedArea");
			In<Object> caExpression = cb.in(garbageChartedArea.get("name"));
			for (String ca : chartedAreaNames)
				caExpression = caExpression.value(ca);
			p = cb.and(p, caExpression);
		}

		if (userIds != null && userIds.size() > 0) {
			Join<Garbage, User> garbageInsertedBy = garbage.join("insertedBy");
			In<Object> userIdExpression = cb
					.in(garbageInsertedBy.get("userId"));
			for (Integer userId : userIds)
				userIdExpression = userIdExpression.value(userId);
			p = cb.and(p, userIdExpression);
		}

		if (insertDates != null && insertDates.size() > 0) {
			Predicate dp = null;
			for (Date date : insertDates) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				Date start = new GregorianCalendar(
						calendar.get(java.util.Calendar.YEAR),
						calendar.get(java.util.Calendar.MONTH),
						calendar.get(java.util.Calendar.DAY_OF_MONTH), 0, 0, 0)
						.getTime();
				Date stop = new GregorianCalendar(
						calendar.get(java.util.Calendar.YEAR),
						calendar.get(java.util.Calendar.MONTH),
						calendar.get(java.util.Calendar.DAY_OF_MONTH), 23, 59,
						59).getTime();
				Predicate b = cb.between(garbage.<Date> get("recordDate"),
						start, stop);
				if (dp == null) {
					dp = b;
					continue;
				}
				dp = cb.or(dp, b);
			}
			p = cb.and(p, dp);
		}

		cq.where(p);
		TypedQuery<Garbage> tq = em.createQuery(cq);
		return tq.getResultList();
	}

	private InputStream scaleImage(Image image, int width, int height)
			throws ImageFormatException, IOException {
		int thumbWidth = width;
		int thumbHeight = height;

		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		param.setQuality(resizedQuality.floatValue(), false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		ImageIO.write(thumbImage, IMAGE_JPG, out);

		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		return bis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#setGarbageStatus(int,
	 * ro.ldir.dto.Garbage.GarbageStatus)
	 */
	@Override
	public void setGarbageStatus(int garbageId, GarbageStatus status) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		garbage.setStatus(status);
		em.merge(garbage);
	}

	/**
	 * Sets the geographical features, i.e., the charted area, town and county
	 * this garbage is located.
	 * 
	 * @param garbage
	 *            The garbage to set.
	 * @throws NoCountyException
	 *             If there is no county containing this garbage.
	 */
	private void setGeoFeatures(Garbage garbage) throws NoCountyException {
		Point2D.Double p = new Point2D.Double(garbage.getX(), garbage.getY());

		CountyArea county = geoManager.getCountyArea(p);
		// If no county is found, the garbage is being inserted in an area not
		// covered by the system.
		if (county == null) {
			logger.info("Unable to find county for " + p);
			throw new NoCountyException(garbage);
		}

		ChartedArea ca = geoManager.getChartedArea(p);
		TownArea town = geoManager.getTownArea(p);

		garbage.setCounty(county);
		county.getGarbages().add(garbage);

		if (ca != null) {
			garbage.setChartedArea(ca);
			ca.getGarbages().add(garbage);
		}
		if (town != null) {
			garbage.setTown(town);
			town.getGarbages().add(garbage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#updateGarbage(java.lang.Integer,
	 * ro.ldir.dto.Garbage)
	 */
	@Override
	public void updateGarbage(Integer garbageId, Garbage garbage)
			throws NoCountyException {
		Garbage existing = em.find(Garbage.class, garbageId);
		updateGeoFeatures(existing, garbage);
		existing.copyFields(garbage);
		em.merge(existing);
	}

	/**
	 * Updates the geographical features, i.e., the charted area, town and
	 * county this garbage is located.
	 * 
	 * @param garbage
	 *            The garbage to set.
	 * @throws NoCountyException
	 *             If there is no county containing this garbage.
	 */
	private void updateGeoFeatures(Garbage existing, Garbage garbage)
			throws NoCountyException {
		boolean mustRemove = false, mustSet = false;
		Point2D.Double p = new Point2D.Double(garbage.getX(), garbage.getY());

		CountyArea existingCounty = existing.getCounty();
		TownArea oldTown = existing.getTown();
		ChartedArea oldCa = existing.getChartedArea();

		CountyArea county = geoManager.getCountyArea(p);
		// If no county is found, the garbage is being inserted in an area not
		// covered by the system.
		if (county == null)
			throw new NoCountyException();
		if (existingCounty.getAreaId() != county.getAreaId()) {
			existingCounty.getGarbages().remove(existing);
			existing.setCounty(county);
			county.getGarbages().add(existing);
			em.merge(existingCounty);
			em.merge(county);
		}

		ChartedArea newCa = geoManager.getChartedArea(p);
		mustRemove = oldCa != null
				&& (newCa == null || (newCa != null && oldCa.getAreaId() != newCa
						.getAreaId()));
		mustSet = newCa != null
				&& (oldCa == null || (oldCa != null && oldCa.getAreaId() != newCa
						.getAreaId()));
		if (mustRemove) {
			oldCa.getGarbages().remove(existing);
			em.merge(oldCa);
		}
		if (mustSet) {
			existing.setChartedArea(newCa);
			newCa.getGarbages().add(existing);
			em.merge(newCa);
		}

		TownArea newTown = geoManager.getTownArea(p);
		mustRemove = oldTown != null
				&& (newTown == null || (newTown != null && oldTown.getAreaId() != newTown
						.getAreaId()));
		mustSet = newTown != null
				&& (oldTown == null || (oldTown != null && oldTown.getAreaId() != newTown
						.getAreaId()));
		if (mustRemove) {
			oldTown.getGarbages().remove(existing);
			em.merge(oldTown);
		}
		if (mustSet) {
			existing.setTown(newTown);
			newTown.getGarbages().add(existing);
			em.merge(newTown);
		}
	}
}
