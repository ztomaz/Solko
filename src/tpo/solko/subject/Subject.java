package tpo.solko.subject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.obligation.Score;

import android.widget.ArrayAdapter;

public class Subject {
	public int id;
	String name;
	ArrayList<TimeStamp> times;
	ArrayList<Score> all_scores;
	ArrayList<Score> personal_scores;
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
	

	public Subject(JSONObject json, boolean b) throws JSONException
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
		this.all_scores = new ArrayList<Score>();
		if (json.has("all_scores"))
		{
			JSONArray time = json.getJSONArray("all_scores");
			for (int i = 0; i<time.length(); i++)
			{
				JSONObject jo = time.getJSONObject(i);
				Score s = new Score(jo);
				this.all_scores.add(s);
			}
		}
		this.personal_scores = new ArrayList<Score>();
		if (json.has("personal_scores"))
		{
			JSONArray time = json.getJSONArray("personal_scores");
			for (int i = 0; i<time.length(); i++)
			{
				JSONObject jo = time.getJSONObject(i);
				Score s = new Score(jo);
				this.personal_scores.add(s);
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
	
	public String getGrades()
	{
		if (personal_scores.size()<1)
			return "/";
		String string = "";
		for (Score s: personal_scores)
		{
			string+= (s.toString()) + ", ";
		}
		return string.substring(0,string.lastIndexOf(","));
	}
	
	public String getPersonalAvg()
	{
		String string = "";
		if (personal_scores.size()<1)
		{
			return "/";
		}
		double i = 0.0;
		for (Score s: personal_scores)
		{
			i+=s.score;
		}
		double b = personal_scores.size() * 1.0;
		DecimalFormat d = new DecimalFormat("#.##");
		return d.format(i/b);
	}
	
	public String getAllAvg()
	{
		if (all_scores.size()<1)
		{
			return "/";
		}
		double i = 0.0;
		for (Score s: all_scores)
		{
			i+=s.score;
		}
		double b = all_scores.size() * 1.0;
		DecimalFormat d = new DecimalFormat("#.##");
		return d.format(i/b);
	}
	
	
	
	public String toString()
	{
		return this.name;
	}
	
}
