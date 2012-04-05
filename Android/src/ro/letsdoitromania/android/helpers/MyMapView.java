package ro.letsdoitromania.android.helpers;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

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
 * Filename: MyMapView.java Author(s): Catalin Mincu,
 * cata.mincu@gmail.com
 * 
 */

public class MyMapView extends MapView {
	// ------------------------------------------------------------------------
    // LISTENER DEFINITIONS
    // ------------------------------------------------------------------------
 
    // Change listener
    public interface OnChangeListener
    {
        public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom);
        public void onDblTap(MapView view, GeoPoint point);
    }
 
    // ------------------------------------------------------------------------
    // MEMBERS
    // ------------------------------------------------------------------------
 
    private MyMapView mThis;
    private long mEventsTimeout = 250L;     // Set this variable to your preferred timeout
    private boolean mIsTouched = false;
    private GeoPoint mLastCenterPosition;
    private int mLastZoomLevel;
    private Timer mChangeDelayTimer = new Timer();
    private MyMapView.OnChangeListener mChangeListener = null;
    private long lastTouchTime = -1;
    
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
 
    public MyMapView(Context context, String apiKey)
    {
        super(context, apiKey);
        init();
    }
 
    public MyMapView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
 
    public MyMapView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
 
    private void init()
    {
        mThis = this;
        mLastCenterPosition = this.getMapCenter();
        mLastZoomLevel = this.getZoomLevel();
    }
 
    // ------------------------------------------------------------------------
    // GETTERS / SETTERS
    // ------------------------------------------------------------------------
 
    public void setOnChangeListener(MyMapView.OnChangeListener l)
    {
        mChangeListener = l;
    }
 
    // ------------------------------------------------------------------------
    // EVENT HANDLERS
    // ------------------------------------------------------------------------
 
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // Set touch internal
        mIsTouched = (ev.getAction() != MotionEvent.ACTION_UP);
 
        return super.onTouchEvent(ev);
    }
 
    @Override
    public void computeScroll()
    {
        super.computeScroll();
 
        // Check for change
        if (isSpanChange() || isZoomChange())
        {
            // If computeScroll called before timer counts down we should drop it and
            // start counter over again
            resetMapChangeTimer();
        }
    }
 
    // ------------------------------------------------------------------------
    // TIMER RESETS
    // ------------------------------------------------------------------------
 
    private void resetMapChangeTimer()
    {
        mChangeDelayTimer.cancel();
        mChangeDelayTimer = new Timer();
        mChangeDelayTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (mChangeListener != null) mChangeListener.onChange(mThis, getMapCenter(), mLastCenterPosition, getZoomLevel(), mLastZoomLevel);
                mLastCenterPosition = getMapCenter();
                mLastZoomLevel = getZoomLevel();
            }
        }, mEventsTimeout);
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

      if (ev.getAction() == MotionEvent.ACTION_DOWN) {

        long thisTime = System.currentTimeMillis();
        if (thisTime - lastTouchTime < 250) {

        	GeoPoint point = mThis.getProjection().fromPixels((int) ev.getX(),(int) ev.getY());        	
        	
        	// Double tap
        	if (mChangeListener != null) mChangeListener.onDblTap(mThis, point);

        	lastTouchTime = -1;

        } else {

          // Too slow 
          lastTouchTime = thisTime;
        }
      }

      return super.onInterceptTouchEvent(ev);
    }    
    
    // ------------------------------------------------------------------------
    // CHANGE FUNCTIONS
    // ------------------------------------------------------------------------
 
    private boolean isSpanChange()
    {
        return !mIsTouched && !getMapCenter().equals(mLastCenterPosition);
    }
 
    private boolean isZoomChange()
    {
        return (getZoomLevel() != mLastZoomLevel);
    }
}
