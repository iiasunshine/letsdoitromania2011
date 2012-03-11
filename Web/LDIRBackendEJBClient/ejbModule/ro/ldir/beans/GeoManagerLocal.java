package ro.ldir.beans;

import java.awt.geom.Point2D;
import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.TownArea;

@Local
public interface GeoManagerLocal {
	/**
	 * Deletes a chart area.
	 * 
	 * @param chartedAreaId
	 *            The chart area ID to delete.
	 */
	public void deleteChartedArea(int chartedAreaId);

	/**
	 * Deletes a county area.
	 * 
	 * @param countyAreaId
	 *            The county area ID to delete.
	 */
	public void deleteCountyArea(int countyAreaId);

	/**
	 * Deletes a town area.
	 * 
	 * @param townAreaId
	 *            The town area ID to delete.
	 */
	public void deleteTownArea(int townAreaId);

	/**
	 * Gets a list of all counties.
	 * 
	 * @return A list of all counties.
	 */
	public List<CountyArea> getAllCounties();

	/**
	 * Get a cached list of all counties. Useful only to fetch non-volatile
	 * properties of a county.
	 */
	public List<CountyArea> getCacheAllCounties();

	/**
	 * Finds a chart area by ID.
	 * 
	 * @param chartedAreaId
	 *            The ID of the chart area to search for.
	 * @return the charted area matching the ID.
	 */
	public ChartedArea getChartedArea(int chartedAreaId);

	/**
	 * Returns the chart area containing a given point.
	 * 
	 * @param point
	 *            The point for which the chart area is searched for.
	 * @return The chart area containing the point.
	 */
	public ChartedArea getChartedArea(Point2D.Double point);

	/**
	 * Gets the charted area bearing the exact name wrt the one given.
	 * 
	 * @param name
	 *            The name to search for.
	 * @return The charted area matching the name, {@code null} if none is
	 *         found.
	 */
	public ChartedArea getChartedArea(String name);

	/**
	 * Gets all the charting areas that intersect a bounding box.
	 * 
	 * @param topLeftX
	 *            The top left point X of the bounding box.
	 * @param topLeftY
	 *            The top left point Y of the bounding box.
	 * @param bottomRightX
	 *            The bottom right point X of the bounding box.
	 * @param bottomRightY
	 *            The bottom right point Y of the bounding box.
	 * @return A list of charting areas that intersect the bounding box.
	 */
	public List<ChartedArea> getChartedAreas(double topLeftX, double topLeftY,
			double bottomRightX, double bottomRightY);

	/**
	 * Gets all charted areas that bear a name similar to the provided name.
	 * 
	 * @param name
	 *            The name to search for.
	 * @return A list of charted area that bear the given name.
	 */
	public List<ChartedArea> getChartedAreas(String name);

	/**
	 * Gets all charted areas assigned to a team.
	 * 
	 * @param teamId
	 *            The team ID to search for.
	 * @return the list of charted areas charted by the provided team
	 */
	public List<ChartedArea> getChartedAreasByChartedBy(int teamId);

	/**
	 * Gets all charted areas in a county.
	 * 
	 * @param county
	 *            The county to search in.
	 * @return A list of all charted areas in the county.
	 */
	public List<ChartedArea> getChartedAreasByCounty(String county);

	/**
	 * Finds an county area by ID.
	 * 
	 * @param countyAreaId
	 *            The ID of the county area to search for.
	 * @return The county area that matches the given ID.
	 */
	public CountyArea getCountyArea(int countyAreaId);

	/**
	 * Returns the county area containing a given point.
	 * 
	 * @param point
	 *            The point for which the county area is searched for.
	 * @return The county area containing the point.
	 */
	public CountyArea getCountyArea(Point2D.Double point);

	/**
	 * Finds an town area by ID.
	 * 
	 * @param townAreaId
	 *            The ID of the town area to search for.
	 * @return The town area matching the given ID.
	 */
	public TownArea getTownArea(int townAreaId);

	/**
	 * Returns the town area containing a given point.
	 * 
	 * @param point
	 *            The point for which the town area is searched for.
	 * @return The town area containing the point.
	 */
	public TownArea getTownArea(Point2D.Double point);

	/**
	 * Creates a new chart area.
	 * 
	 * @param chartedArea
	 *            The new chart area to create.
	 */
	public void newChartedArea(ChartedArea chartedArea);

	/**
	 * Creates a new county area.
	 * 
	 * @param countyArea
	 *            The new county area to create.
	 */
	public void newCountyArea(CountyArea countyArea);

	/**
	 * Creates a new town area.
	 * 
	 * @param townArea
	 *            The new town area to create.
	 */
	public void newTownArea(TownArea townArea);

	/**
	 * Sets the percentage completed of a charted area.
	 * 
	 * @param chartedAreaId
	 * @param percentage
	 */
	public void setPercentageCompleted(int chartedAreaId, int percentage);

	/**
	 * Updates a chart area.
	 * 
	 * @param chartedAreaId
	 *            The chart area ID to update.
	 * @param chartedArea
	 *            The new settings.
	 */
	public void updateChartedArea(int chartedAreaId, ChartedArea chartedArea);

	/**
	 * Updates an county area.
	 * 
	 * @param countyAreaId
	 *            The county area ID to update.
	 * @param countyArea
	 *            The new settings.
	 */
	public void updateCountyArea(int countyAreaId, CountyArea countyArea);

	/**
	 * Updates an town area.
	 * 
	 * @param townAreaId
	 *            The town area ID to update.
	 * @param townArea
	 *            The new settings.
	 */
	public void updateTownArea(int townAreaId, TownArea townArea);
}
