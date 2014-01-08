package tpo.solko.main;

import java.util.zip.Inflater;

import org.apache.http.HttpResponse;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.subject.EditSubjectActivity;
import tpo.solko.subject.Subject;
import tpo.solko.obligation.StatisticsAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class StatisticsActivity extends Activity {

	ListView listView;
	StatisticsAdapter adapter;
	
	String URL;
	String url_get_subjects;
	
	int grade_id;
	String token;
	String url_enroll_me;
	Activity self;
	Subject subject_to_remove;
	
	private ListView mListView;
	
	protected ListView getListView() {
	    if (mListView == null) {
	        mListView = (ListView) findViewById(android.R.id.list);
	    }
	    return mListView;
	}
	
	protected void setListAdapter(ListAdapter adapter) {
	    getListView().setAdapter(adapter);
	}
	
	protected ListAdapter getListAdapter() {
	    ListAdapter adapter = getListView().getAdapter();
	    if (adapter instanceof HeaderViewListAdapter) {
	        return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
	    } else {
	        return adapter;
	    }
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject);
		self = this;
		adapter = new StatisticsAdapter(getApplicationContext(), R.layout.statistics_row);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter2, View arg1, int position,
					long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(self);
				LinearLayout item = (LinearLayout) LayoutInflater.inflate(self, R.layout.statistics_dialog);
				
				TextView myMsg1 = (TextView) item.findViewById(R.id.textView1);
				TextView myMsg2 = (TextView) item.findViewById(R.id.textView2);
				myMsg1.setText("Ocene: " + adapter.getItem(position).getGrades());
				myMsg2.setText("Skupno povprečje: " + adapter.getItem(position).getAllAvg());
				builder.setView(item);
				
				AlertDialog dialog = builder.create();
				dialog.show();
				
			}
		});
		URL = SolkoApplication.getDefaultURL();
		url_get_subjects = "/subjects/get_statistics";
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		setupActionBar();
		getAllSubjectsTask t = new getAllSubjectsTask();
		t.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    return super.onCreateOptionsMenu(menu);
	}
	


    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_add_new_subject:
			Intent intent = new Intent(getApplicationContext(), EditSubjectActivity.class);
			//intent.putExtra("subject", "");
			startActivityForResult(intent, 200);
			
		}
		return super.onOptionsItemSelected(item);
	}
    
    private void setupActionBar() {
		
		getSupportActionBar().setTitle("STATESTISTIKA");
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	
	}
		

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	if (requestCode == 200) {
    		if(resultCode == RESULT_OK){
    			String s = data.getStringExtra("result");
    			try {
    				JSONObject jo = new JSONObject(s);
    				Subject current_subject = new Subject(jo);
    				adapter.add(current_subject);
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		else if (resultCode == RESULT_CANCELED) {    
    
    		}
    		else
    			Log.e("error", "fuck it");
    		return;
    	}
    	else if (requestCode == 300)
    	{
    		if(resultCode == RESULT_OK){
    			String s = data.getStringExtra("result");
    			try {
    				JSONObject jo = new JSONObject(s);
    				Subject current_subject = new Subject(jo);
    				adapter.remove(subject_to_remove);
    				adapter.add(current_subject);
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		else if (resultCode == RESULT_CANCELED) {    
    
    		}
    		else
    			Log.e("error", "fuck it");
    		return;
    	}

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
						Subject cur_subejct = new Subject(jsonO, true);
						adapter.add(cur_subejct);
						
					}
					catch(JSONException e){
						
					}
					
	
					adapter.notifyDataSetChanged();
				}
			} else {
				
			}
		}
		
	
	}
	
}
