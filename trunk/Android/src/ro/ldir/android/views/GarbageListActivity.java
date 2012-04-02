package ro.ldir.android.views;

import java.util.ArrayList;
import java.util.List;

import ro.ldir.R;
import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.User;
import ro.ldir.android.remote.BackendFactory;
import ro.ldir.android.remote.IBackend;
import ro.ldir.android.remote.RemoteConnError;
import ro.ldir.android.sqlite.LdirDbManager;
import ro.ldir.android.util.ErrorDialogHandler;
import ro.ldir.android.util.LDIRActivity;
import ro.ldir.android.util.LDIRApplication;
import ro.ldir.android.util.LLog;
import ro.ldir.android.util.Utils;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GarbageListActivity extends LDIRActivity{
	
	private static final String SAVED_GARBAGE_LIST = "ro.ldir.android.views.saved.garbage.list";
	public static final int DLG_LOGIN_REQUIRED = 200;
	
	
	
	private ArrayList<Garbage> garbageList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garbage_list);
        
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
				handleItemClick(garbage);
			}
        	
		});
	}
	
	private void setListAdapter(ListAdapter adapter)
	{
		getListView().setAdapter(adapter);
	}
	
	private ListView getListView()
	{
		return (ListView)findViewById(R.id.list);
	}
	
	private void handleItemClick(Garbage garbage)
	{
		((LDIRApplication)getApplication()).putCachedGarbage(garbage);
		Intent intent = new Intent(getBaseContext(), AddGarbageActivity.class);
		startActivityForResult(intent, 0);
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
			upload();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
		{
			refreshListItems();
	        
	        // scroll to the item added/updated
	        long id = data.getLongExtra(AddGarbageActivity.SAVED_GARBAGE_ID, -1l);
	        getListView().setSelection((int)id);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void refreshListItems()
	{
		garbageList = load();
		GarbageListAdapter adapter = new GarbageListAdapter(this, garbageList);
        setListAdapter(adapter);
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		if (id == DLG_LOGIN_REQUIRED)
		{
			return Utils.displayDialog(this, R.string.login_required);
		}
		return super.onCreateDialog(id);
	}

	private void handleDelete(Garbage garbage)
	{
		LLog.d("delete");
		LdirDbManager dbManager = new LdirDbManager();
		Context context = GarbageListActivity.this;
		dbManager.open(context);
		boolean deleted = dbManager.delete(garbage);
		if (deleted)
		{
			Utils.displayToast(context, context.getResources().getString(R.string.details_delete_confirm, garbage.getSqliteGarbageId()));
			refreshListItems();
		}
		dbManager.close();
	}
	
	private class GarbageListAdapter extends ArrayAdapter<Garbage>
	{
		private GarbageListAdapter(Context context, List<Garbage> objects) 
		{
			super(context,  R.layout.garbage_list_cell, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Garbage garbage = getItem(position);
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate( R.layout.garbage_list_cell, null);
				convertView.setOnTouchListener(new GarbageTouchlistener(garbage));
			}
			
			((TextView)convertView.findViewById(R.id.txtLstDescription)).setText(garbage.getDescription());
			((TextView)convertView.findViewById(R.id.txtLstLatitude)).setText(String.valueOf(garbage.getLatitude()));
			((TextView)convertView.findViewById(R.id.txtLstLongitude)).setText(String.valueOf(garbage.getLongitude()));
			((TextView)convertView.findViewById(R.id.txtLstBagCount)).setText(String.valueOf(garbage.getBagCount()));
			ImageView uploaded = (ImageView)convertView.findViewById(R.id.imgUpload);
			if (garbage.isUploaded())
			{
				uploaded.setImageResource(R.drawable.uploaded);
			}
			else
			{
				uploaded.setImageResource(R.drawable.upload);
			}
			return convertView;
		}
	}
	
	
	private class GarbageTouchlistener /*extends GestureDetector.SimpleOnGestureListener*/ implements OnTouchListener, GestureDetector.OnGestureListener
	{
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
		private GestureDetector gestureDetector;
		private Garbage garbage;
		
		private GarbageTouchlistener(Garbage garbage) {
			super();
			gestureDetector = new GestureDetector(this);
			this.garbage = garbage;
			LLog.d("constr GarbageTouchlistener");
		}
		
		public boolean onTouch(View v, MotionEvent event) {
			boolean handled = gestureDetector.onTouchEvent(event);
			LLog.d("onTouch handled: " + handled);
			return handled;
		}

//		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			/*if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// fling
				delete();
				return true;
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// fling
				delete();
				return true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);*/
			LLog.d("onFling");
			handleDelete(garbage);
			return true;
		}
		
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean onSingleTapUp(MotionEvent e) {
			/*handleItemClick(garbage);
			return true;*/
			return false;
		}
	}
	
	private void upload()
	{
		if (!((LDIRApplication)getApplication()).isLoggedIn())
		{
			showDialog(DLG_LOGIN_REQUIRED);
			return;
		}
		User user = ((LDIRApplication)getApplication()).getUserDetails();
		IBackend backend = BackendFactory.createBackend();
		for (Garbage garbage: garbageList)
		{
			if (!garbage.isUploaded())
			{
				try
				{
					// upload garbage
					int remoteDbId = backend.addGarbage(user, garbage);
					LLog.d("Uploaded garbage to remoteDB. garbage id = " + remoteDbId);
					garbage.setGarbageId(remoteDbId);
					// save id to local DB
					LdirDbManager dbManager = new LdirDbManager();
					dbManager.open(this);
					int localDbId = dbManager.update(garbage);
					LLog.d("Update garbage to localDB. garbage id = " + localDbId);
					dbManager.close();
					if (localDbId == Garbage.NO_DB_ID)
					{
						Utils.displayToast(this, getResources().getString(R.string.adaugat_nok));
					}
					else
					{
						List<String> images = garbage.getPictures();
						for (String image: images)
						{
							backend.assignImageToGarbage(user, garbage.getGarbageId(), image);
							LLog.d("Uploaded image: " + image);
						}
					}
					
				} catch (RemoteConnError e)
				{
					e.printStackTrace();
					ErrorDialogHandler.showErrorDialog(GarbageListActivity.this, e.getStatusCode());
				}
			}
		}
	}
	
}
