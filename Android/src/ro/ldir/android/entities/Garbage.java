package ro.ldir.android.entities;

import java.io.Serializable;

public class Garbage extends BaseGarbage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2737049284989125474L;
	
	private boolean uploaded;

	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}
	
	

}
