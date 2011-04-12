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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.Garbage.Status;

/**
 * Session Bean implementation class GarbageManager
 */
@Stateless
@LocalBean
public class GarbageManager implements GarbageManagerLocal {
	@PersistenceContext(unitName = "ldir")
	EntityManager em;

	@Resource
	private Integer imgBufSize;
	@Resource
	private String imgLocation;

	/**
	 * Default constructor.
	 */
	public GarbageManager() {
	}

	@Override
	public void addNewImage(int garbageId, File file, String originalName)
			throws FileNotFoundException, IOException {
		Garbage garbage = em.find(Garbage.class, garbageId);
		int size = 0;
		if (garbage.pictures != null)
			size = garbage.pictures.size();

		String pathSeparator = System.getProperty("file.separator");

		String destination = imgLocation + pathSeparator + garbageId
				+ pathSeparator + size + "_" + originalName;
		File destFile = new File(destination);
		new File(destFile.getParent()).mkdir();
		copyFile(file, destFile);

		if (garbage.pictures == null)
			garbage.pictures = new ArrayList<String>();
		garbage.pictures.add(destination);
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

	@Override
	public void deleteImage(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		int i = 0;
		for (String filename : garbage.pictures) {
			if (i == imageId) {
				garbage.pictures.remove(filename);
				return;
			}
			i++;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public Garbage getGarbage(int garbageId) {
		return em.find(Garbage.class, garbageId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbages(Status status) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE x.status = :statusParam");
		query.setParameter("statusParam", status);
		return (List<Garbage>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbagesByCounty(String county) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE county = :countyParam");
		query.setParameter("countyParam", county);
		return (List<Garbage>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Garbage> getGarbagesByTown(String town) {
		Query query = em
				.createQuery("SELECT x FROM Garbage x WHERE town = :townParam");
		query.setParameter("townParam", town);
		return (List<Garbage>) query.getResultList();
	}

	@Override
	public String getImagePath(int garbageId, int imageId) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		int i = 0;
		for (String filename : garbage.pictures) {
			if (i == imageId)
				return filename;
			i++;
		}
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void setGarbageStatus(int garbageId, Status status) {
		Garbage garbage = em.find(Garbage.class, garbageId);
		garbage.status = status;
		em.merge(garbage);
	}
}
