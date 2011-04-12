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

/**
 * The local business interface of the bean managing garbages.
 * 
 * @see ro.ldir.beans.GarbageManager
 */
@Local
public interface GarbageManagerLocal {
	/**
	 * Assigns a new image to a a garbage.
	 * 
	 * @param garbageId
	 *            The ID of the garbage for which the image should be assigned.
	 * @param file
	 *            The (temporary) file containing the image to be assign.
	 * @param originalName
	 *            The original name of the image.
	 * @throws FileNotFoundException
	 *             When the provided file does not exist.
	 * @throws IOException
	 *             if the file cannot be copied.
	 */
	public void addNewImage(int garbageId, File file, String originalName)
			throws FileNotFoundException, IOException;

	/**
	 * Deletes an image.
	 * 
	 * @param garbageId
	 *            The garbage the image to be delted belongs to.
	 * @param imageId
	 *            The image ID.
	 */
	public void deleteImage(int garbageId, int imageId);

	/**
	 * Searches a garbage by ID.
	 * 
	 * @param garbageId
	 *            The ID used in lookup.
	 * @return the garbage object.
	 */
	public Garbage getGarbage(int garbageId);

	/**
	 * Searches a garbage by status.
	 * 
	 * @param status
	 *            The status used to match garbages against.
	 * @return a list of garbages having the desired status.
	 * @see ro.ldir.dto.Garbage.Status
	 */
	public List<Garbage> getGarbages(Status status);

	/**
	 * Searches garbages by county.
	 * 
	 * @param county
	 *            The county of interest.
	 * @return a list of garbages in the given county.
	 */
	public List<Garbage> getGarbagesByCounty(String county);

	/**
	 * Searches garbages by town.
	 * 
	 * @param town
	 *            The town of interest.
	 * @return a list of garbages in the given town.
	 */
	public List<Garbage> getGarbagesByTown(String town);

	/**
	 * Returns the location of a file.
	 * 
	 * @param garbageId
	 *            The ID of the garbage that the image belongs to.
	 * @param imageId
	 *            The ID of the image.
	 * @return The full path where the image file can be located.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the imageId is an invalid image for the given garbage.
	 */
	public String getImagePath(int garbageId, int imageId);

	/**
	 * Sets a garbage status.
	 * 
	 * @param garbageId
	 *            The ID of the garbage whose status is to be changed
	 * @param status
	 *            The new status of the garbabe.
	 * 
	 * @see ro.ldir.dto.Garbage.Status
	 */
	public void setGarbageStatus(int garbageId, Status status);
}
