package ro.ldir.android.views;

import ro.ldir.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class CentralActivity extends TabActivity 
{
	private enum Tabs{
		GarbageList("list", R.string.details_lista_link, R.drawable.garbage_list, GarbageListActivity.class), // TODO - use settings icon; 
		Settings("settings", R.string.key_settings, R.drawable.icon_settings, SettingsActivity.class); // TODO - use mormane icon
		String tag;
		int messageKey;
		int drawableKey;
		Class<?> activityClass;
		private Tabs(String tag, int messageKey, int drawableKey, Class<?> activityClass) {
			this.tag = tag;
			this.messageKey = messageKey;
			this.drawableKey = drawableKey;
			this.activityClass = activityClass;
		}
		
		
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.central_tab_layout);

        TabHost tabHost = getTabHost();  // The activity TabHost
        for (Tabs tab: Tabs.values())
        {
        	tabHost.addTab(createTab(tab));
        }
        tabHost.setCurrentTab(0);
	}
	
	private TabHost.TabSpec createTab(Tabs tab)
	{
		 TabHost tabHost = getTabHost();  // The activity TabHost
		// Create an Intent to launch an Activity for the tab (to be reused)
		Intent intent = new Intent().setClass(this, tab.activityClass);

        // Initialize a TabSpec for each tab and add it to the TabHost
		TabHost.TabSpec spec = tabHost.newTabSpec(tab.tag)
		.setIndicator(getResources().getString(tab.messageKey),	getResources().getDrawable(tab.drawableKey)).setContent(intent);
        return spec;
	}
}
