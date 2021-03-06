package ro.ldir.android.sqlite;

import ro.ldir.android.util.LLog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LdirDbHelper extends SQLiteOpenHelper 
{
	/**
	 * Database version used during development
	 */
	private static final int DATABASE_VERSION_DEV = 2;
	
	/**
	 * Database version released to users
	 */
	private static final int DATABASE_VERSION_RELEASED = 1;
	
	/**
	 * Database version used when creating the database
	 */
	private static final int DATABASE_VERSION = DATABASE_VERSION_DEV;
	
	private static final String DATABASE_NAME = "ldir";
	protected static final String TABLE_NAME_GARBAGE = "garbage";
	
	public enum GarbageFields
	{
		sqliteGarbageId("_id", "INTEGER primary key autoincrement"),
		bagCount("bagCount", "INTEGER"),
	    bigComponentsDescription("bigComponentsDescription", "TEXT"),
	    description("description", "TEXT"),
	    details("details", "TEXT"),
	    dispersed("dispersed", "INTEGER"),
	    percentageGlass("percentageGlass", "INTEGER"),
	    percentageMetal("percentageMetal", "INTEGER"),
	    percentagePlastic("percentagePlastic", "INTEGER"),
	    percentageWaste("percentageWaste", "INTEGER"),
	    pictures("pictures", "TEXT"),
//	    private Date recordDate;
	    xLatitude("xLatitude", "REAL"),
	    yLongitude("yLongitude", "REAL"),
	    garbageId("remoteDbId", "INTEGER"),
		status("status", "TEXT");
	    
	    String column;
	    String type;
		private GarbageFields(String column, String type) {
			this.column = column;
			this.type = type;
		}
	    
	    
	};
	
	protected LdirDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(this.getCreateStatement());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LLog.d("Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GARBAGE);
		onCreate(db);

	}
	
	private String getCreateStatement()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ").append(TABLE_NAME_GARBAGE).append(" (");
		GarbageFields[] fields = GarbageFields.values();
		for (int i = 0; i < fields.length - 1; i++)
		{
			// KEY_WORD + " TEXT, "
			sb.append('\'').append(fields[i].column).append('\'').append(' ')
					.append(fields[i].type).append(", ");
		}
		sb.append(fields[fields.length - 1].column).append(' ').append(fields[fields.length - 1].type).append(");");
		
		return sb.toString();
	}

}
