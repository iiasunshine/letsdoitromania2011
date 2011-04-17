package ro.ldir.beans;

import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.ChartedArea;

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
	public List<ChartedArea> getChartedAreas(float topLeftX, float topLeftY,
			float bottomRightX, float bottomRightY);

	/**
	 * Finds a chart area by ID.
	 * 
	 * @param chartedAreaId
	 *            The ID of the chart area to search for.
	 * @return
	 */
	public ChartedArea getChartedArea(int chartedAreaId);

	/**
	 * Creates a new chart area.
	 * 
	 * @param chartedArea
	 *            The new chart area to create.
	 */
	public void newChartedArea(ChartedArea chartedArea);

	/**
	 * Updates a chart area.
	 * 
	 * @param chartedAreaId
	 *            The chart area ID to update.
	 * @param chartedArea
	 *            The new settings.
	 */
	public void updateChartedArea(int chartedAreaId, ChartedArea chartedArea);
}
