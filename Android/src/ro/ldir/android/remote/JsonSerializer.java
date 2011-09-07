package ro.ldir.android.remote;

import java.util.Date;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.User;

public class JsonSerializer
{
	//Android garbage field name <-> Backend garbage field name <-> 
	
	
	public String serialize(IBackendGarbage garbage)
	{
		JSONObject obj=new JSONObject();
		
		obj.put("bagCount", garbage.getBagCount());
		obj.put("bigComponentsDescription", garbage.getBigComponentsDescription());
		obj.put("description", garbage.getDescription());
		obj.put("details", garbage.getDetails());
		obj.put("dispersed", garbage.isDispersed());
		obj.put("garbageId", garbage.getGarbageId());
		obj.put("percentageGlass", garbage.getPercentageGlass());
		obj.put("percentageMetal", garbage.getPercentageMetal());
		obj.put("percentagePlastic", garbage.getPercentagePlastic());
		obj.put("percentageWaste", garbage.getPercentageWaste());
		obj.put("status", garbage.getStatus().getTranslation());
		obj.put("x", garbage.getLatitude());
		obj.put("y", garbage.getLongitude());
		
		return obj.toJSONString();
	}
	
	public IBackendGarbage deserialize(String jsonGarbage)
	{
		Garbage garbage = new Garbage();
		JSONParser parser = new JSONParser();
		try
		{
			Map jsonObj = (Map)parser.parse(jsonGarbage);
			garbage.setBagCount((Integer)jsonObj.get("bagCount"));
			garbage.setBigComponentsDescription((String)jsonObj.get("bigComponentsDescription"));
			// TODO -- add the rest of the fields
			
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return garbage;
	}
	
	public User deserialize(String jsonUser, User user)
	{
		JSONParser parser = new JSONParser();
		try
		{
			Map jsonObj = (Map)parser.parse(jsonUser);
			String value = (String)jsonObj.get("acceptsMoreInfo");
			user.setAcceptsMoreInfo(Boolean.parseBoolean(value));
			
			//TODO - get activities
			
			/*value = (String)jsonObj.get("birthday");
			user.setBirthday(new Date(Date.parse(value)));*/
			
			value = (String)jsonObj.get("county");
			user.setCounty(value);
			
			value = (String)jsonObj.get("email");
			user.setEmail(value);
			
			value = (String)jsonObj.get("firstName");
			user.setFirstName(value);
			
			/*value = (String)jsonObj.get("lastAccess");
			user.setLastAccess(new Date(Date.parse(value)));*/
			
			value = (String)jsonObj.get("lastName");
			user.setLastName(value);
			// TODO - managed teams
			//TODO - member of
			
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return user;
	}
}
