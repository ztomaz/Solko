package tpo.solko.obligation;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Obligation {
	public int id;
	
	String name;
	Score score;
	String type;
	Date dt;
	boolean personal;
	boolean is_score_obligated;
	
	//ArrayList<Score> scores;

	int subject_id;
	
	GregorianCalendar date;
	
	String description;
	
	public Obligation(int id)
	{
		this.id = id;
	}
	
	public Obligation(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public Obligation(JSONObject json) throws JSONException
	{
		if (json.has("obligation_name"))
		{
			this.name = json.getString("obligation_name");
		}
		if (json.has("obligation_id"))
		{
			this.id = json.getInt("obligation_id");
		}
		if (json.has("description"))
		{
			this.description = json.getString("description");
		}
		/*this.scores = new ArrayList<Score>();
		if (json.has("scores"))
		{
			JSONArray time = json.getJSONArray("scores");
			for (int i = 0; i<time.length(); i++)
			{
				JSONObject jo = time.getJSONObject(i);
				Score t = new Score(jo);
				this.scores.add(t);
			}
		}*/
		
		if (json.has("personal"))
		{
			this.personal = json.getBoolean("personal");
		}
		if (json.has("subject_id"))
		{
			this.subject_id = json.getInt("subject_id");
		}
		if (json.has("is_score_necessary"))
		{
			this.is_score_obligated = json.getBoolean("is_score_necessary");
		}
		if (json.has("date"))
		{
			JSONArray json_start_date = json.getJSONArray("date");
			int year = (Integer) json_start_date.get(0);
			int month = (Integer) json_start_date.get(1)-1;
			int day = (Integer) json_start_date.get(2);
			this.date = new GregorianCalendar(year, month, day, 0, 0);
		}		
		if (json.has("type"))
		{
			this.type = json.getString("type");
		}
		if (json.has("description"))
		{
			this.description = json.getString("description");
		}
		if (json.has("score"))
		{
			JSONObject sub_json = json.getJSONObject("score");
			this.score = new Score(sub_json);
		}
		
	}
	

	public JSONObject toJSON()
	{
		JSONObject jsonValues = new JSONObject();
		try
		{
			if (this.id != 0)
			{
				jsonValues.put("obligation_id", this.id);
			}
			else
			{
				jsonValues.put("obligation_id", String.valueOf(-1));
			}
			
			if (this.name != null)
			{
    			jsonValues.put("obligation_name", this.name);
			}
			else
			{
				jsonValues.put("obligation_name", "");
			}
			/*
			if (this.scores != null)
			{
				JSONArray ja = new JSONArray();
				for (Score s: scores)
				{
					ja.put(s.toJSON());
				}
				jsonValues.put("scores", ja);
			}*/
			jsonValues.put("subject_id", subject_id);
			jsonValues.put("personal", personal);
			jsonValues.put("is_score_necessary", is_score_obligated);
			if (type == null)
				jsonValues.put("type", "Drugo");
			else
				jsonValues.put("type", type);
			if (description != null)
				jsonValues.put("description", description);
			if (this.date != null)
			{
    			jsonValues.put("date", date_to_jsonArray(date, false));
			}
			if (this.description != null)
			{
				jsonValues.put("description", this.description);
			}
			else
			{
				jsonValues.put("description", "");
			}
			if (this.score != null)
			{
				
				//JSONObject sub_json = new JSONObject();
				jsonValues.put("score", score.toJSON());
			}
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return jsonValues;
	}
	
	public JSONArray date_to_jsonArray(GregorianCalendar gc, boolean with_time)
	{
		JSONArray ja = new JSONArray();
		ja.put(gc.get(Calendar.YEAR));
		ja.put(gc.get(Calendar.MONTH)+1);
		ja.put(gc.get(Calendar.DAY_OF_MONTH));
		return ja;
	}
	public String toString()
	{
		return this.name;
	}
	
}
