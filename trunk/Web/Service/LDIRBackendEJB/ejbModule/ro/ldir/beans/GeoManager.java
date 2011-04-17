package ro.ldir.beans;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.ChartedArea;

/**
 * Session Bean implementation class GeoManager
 */
@Stateless
@LocalBean
@DeclareRoles("ADMIN")
public class GeoManager implements GeoManagerLocal {
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	/** Default constructor. */
	public GeoManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#deleteChartedArea(int)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void deleteChartedArea(int chartedAreaId) {
		ChartedArea chartedArea = em.find(ChartedArea.class, chartedAreaId);
		em.remove(chartedArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#getChartedAreas(float, float, float,
	 * float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChartedArea> getChartedAreas(float topLeftX, float topLeftY,
			float bottomRightX, float bottomRightY) {
		Query query = em.createQuery("SELECT ca FROM ChartedArea ca WHERE"
				+ "(ca.topLeftX BETWEEN :topLeftX AND :bottomRightX AND"
				+ " ca.topLeftY BETWEEN :bottomRightY AND :topLeftY) OR "
				+ "(ca.bottomRightX BETWEEN :topLeftX AND :bottomRightX AND"
				+ " ca.bottomRightY BETWEEN :bottomRightY AND :topLeftY)");
		query.setParameter("topLeftX", topLeftX);
		query.setParameter("topLeftY", topLeftY);
		query.setParameter("bottomRightX", bottomRightX);
		query.setParameter("bottomRightY", bottomRightY);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#getChartedArea(int)
	 */
	@Override
	public ChartedArea getChartedArea(int chartedAreaId) {
		return em.find(ChartedArea.class, chartedAreaId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GeoManagerLocal#newChartedArea(ro.ldir.dto.ChartedArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void newChartedArea(ChartedArea chartedArea) {
		// TODO make a check whether it intersects other charting areas
		chartedArea.setBoundingBox();
		em.persist(chartedArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#updateChartedArea(int,
	 * ro.ldir.dto.ChartedArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void updateChartedArea(int chartedAreaId, ChartedArea chartedArea) {
		ChartedArea existing = em.find(ChartedArea.class, chartedAreaId);
		existing.copyFields(chartedArea);
		existing.setBoundingBox();
		em.merge(existing);
	}
}
