package ro.ldir.android.remote;

public class BackendFactory
{
	public static final IBackend createBackend()
	{
		return new JsonBackend();
	}
}
