package ro.ldir.android.sqlite;

import java.util.ArrayList;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.util.LLog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LdirDbManager {
	private LdirDbHelper dbHelper;
	private SQLiteDatabase database;
	
	public void open(Context context) throws SQLException {
		dbHelper = new LdirDbHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public long save(Garbage garbage)
	{
		if (garbage.getGarbageId() == -1)
		{
			return insert(garbage);
		}
		else
		{
			return update(garbage);
		}
	}
	
	public long insert(Garbage garbage) {
		try {
			ContentValues values = createContentValues(garbage);
			return database.insert(LdirDbHelper.TABLE_NAME_GARBAGE, null, values);
		} catch (SQLException sqlerror) 
		{
			LLog.d(sqlerror.getMessage());
			return -1;
		}
	}
	
	public int update(Garbage garbage) 
	{
		ContentValues values = createContentValues(garbage);
		String whereClause = LdirDbHelper.GarbageFields.garbageId.column + "="
				+ garbage.getGarbageId();
		try {
			return database.update(LdirDbHelper.TABLE_NAME_GARBAGE, values,
					whereClause, null);
		} catch (SQLException sqlerror) {
			LLog.d(sqlerror.getMessage());
			return -1;
		}
	}
	
	public boolean delete(Garbage garbage) {
		String whereClause = LdirDbHelper.GarbageFields.garbageId.column + "="
				+ garbage.getGarbageId();
		try {
			database.delete(LdirDbHelper.TABLE_NAME_GARBAGE, whereClause, null);
			return true;
		} catch (SQLException sqlerror) {
			LLog.d(sqlerror.getMessage());
			return false;
		}
	}
	
	/**
     * Get all garbages
     * @return List of Garbage
     */
	public ArrayList<Garbage> getGarbageList() {
		ArrayList<Garbage> garbageList = new ArrayList<Garbage>();

		Cursor crsr = database.rawQuery("SELECT * FROM " + LdirDbHelper.TABLE_NAME_GARBAGE, null);
		crsr.moveToFirst();

		for (int i = 0; i < crsr.getCount(); i++) {
			Garbage garbage = new Garbage();
			garbage.setGarbageId(crsr
					.getInt(LdirDbHelper.GarbageFields.garbageId.ordinal()));
			garbage.setBagCount(crsr.getInt(LdirDbHelper.GarbageFields.bagCount
					.ordinal()));
			garbage.setBigComponentsDescription(crsr
					.getString(LdirDbHelper.GarbageFields.bigComponentsDescription
							.ordinal()));
			garbage.setDescription(crsr
					.getString(LdirDbHelper.GarbageFields.description.ordinal()));
			garbage.setDetails(crsr
					.getString(LdirDbHelper.GarbageFields.details.ordinal()));
			garbage.setDispersed(crsr
					.getInt(LdirDbHelper.GarbageFields.dispersed.ordinal()) == 1);
			garbage.setPercentageGlass(crsr
					.getInt(LdirDbHelper.GarbageFields.percentageGlass
							.ordinal()));
			garbage.setPercentageMetal(crsr
					.getInt(LdirDbHelper.GarbageFields.percentageMetal
							.ordinal()));
			garbage.setPercentagePlastic(crsr
					.getInt(LdirDbHelper.GarbageFields.percentagePlastic
							.ordinal()));
			garbage.setPercentageWaste(crsr
					.getInt(LdirDbHelper.GarbageFields.percentageWaste
							.ordinal()));
			garbage.setxLatitude(crsr
					.getDouble(LdirDbHelper.GarbageFields.xLatitude.ordinal()));
			garbage.setyLongitude(crsr
					.getDouble(LdirDbHelper.GarbageFields.yLongitude.ordinal()));
			garbage.setCSVPictures(crsr.getString(LdirDbHelper.GarbageFields.pictures.ordinal()));
			garbageList.add(garbage);

			crsr.moveToNext();
		}
		crsr.close();

		return garbageList;
	}
	
	/**
     * Clear the garbage table
     * To be used only in development
     */
    public void clear() 
    {
        try 
        {
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            sqlite.delete(LdirDbHelper.TABLE_NAME_GARBAGE, "", null);
        } 
        catch (SQLException sqlerror) {
            LLog.d(sqlerror.getMessage());
        }    
    }
	
	private ContentValues createContentValues(Garbage garbage) {
		ContentValues values = new ContentValues();
		// don't write the id because it is automatically incremented
		values.put(LdirDbHelper.GarbageFields.bagCount.column, garbage.getBagCount());
		values.put(LdirDbHelper.GarbageFields.bigComponentsDescription.column, garbage.getBigComponentsDescription());
		values.put(LdirDbHelper.GarbageFields.description.column, garbage.getDescription());
		values.put(LdirDbHelper.GarbageFields.details.column, garbage.getDetails());
		values.put(LdirDbHelper.GarbageFields.dispersed.column, garbage.isDispersed() ? 1 : 0);
		values.put(LdirDbHelper.GarbageFields.percentageGlass.column, garbage.getPercentageGlass());
		values.put(LdirDbHelper.GarbageFields.percentageMetal.column, garbage.getPercentageMetal());
		values.put(LdirDbHelper.GarbageFields.percentagePlastic.column, garbage.getPercentagePlastic());
		values.put(LdirDbHelper.GarbageFields.percentageWaste.column, garbage.getPercentageWaste());
		values.put(LdirDbHelper.GarbageFields.xLatitude.column, garbage.getxLatitude());
		values.put(LdirDbHelper.GarbageFields.yLongitude.column, garbage.getyLongitude());
		values.put(LdirDbHelper.GarbageFields.pictures.column, garbage.getCSVPictures());
		return values;
	}
	
}
