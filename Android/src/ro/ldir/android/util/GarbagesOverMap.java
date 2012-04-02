package ro.ldir.android.util;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * This file is part of the LDIRAndroid - the Android client for the Let's Do It
 * Romania 2011 Garbage collection campaign. Copyright (C) 2011 by the LDIR
 * development team, further referred to as "authors".
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Filename: GarbageMapActivity.java Author(s): Catalin Mincu,
 * cata.mincu@gmail.com
 * 
 */

public class GarbagesOverMap extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapGarbages = new ArrayList<OverlayItem>();

	private GeoPoint mapCenter;

	private Context context;

	public GarbagesOverMap(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public GarbagesOverMap(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapGarbages.get(i);
	}

	@Override
	public int size() {
		return mapGarbages.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mapGarbages.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapGarbages.add(overlay);
		this.populate();
	}

	public GeoPoint getMapCenter() {
		return mapCenter;
	}

	public void setMapCenter(GeoPoint mapCenter) {
		this.mapCenter = mapCenter;
	}

}
