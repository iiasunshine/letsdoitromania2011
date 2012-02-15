package ro.ldir.android.entities;

import java.io.Serializable;

/**
 * Custom garbage with extra fields and functionality that the backend garbage
 * @author Coralia Paunoiu
 *
 */
public class Garbage extends BaseGarbage implements Serializable{
	
	private static final long serialVersionUID = 2737049284989125474L;
	
	/**
     * Local database id
     */
    private Integer sqliteGarbageId = -1;

	public boolean isUploaded() {
		return getGarbageId() != NO_DB_ID;
	}

	public Integer getSqliteGarbageId()
	{
		return sqliteGarbageId;
	}

	public void setSqliteGarbageId(Integer sqliteGarbageId)
	{
		this.sqliteGarbageId = sqliteGarbageId;
	}
	
	
}
