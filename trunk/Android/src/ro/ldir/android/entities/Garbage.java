package ro.ldir.android.entities;

import java.io.Serializable;

public class Garbage extends BaseGarbage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2737049284989125474L;
	
	/**
	 * the default value that the field {@link Garbage#remoteDbId}} has. If the garbage is not uploaded then this is the id
	 * After the upload, the  {@link Garbage#remoteDbId}} gets a normal id value
	 */
	private static final long NO_DB_ID = -1L;
	
	/*
	 * The id of the garbage in the remote (backend) database
	 */
	private long remoteDbId = NO_DB_ID;

	public boolean isUploaded() {
		return remoteDbId != NO_DB_ID;
	}

	public void setRemoteDbId(long remoteDbId) {
		this.remoteDbId = remoteDbId;
	}

	public long getRemoteDbId() {
		return remoteDbId;
	}
	
}
