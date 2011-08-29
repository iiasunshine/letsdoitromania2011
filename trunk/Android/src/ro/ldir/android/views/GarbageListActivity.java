package ro.ldir.android.views;

import java.util.ArrayList;
import java.util.List;

import ro.ldir.R;
import ro.ldir.android.entities.Garbage;
import ro.ldir.android.sqlite.LdirDbManager;
import ro.ldir.android.util.LDIRApplication;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GarbageListActivity extends ListActivity {
	
	private static final String SAVED_GARBAGE_LIST = "ro.ldir.android.views.saved.garbage.list";
	
	private ArrayList<Garbage> garbageList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // fill in an adapter with data from the database
        if (savedInstanceState == null)
        {
        	garbageList = load();
        }
        else
        {
        	garbageList = (ArrayList<Garbage>)savedInstanceState.get(SAVED_GARBAGE_LIST);
        }
        
        GarbageListAdapter adapter = new GarbageListAdapter(this, garbageList);
        setListAdapter(adapter);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Garbage garbage = (Garbage)parent.getAdapter().getItem(position);
				((LDIRApplication)getApplication()).putCachedGarbage(garbage);
				Intent intent = new Intent(getBaseContext(), AddGarbageActivity.class);
				startActivityForResult(intent, 0);
			}
        	
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SAVED_GARBAGE_LIST, garbageList);
		super.onSaveInstanceState(outState);
	}



	private ArrayList<Garbage> load()
	{
		LdirDbManager dbManager = new LdirDbManager();
		dbManager.open(this);
		ArrayList<Garbage> garbageList = dbManager.getGarbageList();
		dbManager.close();
		return garbageList;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add button
		menu.add(0, R.string.chart_add_morman_link, 0, R.string.chart_add_morman_link);
		// upload button
		menu.add(0, R.string.chart_upload_morman_link, 0, R.string.chart_upload_morman_link);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId())
		{
		case R.string.chart_add_morman_link:
			Intent intent = new Intent(getBaseContext(), AddGarbageActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.string.chart_upload_morman_link:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
		{
			// refresh list
			garbageList = load();
			GarbageListAdapter adapter = new GarbageListAdapter(this, garbageList);
	        setListAdapter(adapter);
	        
	        // scroll to the item added/updated
	        long id = data.getLongExtra(AddGarbageActivity.SAVED_GARBAGE_ID, -1l);
	        getListView().setSelection((int)id);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private class GarbageListAdapter extends ArrayAdapter<Garbage>
	{
		private GarbageListAdapter(Context context, List<Garbage> objects) 
		{
			super(context,  R.layout.garbage_list_cell, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate( R.layout.garbage_list_cell, null);
			}
			Garbage garbage = getItem(position);
			((TextView)convertView.findViewById(R.id.txtLstDescription)).setText(garbage.getDescription());
			((TextView)convertView.findViewById(R.id.txtLstLatitude)).setText(String.valueOf(garbage.getxLatitude()));
			((TextView)convertView.findViewById(R.id.txtLstLongitude)).setText(String.valueOf(garbage.getyLongitude()));
			((TextView)convertView.findViewById(R.id.txtLstBagCount)).setText(String.valueOf(garbage.getBagCount()));
			return convertView;
		}
	}
	
	
}
