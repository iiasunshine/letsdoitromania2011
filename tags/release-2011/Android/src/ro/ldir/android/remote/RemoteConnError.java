package ro.ldir.android.remote;

public class RemoteConnError extends Exception
{
	/**
	 * The error status code received from the backend
	 */
	private int statusCode;

	public RemoteConnError(int statusCode)
	{
		super();
		this.statusCode = statusCode;
	}

	public int getStatusCode()
	{
		return statusCode;
	}
	
	
}
