package tpo.solko.obligation;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Score {
	public int id;
	public int score;
	public int user_id;
	public int completed;
	
	public Score(int id)
	{
		this.id = id;
	}
	
	public Score(JSONObject json) throws JSONException
	{
		
		if (json.has("score_id"))
		{
			this.id = json.getInt("score_id");
		}
		if (json.has("score"))
		{
			this.score = json.getInt("score");
		}
		if (json.has("user_id"))
		{
			this.user_id = json.getInt("user_id");
		}
		if (json.has("completed"))
		{
			this.completed = json.getInt("completed");
		}
	}
	
	public String toString()
	{
		return String.valueOf(score);
	}

	public JSONObject toJSON() {
		JSONObject jsonValues = new JSONObject();
		try
		{
			if (this.id != 0)
			{
				jsonValues.put("score_id", this.id);
			}
			
    		jsonValues.put("user_id", this.user_id);
			
    		jsonValues.put("score", this.score);
    		
    		jsonValues.put("completed", completed);

			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonValues;
	}
	

}
