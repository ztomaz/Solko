package tpo.solko.School;

import org.json.JSONException;
import org.json.JSONObject;

public class School {
	public int id;
	String name;
	
	public School(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public School(JSONObject json) throws JSONException
	{
		if (json.has("school_name"))
		{
			this.name = json.getString("school_name");
		}
		if (json.has("school_id"))
		{
			this.id = json.getInt("school_id");
		}
	}
	
	public String toString()
	{
		return this.name;
	}
	
}
