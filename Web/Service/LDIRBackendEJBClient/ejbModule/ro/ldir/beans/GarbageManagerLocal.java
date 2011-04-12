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
 *  Filename: GarbageManagerLocal.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.Garbage.Status;

@Local
public interface GarbageManagerLocal {

	public void addNewImage(int garbageId, File file, String originalName)
			throws FileNotFoundException, IOException;

	public void deleteImage(int garbageId, int imageId);

	public Garbage getGarbage(int garbageId);

	public List<Garbage> getGarbages(Status status);

	public List<Garbage> getGarbagesByCounty(String county);

	public List<Garbage> getGarbagesByTown(String town);

	public String getImagePath(int garbageId, int imageId);

	public void setGarbageStatus(int garbageId, Status status);
}
