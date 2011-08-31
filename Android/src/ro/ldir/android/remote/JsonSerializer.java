package ro.ldir.android.remote;

import ro.ldir.android.entities.Garbage;

public class JsonSerializer
{
	//Android garbage field name <-> Backend garbage field name <-> 
	
	
	public String serialize(Garbage garbage)
	{
		StringBuffer sb = new StringBuffer();
		
		// TODO serialize garbage using json_simple
		
		return sb.toString();
	}
	
	public Garbage deserialize(String jsonGarbage)
	{
		Garbage garbage = new Garbage();
		
		// TODO - deserialize garbage using json_simple
		
		return garbage;
	}
}
