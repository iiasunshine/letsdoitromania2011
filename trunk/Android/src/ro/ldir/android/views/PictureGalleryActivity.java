package ro.ldir.android.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import ro.ldir.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PictureGalleryActivity extends Activity 
{
	
	public static final String PICTURE_LIST = "ro.ldir.garbage.picure.list";
	private static final int DLG_CONFIRM_DELETE = 1;
	
	private ArrayList<String> pictures;
	private int position;
	private GalleryAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.picture_gallery);
	    
	    pictures = getIntent().getStringArrayListExtra(PICTURE_LIST);

	    Gallery g = (Gallery) findViewById(R.id.gallery);
	    adapter = new GalleryAdapter(this, pictures);
	    g.setAdapter(adapter);
	    
	    setBigPicture(position);

	    g.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	PictureGalleryActivity.this.position = position;
	            setBigPicture(position);
	        }
	    });
	}
	
	private void setPicture(ImageView img, String path)
	{
		if (path == null)
		{
			img.setImageResource(R.drawable.no_pic_found);
			return;
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			Bitmap b = BitmapFactory.decodeStream(fis);
			img.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			img.setImageResource(R.drawable.no_pic_found);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setBigPicture(int position)
	{
		ImageView bigPicture = (ImageView)findViewById(R.id.crtPicture);
		String path = position >= 0 && position < pictures.size() ? pictures.get(position) : null;
		setPicture(bigPicture, path);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// delete button
		menu.add(0, R.string.gallery_delete, 0, R.string.gallery_delete);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId())
		{
		case R.string.gallery_delete:
			showDialog(DLG_CONFIRM_DELETE);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void saveChanges()
	{
		Intent result = new Intent();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(PICTURE_LIST, pictures);
		result.putExtras(bundle);
		setResult(RESULT_OK, result);
	}
	
	private void deleteSelectedPicture()
	{
		if (position >= 0 && position < pictures.size())
		{
			String path = pictures.remove(position);
			File picFile = new File(path);
			picFile.delete();
			position = Math.max(0, position - 1);
			adapter.setPictures(pictures);
			Gallery g = (Gallery) findViewById(R.id.gallery);
			g.setSelection(position);
			setBigPicture(position);
		}
	}
	
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			saveChanges();
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DLG_CONFIRM_DELETE)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.gallery_delete_confirm);
			builder.setPositiveButton(R.string.details_ok, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					deleteSelectedPicture();
				}
			});
			builder.setCancelable(true);
			builder.setNegativeButton(R.string.details_cancel, null);
			return builder.create();
		}
		return super.onCreateDialog(id);
	}



	private class GalleryAdapter extends BaseAdapter 
	{
		private Context context;
		private List<String> pictures;
		
		public GalleryAdapter(Context context, List<String> pictures) {
			super();
			this.context = context;
			this.pictures = pictures;
		}
		

		public void setPictures(List<String> pictures) {
			this.pictures = pictures;
			notifyDataSetChanged();
		}


		public int getCount() {
			return pictures.size();
		}

		public Object getItem(int position) {
			return pictures.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if (convertView == null)
			{
				ImageView i = new ImageView(context);
		        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
		        i.setScaleType(ImageView.ScaleType.FIT_XY);
//		        i.setBackgroundResource(mGalleryItemBackground);
		        convertView = i;
			}
			setPicture((ImageView)convertView, pictures.get(position));
			return convertView;
		}
		
	}

}
