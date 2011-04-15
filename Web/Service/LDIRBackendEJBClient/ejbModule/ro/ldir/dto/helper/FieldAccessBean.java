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
 *  Filename: FieldAccessBean.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto.helper;

import java.lang.reflect.Field;

/**
 * The base DTO class that provide helper functions to compare, copy and update
 * beans.
 */
public abstract class FieldAccessBean {
	/**
	 * Copy fields from another object and skips the fields annotated with
	 * {@link NonTransferableField}.
	 * 
	 * @param other
	 *            The object to get fields from.
	 */
	public void copyFields(FieldAccessBean other) {
		if (!this.getClass().equals(other.getClass()))
			return;

		for (Field f : this.getClass().getFields()) {
			NonTransferableField a = f
					.getAnnotation(NonTransferableField.class);
			if (a != null)
				continue;
			try {
				f.set(this, f.get(other));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

	/**
	 * Compare this object against another object by having by having a look at
	 * each field and skipping these annotated with {@link NonComparableField}.
	 * 
	 * @param other
	 *            The object to compare against.
	 * @return {@code true} if all non-annotated fields match, {@code false}
	 *         otherwise.
	 */
	public boolean fieldsEqual(FieldAccessBean other) {
		if (!this.getClass().equals(other.getClass()))
			return false;

		for (Field f : this.getClass().getFields()) {
			NonComparableField a = f.getAnnotation(NonComparableField.class);
			if (a != null)
				continue;
			try {
				Object mine = f.get(this);
				Object his = f.get(other);
				if (mine == null && his == null)
					continue;
				if (mine == null || his == null
						|| !f.get(this).equals(f.get(other))) {
					System.out.println("different " + f.getName() + " " + mine
							+ " " + other);
					return false;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
