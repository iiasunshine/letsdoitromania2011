package ro.letsdoitromania.android.structuri;

import org.json.*;

public class Morman{
	
	static final long serialVersionUID = 1;
	
	static final String ws_desc   = "description";
	static final String ws_status = "status";
	static final String ws_volume = "volume";
	static final String ws_longit = "x";
	static final String ws_latit  = "y";
	
	public static final String STAT_CLEAN = "CLEANED";
	public static final String STAT_ID    = "IDENTIFIED";
	
	
	long 	_mormanId;/*Tip=long. Id-ul mormanului.*/
	double 	_lat_Y;
	double	_long_X;
	int 	_pnerecic;/*Tip=int. Estimare procent de nereciclabile.*/
	int 	_psticla;
	int 	_pmetal;
	int 	_pplastic;
	int 	_volume;  /*saci de 100 de litrii*/
	
	
	/**
	 * @return the _mormanId
	 */
	public final long get_mormanId() {
		return _mormanId;
	}

	/**
	 * @param _mormanId the _mormanId to set
	 */
	public void set_mormanId(long _mormanId) {
		this._mormanId = _mormanId;
	}

	/**
	 * @return the _lat_Y
	 */
	public final double get_lat_Y() {
		return _lat_Y;
	}

	/**
	 * @param _lat_Y the _lat_Y to set
	 */
	public void set_lat_Y(double _lat_Y) {
		this._lat_Y = _lat_Y;
	}

	/**
	 * @return the _long_X
	 */
	public final double get_long_X() {
		return _long_X;
	}

	/**
	 * @param _long_X the _long_X to set
	 */
	public void set_long_X(double _long_X) {
		this._long_X = _long_X;
	}

	/**
	 * @return the _pnerecic
	 */
	public final int get_pnerecic() {
		return _pnerecic;
	}

	/**
	 * @param _pnerecic the _pnerecic to set
	 */
	public void set_pnerecic(int _pnerecic) {
		this._pnerecic = _pnerecic;
	}

	/**
	 * @return the _psticla
	 */
	public final int get_psticla() {
		return _psticla;
	}

	/**
	 * @param _psticla the _psticla to set
	 */
	public void set_psticla(int _psticla) {
		this._psticla = _psticla;
	}

	/**
	 * @return the _pmetal
	 */
	public final int get_pmetal() {
		return _pmetal;
	}

	/**
	 * @param _pmetal the _pmetal to set
	 */
	public void set_pmetal(int _pmetal) {
		this._pmetal = _pmetal;
	}

	/**
	 * @return the _pplastic
	 */
	public final int get_pplastic() {
		return _pplastic;
	}

	/**
	 * @param _pplastic the _pplastic to set
	 */
	public void set_pplastic(int _pplastic) {
		this._pplastic = _pplastic;
	}

	/**
	 * @return the _volume
	 */
	public final int get_volume() {
		return _volume;
	}

	/**
	 * @param _volume the _volume to set
	 */
	public void set_volume(int _volume) {
		this._volume = _volume;
	}

	/**
	 * @return the _dispersat
	 */
	public final boolean is_dispersat() {
		return _dispersat;
	}

	/**
	 * @param _dispersat the _dispersat to set
	 */
	public void set_dispersat(boolean _dispersat) {
		this._dispersat = _dispersat;
	}

	/**
	 * @return the _descVoluminoase
	 */
	public final String get_descVoluminoase() {
		return _descVoluminoase;
	}

	/**
	 * @param _descVoluminoase the _descVoluminoase to set
	 */
	public void set_descVoluminoase(String _descVoluminoase) {
		this._descVoluminoase = _descVoluminoase;
	}

	/**
	 * @return the _status
	 */
	public final String get_status() {
		return _status;
	}

	/**
	 * @param _status the _status to set
	 */
	public void set_status(String _status) {
		this._status = _status;
	}

	/**
	 * @return the _desc
	 */
	public final String get_desc() {
		return _desc;
	}

	/**
	 * @param _desc the _desc to set
	 */
	public void set_desc(String _desc) {
		this._desc = _desc;
	}
	
	boolean _dispersat;
	String 	_descVoluminoase;
	String 	_status; /*CLEANED or IDENTIFIED*/
	String 	_desc;
	
	public Morman(){
		_desc   = "";
		_status = "uninitialized";
		_descVoluminoase = "";
		_volume   = 0;
		_pplastic = 0;
		_psticla  = 0;
		_pmetal   = 0;
		_pnerecic = 0;
		_lat_Y    = 0;
		_long_X   = 0;
		_mormanId = 0;
	}
	
	public Morman(String json) throws Exception{
		this();
		if (json == "")
			throw new Exception();
		
		JSONObject jcontent = new JSONObject(json);
			
	    _desc   = jcontent.getString(ws_desc);
	    _status = jcontent.getString(ws_status);
	    _volume = jcontent.getInt(ws_volume);
		_long_X = jcontent.getDouble(ws_longit);
		_lat_Y  = jcontent.getDouble(ws_latit);
		
	}
	
	public String toJson(){
		JSONObject json = new JSONObject();
		try{
			json.put(ws_desc, _desc);
			json.put(ws_status, _status);
			json.put(ws_volume, _volume);
			json.put(ws_longit, _long_X);
			json.put(ws_latit, _lat_Y);
		}catch(JSONException ex){
			
		}
		
		return json.toString();
	}
	
}
