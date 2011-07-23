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
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ro.ldir.dto.Garbage;
import ro.ldir.exceptions.NoCountyException;

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
	 * Delets a garbage.
	 * 
	 * @param garbageId
	 *            The ID of the garbage to delete.
	 */
	public void deleteGarbage(int garbageId);

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
	 * Gets all the garbages that are located inside a bounding box.
	 * 
	 * @param topLeftX
	 *            The top left point X of the bounding box.
	 * @param topLeftY
	 *            The top left point Y of the bounding box.
	 * @param bottomRightX
	 *            The bottom right point X of the bounding box.
	 * @param bottomRightY
	 *            The bottom right point Y of the bounding box.
	 * @return A list of garbages inside the bounding box.
	 */
	public List<Garbage> getGarbages(double topLeftX, double topLeftY,
			double bottomRightX, double bottomRightY);

	/**
	 * Searches a garbage by status.
	 * 
	 * @param status
	 *            The status used to match garbages against.
	 * @return a list of garbages having the desired status.
	 * @see ro.ldir.dto.Garbage.Status
	 */
	public List<Garbage> getGarbages(Garbage.GarbageStatus status);

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
	 * Returns the location of the display image.
	 * 
	 * @param garbageId
	 *            The ID of the garbage that the image belongs to.
	 * @param imageId
	 *            The ID of the image.
	 * @return The full path where the image file can be located.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the imageId is an invalid image for the given garbage.
	 */
	public String getImageDisplayPath(int garbageId, int imageId);

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
	 * Returns the location of the thumbnail of a file.
	 * 
	 * @param garbageId
	 *            The ID of the garbage that the image belongs to.
	 * @param imageId
	 *            The ID of the image.
	 * @return The full path where the image file can be located.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the imageId is an invalid image for the given garbage.
	 */
	public String getImageThumbnailPath(int garbageId, int imageId);

	/**
	 * Inserts a new garbage in the database.
	 * 
	 * The operation fails if the coordinates of the garbage are not contained
	 * within a county of the database.
	 * 
	 * @param garbage
	 *            The garbage to insert.
	 * @return The ID of the inserted garbage.
	 * @throws NoCountyException
	 *             If there is no county containing the garbage.
	 */
	public int insertGarbage(Garbage garbage) throws NoCountyException;

	/**
	 * Lists all garbages matching the specified query parameters. Use
	 * {@code null} if you do not want to use any of the query parameters.
	 * 
	 * @param counties
	 *            A set of county names where to search.
	 * @param chartedAreaNames
	 *            A set of charted area names where to search.
	 * @param userIds
	 *            A set of the ID of the users that inserted the garbages.
	 * @param insertDates
	 *            A set of garbage insertion dates.
	 * @return A list of garbages matching the specified query criteria.
	 */
	public List<Garbage> report(Set<String> counties,
			Set<String> chartedAreaNames, Set<Integer> userIds,
			Set<Date> insertDates);

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
	public void setGarbageStatus(int garbageId, Garbage.GarbageStatus status);

	/**
	 * Updates a garbage.
	 * 
	 * @param garbageId
	 *            The ID of the garbage to update.
	 * @param garbage
	 *            The new garbage settings. * @throws NoCountyException If there
	 *            is no county containing the updated coordinates of the
	 *            garbage.
	 */
	public void updateGarbage(Integer garbageId, Garbage garbage)
			throws NoCountyException;
}
