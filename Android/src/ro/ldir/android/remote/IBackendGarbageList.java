package ro.ldir.android.remote;

import java.util.LinkedList;

import ro.ldir.android.entities.Garbage;

/**
 *  This file is part of the LDIRAndroid - the Android client for the Let's Do It
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
 *
 *  Filename: IBackendGarbageList.java
 *  Author(s): Catalin Mincu, cata.mincu@gmail.com
 *
 *  Represents the interface of the list of garbage objects common 
 *  between the mobile application and the backend
 */
public interface IBackendGarbageList {

	public LinkedList<Garbage> getGarbage();
	public void setGarbage(LinkedList<Garbage> garbageList);
}
