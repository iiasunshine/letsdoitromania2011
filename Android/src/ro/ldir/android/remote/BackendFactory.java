package ro.ldir.android.remote;

public class BackendFactory
{
	public static final JsonBackend createBackend()
	{
		return new JsonBackend();
	}

	public static JerseyBackend createJerseyBackend() {
		return new JerseyBackend();
	}
}
