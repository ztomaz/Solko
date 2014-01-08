package tpo.solko.main;

import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;
import org.holoeverywhere.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.obligation.EditObligationActivity;
import tpo.solko.obligation.Obligation;
import tpo.solko.obligation.ObligationAdapter;
import tpo.solko.obligation.QuickObligationAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainFragmentActivity extends Activity {


	public QuickObligationAdapter cta;

	String token;
	String URL;
	String url_get_quick_obligations;
	int grade_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cta = new QuickObligationAdapter(this, android.R.layout.simple_list_item_checked);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		URL = SolkoApplication.getDefaultURL();
    	url_get_quick_obligations = "/obligations/get_quick_obligations/";
    	
		
		setContentView(R.layout.main_fragment);
		ListView obligations = (ListView)findViewById(R.id.quick_obligations);
		obligations.setAdapter(cta);
		
		Button b = (Button)findViewById(R.id.subjects_button);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
				startActivity(intent);
				
			}
		});
		findViewById(R.id.obveznost_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), HistoryFragmentActivity.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.statistics_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
				startActivity(intent);
				
			}
		});
		
		setTitle("ŠOLKO");
		

		GetObligationByDay gobd = new GetObligationByDay();
		gobd.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences introShared;
		switch (item.getItemId()) {
		case R.id.logout:

	        introShared = this.getSharedPreferences("Login", 0);
			Editor ed = introShared.edit();
					ed.putBoolean("automatic", false);
					ed.commit();
			
			this.finish();
			return true;
		
			
		
		}return super.onOptionsItemSelected(item);
	}
	

	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first

		GetObligationByDay gobd = new GetObligationByDay();
		gobd.execute();
	    
	}
	private class GetObligationByDay extends AsyncTask<Void, Integer, JSONArray>
	{
		
		
		@Override
		protected void onPreExecute()
		{
		}
		@Override
		protected JSONArray doInBackground(Void... params) {
			RestJsonClient rjc = new RestJsonClient();
    		JSONObject jsonValues = new JSONObject();
    		try {
    			Obligation o = new Obligation(-1);
    			jsonValues.put("android_date_time", o.date_to_jsonArray(new GregorianCalendar(), false));
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		HttpResponse hr = rjc.postTokenAndJsonObject(URL + String.valueOf(grade_id) + url_get_quick_obligations, token, jsonValues);
    		JSONArray js = rjc.GetArrayFromResponse(hr);
    		return js;
			
		}
		@Override  
        protected void onProgressUpdate(Integer... values)  
        {   
			
        }  
        @Override  
        protected void onPostExecute(JSONArray result)  
        {   
			cta.clear();
    		
			try {
        		
				JSONObject current_object;
				Obligation o;
				for (int i = 0; i < result.length(); i++)
				{
					current_object = result.getJSONObject(i);
					o = new Obligation(current_object);
					cta.add(o);
				}
				
       
				cta.notifyDataSetChanged();
        		
        		
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	
        }  
        
	}


}
