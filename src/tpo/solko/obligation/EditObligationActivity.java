package tpo.solko.obligation;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.R.layout;
import tpo.solko.R.menu;
import tpo.solko.main.SubjectActivity.getAllSubjectsTask;
import tpo.solko.subject.Subject;
import tpo.solko.subject.SubjectAdapter;
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
import android.widget.Spinner;

public class EditObligationActivity extends ActionBarActivity {

	ObligationAdapter adapter_obligations;
	SubjectAdapter adapter_subjects;
	
	GregorianCalendar date;
	
	String URL;
	String url_get_subjects;
	String url_save_obligation;
	
	int grade_id;
	String token;
	
	Obligation current_obligation;
	EditText obligation_name_et;
	Spinner predm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_obligation);	
		
		adapter_obligations = new ObligationAdapter(this, android.R.layout.simple_list_item_1);
		adapter_subjects = new SubjectAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line);
		
		date = new GregorianCalendar();
		
		predm = (Spinner) findViewById(R.id.predmetovje);
		predm.setAdapter(adapter_subjects);
		
		obligation_name_et = (EditText) findViewById(R.id.obligation_name_et);
		
		String s = getIntent().getStringExtra("obligation");
		
		obligation_name_et = (EditText) findViewById(R.id.obligation_name_et);
		
		if (s != null && s.length()>0)
		{
			JSONObject jo;
			try {
				jo = new JSONObject(s);
				current_obligation = new Obligation(jo);
				
				obligation_name_et.setText(current_obligation.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			current_obligation = new Obligation(-1);
		}
		
		
		URL = SolkoApplication.getDefaultURL();
		url_get_subjects = "/subjects/get_subjects";
		url_save_obligation = "/obligations/save_obligation/";
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		getAllSubjectsTask t = new getAllSubjectsTask();
		t.execute();
		
		setupActionBar();
	}


    
	public class getAllSubjectsTask extends AsyncTask<Void, Void, JSONArray> {
	
		JSONArray json;
		
		@Override
		protected void onPreExecute(){
			
		}
		
		@Override
		protected JSONArray doInBackground(Void... params) {
	
			RestJsonClient sendget = new RestJsonClient();
			
			json = null;
			
			
			HttpResponse req = sendget.getResponseWithToken(URL + String.valueOf(grade_id) + url_get_subjects, token);//, token, jsonData)();
			
			if (req != null && req.getStatusLine().getStatusCode() == 200){
				json = sendget.GetArrayFromResponse(req);
			}
		
			return json;
			
		}
	
		@Override
		protected void onPostExecute(JSONArray json2) {
	
			if (json2!=null) {
				JSONObject jsonO;
				for (int i = 0; i<json2.length(); i++)
				{
					try {
						jsonO = json.getJSONObject(i);
						Subject cur_subejct = new Subject(jsonO);
						if (cur_subejct.enrolled)
							adapter_subjects.add(cur_subejct);
						
					}
					catch(JSONException e){
						
					}
					
					adapter_subjects.notifyDataSetChanged();
				}
			} else {
				
			}
		}
		
	
	}
	
	public void attemptSave()
	{
		if (obligation_name_et.getText().toString().length()<1)
			return;
		
		current_obligation.name = obligation_name_et.getText().toString();
		current_obligation.date = date;
		
		int current_subject_id = predm.getSelectedItemPosition();
		current_subject_id = adapter_subjects.getItem(current_subject_id).id;
		current_obligation.subject_id = current_subject_id;
		
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
			
			JSONObject jo = current_obligation.toJSON();
			JSONObject response = null;
			
			HttpResponse req = sendget.postTokenAndJsonObject(URL + String.valueOf(grade_id) + url_save_obligation + String.valueOf(current_obligation.subject_id) + "/" + current_obligation.id, token, jo);//, token, jsonData)();
			
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
		
		getSupportActionBar().setTitle(current_obligation.toString());
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	
	}

}
