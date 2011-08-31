package ro.ldir.android.remote;

public class ConnectionUtils
{
	public final static String _ldir_host_prod    = "app.letsdoitromania.ro";
	public final static String _ldir_host_test    = "85.204.235.19";
	public final static String _ldir_host    = _ldir_host_test;
	public final static int    _ldir_port    = 8080;
	public final static String _ldir_proto   = "http";
	public final static String _ldir_ws_auth = "LDIRBackend/";
	
	/*  Actions  */
	public static final String ACTION_SIGN_IN = "ws/user";
	public static final String ACTION_SIGN_OUT = ""; //TODO
	public static final String ACTION_ADD_GARBAGE = "ws/garbage";
	public static final String ACTION_ASSIGN_IMAGE = "ws/garbage/%d/image";
	public static final String ACTION_SET_STATUS = "ws/garbage/%d/status";
	
	/**
	 * Creates an URL like: http://app.letsdoitromania.ro:8080/LDIRBackend/<b>action</b>
	 * @param action
	 * @return 
	 */
	public static final String getWsAddress(String action, Object... params){
		return _ldir_proto + "://" + _ldir_host + ":" + _ldir_port + "/" + _ldir_ws_auth + "/" + String.format(action, params);
	}
}
