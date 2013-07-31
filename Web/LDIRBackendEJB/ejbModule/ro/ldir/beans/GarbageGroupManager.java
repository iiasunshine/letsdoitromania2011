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
 *  Filename: GarbageGroupManager.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.GarbageGroup;

/**
 * Session bean managing garbage groups.
 */
@Stateless
@LocalBean
public class GarbageGroupManager implements GarbageGroupManagerLocal {

	private static Logger logger = Logger.getLogger(GarbageGroupManager.class
			.getName());

	@PersistenceContext(unitName = "ldir")
	EntityManager em;

	@EJB
	private GarbageManager garbageManager;

	/**
	 * Removes a garbage from its garbage group. If the removed garbage is the
	 * last one in the garbage group, it also deletes the garbage group from the
	 * persistence.
	 * 
	 * @param garbage
	 *            The garbage to remove
	 */
	public void cleanupGroup(Garbage garbage) {
		GarbageGroup group = garbage.getGarbageGroup();
		if (group == null)
			return;
		group.getGarbages().remove(garbage);
		if (group.getGarbages().size() == 0)
			em.remove(group);
		else
			em.merge(group);
	}

	private void createGroup(Garbage garbage, boolean merge) {
		GarbageGroup group = new GarbageGroup();
		List<Garbage> garbages = new ArrayList<Garbage>();

		garbages.add(garbage);
		group.setGarbages(garbages);
		garbage.setGarbageGroup(group);
		if (merge)
			em.persist(group);
	}

	/**
	 * Finds a group where this garbage should be added. It additionally merges
	 * groups that the given garbage might link.
	 * 
	 * @param garbage
	 *            The garbage to find the group for.
	 * @param merge
	 *            Indicates whether the garbage is a new one (to be persisted in
	 *            the database) or an already existing one (to be merged in the
	 *            database).
	 */
	public void findGroup(Garbage garbage, boolean merge) {
		Set<GarbageGroup> neighboringGroups = findNeighboringGroups(garbage);

		logger.finest("garbage has " + neighboringGroups
				+ " neighboring groups");

		if (neighboringGroups == null)
			return;

		if (neighboringGroups.size() == 0) {
			createGroup(garbage, merge);
			logger.finer("garbage group " + garbage.getGarbageGroup() + " "
					+ merge);
			return;
		}

		if (neighboringGroups.size() == 1) {
			GarbageGroup group = neighboringGroups.iterator().next();
			if (group.getGarbages().size() < GarbageGroup.MAX_GROUP_SIZE) {
				group.getGarbages().add(garbage);
				garbage.setGarbageGroup(group);
				if (!merge)
					em.merge(group);
			} else
				createGroup(garbage, merge);
			return;
		}

		GarbageGroup mergedGroup = mergeGroups(neighboringGroups);
		if (mergedGroup == null) {
			createGroup(garbage, merge);
			return;
		}
		mergedGroup.getGarbages().add(garbage);
		garbage.setGarbageGroup(mergedGroup);
	}

	private Set<GarbageGroup> findNeighboringGroups(Garbage garbage) {
		Set<GarbageGroup> groups = new HashSet<GarbageGroup>();
		List<Garbage> garbages = garbageManager.getGarbages(garbage.getX()
				- GarbageGroup.JOIN_RANGE, garbage.getY()
				+ GarbageGroup.JOIN_RANGE, garbage.getX()
				+ GarbageGroup.JOIN_RANGE, garbage.getY()
				- GarbageGroup.JOIN_RANGE,null);

		logger.finest("found " + garbages.size() + " neighbors for "
				+ garbage.getX() + "," + garbage.getY());

		for (Garbage g : garbages) {
			if (garbage.getGarbageId() != null
					&& garbage.getGarbageId().equals(g.getGarbageId()))
				continue;
			if (g.getGarbageGroup() != null)
				groups.add(g.getGarbageGroup());
			else
				logger.finer(g.getGarbageId() + " has no group!");
		}
		return groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.GarbageGroupManagerLocal#getGarbageGroups(double,
	 * double, double, double)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GarbageGroup> getGarbageGroups(double topLeftX,
			double topLeftY, double bottomRightX, double bottomRightY) {
		Query query = em.createQuery("SELECT gg FROM GarbageGroup gg WHERE "
				+ "gg.x BETWEEN :topLeftX AND :bottomRightX AND "
				+ "gg.y BETWEEN :bottomRightY AND :topLeftY");
		query.setParameter("topLeftX", topLeftX);
		query.setParameter("topLeftY", topLeftY);
		query.setParameter("bottomRightX", bottomRightX);
		query.setParameter("bottomRightY", bottomRightY);
		List<GarbageGroup> result = query.getResultList();
		return result;
	}

	/**
	 * Assigns a group to each garbage, assuming no garbage groups already exist
	 * in the database.
	 */
	public void initializeGarbageGroups() {
		Query query = em.createQuery("SELECT COUNT(gg) FROM GarbageGroup gg");
		Number cnt = (Number) query.getSingleResult();
		if (cnt.intValue() > 0) {
			logger.info("Skipping garbage groups initialization, as " + cnt
					+ " groups already exist.");
			return;
		}

		logger.info("Initializing garbage groups...");

		query = em.createQuery("SELECT g FROM Garbage g");
		@SuppressWarnings("unchecked")
		List<Garbage> garbages = (List<Garbage>) query.getResultList();
		long progress = 0, last_display = 0;
		for (Garbage garbage : garbages) {
			progress++;
			long percentage = progress * 100 / garbages.size();
			if (percentage != last_display) {
				last_display = percentage;
				logger.info("Completed " + percentage + "% garbages.");
			}
			findGroup(garbage, true);
			em.merge(garbage);
		}

		logger.info("Initialization of garbage groups completed!");
	}

	private GarbageGroup mergeGroups(Set<GarbageGroup> groups) {
		GarbageGroup newGroup = new GarbageGroup();
		Set<GarbageGroup> toDelete = new HashSet<GarbageGroup>();

		for (GarbageGroup group : groups) {
			if (group.getGarbages().size() >= GarbageGroup.MAX_GROUP_SIZE)
				continue;
			// leave one spot for the new garbage
			if (newGroup.merge(group, GarbageGroup.MAX_GROUP_SIZE - 1))
				toDelete.add(group);
		}

		if (newGroup.getGarbages() == null)
			return null;

		for (GarbageGroup group : toDelete)
			em.remove(group);
		for (Garbage garbage : newGroup.getGarbages())
			em.merge(garbage);
		return newGroup;
	}
}
