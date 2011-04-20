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

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Garbage.GarbageStatus;
import ro.ldir.dto.TownArea;
import ro.ldir.dto.User;

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
public class GarbageManager implements GarbageManagerLocal {
	@Resource
	private SessionContext ctx;

	@PersistenceContext(unitName = "ldir")
	EntityManager em;

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
		// TODO check that the file is actually an image.
		Garbage garbage = em.find(Garbage.class, garbageId);
		int size = garbage.getPictures().size();

		String pathSeparator = System.getProperty("file.separator");

		String destination = imgLocation + pathSeparator + garbageId
				+ pathSeparator + size + "_" + originalName;
		File destFile = new File(destination);
		new File(destFile.getParent()).mkdir();
		copyFile(file, destFile);

		garbage.getPictures().add(destination);
		em.merge(garbage);
		System.out.println("file ok");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#deleteImage(int, int)
	 */
	@Override
	public void deleteImage(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		String filename = garbage.getPictures().get(imageId);
		File file = new File(filename);
		file.delete();
		garbage.getPictures().remove(imageId);
		em.merge(garbage);
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
	 * @see ro.ldir.beans.GarbageManagerLocal#getGarbages(ro.ldir.dto.Garbage.
	 * GarbageStatus)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<Garbage> getGarbages(Garbage.GarbageStatus status) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.status = :statusParam");
		query.setParameter("statusParam", status);
		return (Set<Garbage>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Garbage> getGarbagesByCounty(String county) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.county.name = :countyParam");
		query.setParameter("countyParam", county);
		return (Set<Garbage>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GarbageManagerLocal#getGarbagesByTown(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<Garbage> getGarbagesByTown(String town) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.town.name = :townParam");
		query.setParameter("townParam", town);
		return (Set<Garbage>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#getImagePath(int, int)
	 */
	@Override
	public String getImagePath(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		return garbage.getPictures().get(imageId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageManagerLocal#insertGarbage(ro.ldir.dto.Garbage)
	 */
	@Override
	public void insertGarbage(Garbage garbage) {

		User user = SecurityHelper.getUser(userManager, ctx);

		if (user.getGarbages() == null)
			user.setGarbages(new HashSet<Garbage>());
		user.getGarbages().add(garbage);
		garbage.setInsertedBy(user);

		Point2D.Float p = new Point2D.Float(garbage.getX(), garbage.getY());

		ChartedArea ca = geoManager.getChartedArea(p);
		if (ca != null) {
			garbage.setChartedArea(ca);
			ca.getGarbages().add(garbage);
			em.merge(ca);
		}

		CountyArea county = geoManager.getCountyArea(p);
		if (county != null) {
			garbage.setCounty(county);
			county.getGarbages().add(garbage);
			em.merge(county);
		}

		TownArea town = geoManager.getTownArea(p);
		if (town != null) {
			garbage.setTown(town);
			town.getGarbages().add(garbage);
			em.merge(town);
		}

		em.persist(garbage);
		em.merge(user);
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
}
