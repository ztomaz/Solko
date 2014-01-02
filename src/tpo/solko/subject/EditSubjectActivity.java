package tpo.solko.subject;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.R.layout;
import tpo.solko.R.menu;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class EditSubjectActivity extends ActionBarActivity {

	TimeStampAdapter time_stamp;
	
	String URL;
	String url_get_subjects;
	
	int grade_id;
	String token;
	
	Subject current_subject;
	EditText subject_name_et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_subject);	
		time_stamp = new TimeStampAdapter(this, android.R.layout.simple_list_item_1);
		ListView l = (ListView) findViewById(R.id.list_of_times);
		String s = getIntent().getStringExtra("subject");
		
		subject_name_et = (EditText) findViewById(R.id.name_of_subject);
		
		if (s != null && s.length()>0)
		{
			JSONObject jo;
			try {
				jo = new JSONObject(s);
				current_subject = new Subject(jo);
				for (TimeStamp t: current_subject.times)
				{
					time_stamp.add(t);
					time_stamp.notifyDataSetChanged();
				}
				subject_name_et.setText(current_subject.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			current_subject = new Subject(-1);
		}
		
		findViewById(R.id.add_new_time).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeStamp temp = new TimeStamp(-1);
				temp.day = "PON";
				temp.time = "00:00";
				time_stamp.add(temp);
				time_stamp.notifyDataSetChanged();
				
			}
		});
		
		URL = SolkoApplication.getDefaultURL();
		url_get_subjects = "/subjects/save_subject/";
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		
		
		l.setAdapter(time_stamp);
		setupActionBar();
	}

	
	
	public void attemptSave()
	{
		if (subject_name_et.getText().toString().length()<1)
			return;
		
		current_subject.name = subject_name_et.getText().toString();
		current_subject.times = new ArrayList<TimeStamp>();
		for (int i = 0; i<time_stamp.getCount(); i++)
		{
			current_subject.times.add(time_stamp.getItem(i));
		}
		
		SaveSubjectTask sst = new SaveSubjectTask();
		sst.execute();
		
	}
	
	public class SaveSubjectTask extends AsyncTask<Void, Void, JSONObject> {
	
		
		@Override
		protected void onPreExecute(){
			
		}
		
		@Override
		protected JSONObject doInBackground(Void... params) {
	
			RestJsonClient sendget = new RestJsonClient();
			
			JSONObject jo = current_subject.toJSON();
			JSONObject response = null;
			HttpResponse req = sendget.postTokenAndJsonObject(URL + String.valueOf(grade_id) + url_get_subjects + current_subject.id, token, jo);//, token, jsonData)();
			
			if (req != null && req.getStatusLine().getStatusCode() == 200){
				response = sendget.GetObjectFromResponse(req);
			}
		
			return response;
			
		}
	
		@Override
		protected void onPostExecute(JSONObject json2) {
			if (json2 != null)
			{
				finish_activity(json2);
			}
		}
		
	
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_subject, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	public void finish_activity(JSONObject jo)
	{
		
		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("result", jo.toString());
		 setResult(RESULT_OK, returnIntent);     
		 finish();
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_save_subject:
			attemptSave();
			//finish();
			return true;
		case R.id.action_delete_subject:
			return false;
			
		}
		return super.onOptionsItemSelected(item);
	}
    
    private void setupActionBar() {
		
		getSupportActionBar().setTitle(current_subject.toString());
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	
	}

}
