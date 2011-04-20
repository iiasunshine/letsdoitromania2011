package ro.ldir.beans;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.TownArea;

/**
 * Session Bean implementation class GeoManager
 */
@Stateless
@LocalBean
@DeclareRoles({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
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
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public void deleteChartedArea(int chartedAreaId) {
		ChartedArea chartedArea = em.find(ChartedArea.class, chartedAreaId);
		em.remove(chartedArea);
	}

	@Override
	@RolesAllowed("ADMIN")
	public void deleteCountyArea(int countyAreaId) {
		CountyArea countyArea = em.find(CountyArea.class, countyAreaId);
		em.remove(countyArea);
	} /*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#getCountyArea(int)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#deleteTownArea(int)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void deleteTownArea(int townAreaId) {
		TownArea townArea = em.find(TownArea.class, townAreaId);
		em.remove(townArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GeoManagerLocal#getChartedArea(java.awt.geom.Point2D.Float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ChartedArea getChartedArea(Float point) {
		Query query = em.createQuery("SELECT ca FROM ChartedArea ca WHERE "
				+ ":x BETWEEN ca.topLeftX AND ca.bottomRightX AND "
				+ ":y BETWEEN ca.bottomRightY AND ca.topLeftY");
		query.setParameter("x", point.x);
		query.setParameter("y", point.y);
		List<ChartedArea> areas = query.getResultList();
		for (ChartedArea a : areas)
			if (a.containsPoint(point))
				return a;
		return null;
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
	 * @see ro.ldir.beans.GeoManagerLocal#getChartedAreas(float, float, float,
	 * float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChartedArea> getChartedAreas(float topLeftX, float topLeftY,
			float bottomRightX, float bottomRightY) {
		Query query = em.createQuery("SELECT ca FROM ChartedArea ca WHERE "
				+ "(ca.topLeftX BETWEEN :topLeftX AND :bottomRightX OR"
				+ " ca.bottomRightX BETWEEN :topLeftX AND :bottomRightX OR"
				+ " :topLeftX BETWEEN ca.topLeftX AND ca.bottomRightX)"
				+ " AND "
				+ "(ca.topLeftY BETWEEN :bottomRightY AND :topLeftY OR"
				+ " ca.bottomRightY BETWEEN :bottomRightY AND :topLeftY OR"
				+ " :bottomRightY BETWEEN ca.bottomRightY and ca.topLeftY)");
		query.setParameter("topLeftX", topLeftX);
		query.setParameter("topLeftY", topLeftY);
		query.setParameter("bottomRightX", bottomRightX);
		query.setParameter("bottomRightY", bottomRightY);
		return query.getResultList();
	}

	@Override
	public CountyArea getCountyArea(int countyAreaId) {
		return em.find(CountyArea.class, countyAreaId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GeoManagerLocal#getCountyArea(java.awt.geom.Point2D.Float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CountyArea getCountyArea(Point2D.Float point) {
		Query query = em.createQuery("SELECT aa FROM CountyArea aa WHERE "
				+ ":x BETWEEN aa.topLeftX AND aa.bottomRightX AND "
				+ ":y BETWEEN aa.bottomRightY AND aa.topLeftY");
		query.setParameter("x", point.x);
		query.setParameter("y", point.y);
		List<CountyArea> areas = query.getResultList();
		for (CountyArea a : areas)
			if (a.containsPoint(point))
				return a;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#getTownArea(int)
	 */
	@Override
	public TownArea getTownArea(int townAreaId) {
		return em.find(TownArea.class, townAreaId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GeoManagerLocal#getTownArea(java.awt.geom.Point2D.Float)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TownArea getTownArea(Point2D.Float point) {
		Query query = em.createQuery("SELECT aa FROM TownArea aa WHERE "
				+ ":x BETWEEN aa.topLeftX AND aa.bottomRightX AND "
				+ ":y BETWEEN aa.bottomRightY AND aa.topLeftY");
		query.setParameter("x", point.x);
		query.setParameter("y", point.y);
		List<TownArea> areas = query.getResultList();
		for (TownArea a : areas)
			if (a.containsPoint(point))
				return a;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.GeoManagerLocal#newChartedArea(ro.ldir.dto.ChartedArea)
	 */
	@Override
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public void newChartedArea(ChartedArea chartedArea) {
		// TODO make a check whether it intersects other charting areas
		chartedArea.setBoundingBox();
		em.persist(chartedArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#newCountyArea(ro.ldir.dto.CountyArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void newCountyArea(CountyArea countyArea) {
		// TODO make a check whether it intersects other county areas
		countyArea.setBoundingBox();
		em.persist(countyArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#newTownArea(ro.ldir.dto.TownArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void newTownArea(TownArea townArea) {
		// TODO make a check whether it intersects other town areas
		townArea.setBoundingBox();
		em.persist(townArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#updateChartedArea(int,
	 * ro.ldir.dto.ChartedArea)
	 */
	@Override
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public void updateChartedArea(int chartedAreaId, ChartedArea chartedArea) {
		ChartedArea existing = em.find(ChartedArea.class, chartedAreaId);
		existing.copyFields(chartedArea);
		existing.setBoundingBox();
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#updateCountyArea(int,
	 * ro.ldir.dto.CountyArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void updateCountyArea(int countyAreaId, CountyArea countyArea) {
		CountyArea existing = em.find(CountyArea.class, countyAreaId);
		existing.copyFields(countyArea);
		existing.setBoundingBox();
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#updateTownArea(int,
	 * ro.ldir.dto.TownArea)
	 */
	@Override
	@RolesAllowed("ADMIN")
	public void updateTownArea(int townAreaId, TownArea townArea) {
		TownArea existing = em.find(TownArea.class, townAreaId);
		existing.copyFields(townArea);
		existing.setBoundingBox();
		em.merge(existing);
	} /*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GeoManagerLocal#deleteCountyArea(int)
	 */
}
