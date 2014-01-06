package tpo.solko.obligation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EditObligationActivity extends ActionBarActivity {

	//ObligationAdapter adapter_obligations;
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
	
	TextView obligation_date;
	TextView description;
	SimpleDateFormat sdt;
	Context context;
	ArrayAdapter<String> subject_type;
	Spinner type_spinner;
	CheckBox personal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_obligation);	
		
		//adapter_obligations = new ObligationAdapter(this, android.R.layout.simple_list_item_1);
		adapter_subjects = new SubjectAdapter(this, android.R.layout.simple_spinner_item);
		context = this;
		date = new GregorianCalendar();
		
		predm = (Spinner) findViewById(R.id.predmetovje);
		predm.setAdapter(adapter_subjects);
		
		obligation_name_et = (EditText) findViewById(R.id.obligation_name_et);
		
		String s = getIntent().getStringExtra("obligation");
		
		obligation_name_et = (EditText) findViewById(R.id.obligation_name_et);
		obligation_date = (TextView) findViewById(R.id.obligation_date);
		
		type_spinner = (Spinner) findViewById(R.id.spinner1);
		subject_type = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
		 
		for (String st: getResources().getStringArray(R.array.Types))
        {
			subject_type.add(st);
        }
        
		description = (TextView) findViewById(R.id.editText1);
		
		type_spinner.setAdapter(subject_type);
		
		sdt = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		
		obligation_date.setText(sdt.format(date.getTime()).toString());
		
		obligation_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnDateSetListener callBack = new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						date = new GregorianCalendar(year, monthOfYear, dayOfMonth, date.get(Calendar.HOUR), date.get(Calendar.MINUTE));
						obligation_date.setText(sdt.format(date.getTime()).toString());
						
					}
					
				};
				DatePickerDialog dpd = new DatePickerDialog(context, callBack , date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
				dpd.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						obligation_date.setText(sdt.format(date.getTime()).toString());
						
						
					}
				});
				
				dpd.show();
				
			}
		});
		
		
		
		
		URL = SolkoApplication.getDefaultURL();
		url_get_subjects = "/subjects/get_subjects";
		url_save_obligation = "/obligations/save_obligation/";
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		getAllSubjectsTask t = new getAllSubjectsTask();
		t.execute();
		
		personal = (CheckBox)findViewById(R.id.checkBox1);
		
		if (s != null && s.length()>0)
		{
			JSONObject jo;
			try {
				jo = new JSONObject(s);
				current_obligation = new Obligation(jo);
				
				init_strings();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			current_obligation = new Obligation(-1);
		}
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
					init_strings();
				}
			} else {
				
			}
		}
		
	
	}
	

    	
    
	public void init_strings()
	{
		if (current_obligation != null && current_obligation.id != -1)
		{
			description.setText(current_obligation.description);
			obligation_name_et.setText(current_obligation.name);
			date = current_obligation.date;
			obligation_date.setText(sdt.format(date.getTime()).toString());
			for (int i = 0; i < subject_type.getCount(); i++)
			{
				if (current_obligation.type != null && current_obligation.type.equalsIgnoreCase(subject_type.getItem(i)))
				{
					type_spinner.setSelection(i);
				}
			}
			for (int i = 0; i < adapter_subjects.getCount(); i++)
			{
				if (current_obligation.subject_id == adapter_subjects.getItem(i).id)
				{
					predm.setSelection(i);
				}
			}
			
			if (current_obligation.personal)
				personal.setChecked(true);
		}
	}
	public void attemptSave()
	{
		if (obligation_name_et.getText().toString().length()<1)
			return;
		
		current_obligation.description = description.getText().toString();
		current_obligation.name = obligation_name_et.getText().toString();
		current_obligation.date = date;
		current_obligation.type = subject_type.getItem(type_spinner.getSelectedItemPosition());
		int current_subject_id = predm.getSelectedItemPosition();
		current_subject_id = adapter_subjects.getItem(current_subject_id).id;
		current_obligation.subject_id = current_subject_id;
		current_obligation.personal = personal.isChecked();
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
		
		//getSupportActionBar().setTitle(current_obligation.toString());
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	
	}

}
