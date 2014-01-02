package tpo.solko.subject;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.ArrayAdapter;

public class Subject {
	public int id;
	String name;
	ArrayList<TimeStamp> times;
	public boolean enrolled;
	
	public Subject(int id)
	{
		this.id = id;
	}
	
	public Subject(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public Subject(JSONObject json) throws JSONException
	{
		if (json.has("subject_name"))
		{
			this.name = json.getString("subject_name");
		}
		if (json.has("subject_id"))
		{
			this.id = json.getInt("subject_id");
		}
		if (json.has("enrolled"))
		{
			this.enrolled = json.getBoolean("enrolled");
		}
		this.times = new ArrayList<TimeStamp>();
		if (json.has("times"))
		{
			JSONArray time = json.getJSONArray("times");
			for (int i = 0; i<time.length(); i++)
			{
				JSONObject jo = time.getJSONObject(i);
				TimeStamp t = new TimeStamp(jo);
				this.times.add(t);
			}
		}
	}
	

	public JSONObject toJSON()
	{
		JSONObject jsonValues = new JSONObject();
		try
		{
			if (this.id != 0)
			{
				jsonValues.put("subject_id", this.id);
			}
			else
			{
				jsonValues.put("subject_id", String.valueOf(-1));
			}
			
			if (this.name != null)
			{
    			jsonValues.put("subject_name", this.name);
			}
			else
			{
				jsonValues.put("subject_name", "");
			}
			if (this.times != null)
			{
				JSONArray ja = new JSONArray();
				for (TimeStamp t: times)
				{
					ja.put(t.toJSON());
				}
				jsonValues.put("times", ja);
			}
			jsonValues.put("enrolled", enrolled);

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return jsonValues;
	}
	
	
	public String toString()
	{
		return this.name;
	}
	
}
