package tpo.solko.subject;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TimeStamp {
	public int id;
	String day;
	String time;
	
	public TimeStamp(int id)
	{
		this.id = id;
	}
	
	public TimeStamp(JSONObject json) throws JSONException
	{
		
		if (json.has("time_id"))
		{
			this.id = json.getInt("time_id");
		}
		if (json.has("time_day"))
		{
			this.day = json.getString("time_day");
		}
		if (json.has("time_tm"))
		{
			this.time = json.getString("time_tm");
		}
	}
	
	public String toString()
	{
		return this.day + " " + this.time;
	}

	public JSONObject toJSON() {
		JSONObject jsonValues = new JSONObject();
		try
		{
			if (this.id != 0)
			{
				jsonValues.put("time_id", this.id);
			}
			else
			{
				jsonValues.put("time_id", String.valueOf(-1));
			}
			
			if (this.day != null)
			{
    			jsonValues.put("time_day", this.day);
			}
			else
			{
				jsonValues.put("time_day", "");
			}
			if (this.time != null)
			{
				jsonValues.put("time_tm", this.time);
			}
			else
			{
				jsonValues.put("time_tm", "");
			}
				
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonValues;
	}
	

}
