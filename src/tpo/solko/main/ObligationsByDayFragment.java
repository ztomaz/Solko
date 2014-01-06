package tpo.solko.main;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.Interfaces.onRefreshInterface;
import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.obligation.EditObligationActivity;
import tpo.solko.obligation.Obligation;
import tpo.solko.obligation.ObligationAdapter;
import tpo.solko.subject.EditSubjectActivity;
import tpo.solko.subject.Subject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class ObligationsByDayFragment extends Fragment
	{
	
	/*
	 * Current day
	 */
	private int mYear;
	private int mMonth;
	private int mDay;
	public static GregorianCalendar the_date;
	
	/*
	 * View info
	 */

	
	/*
	 * Other
	 */
	Context context;
	
	String token;
	String current_date_string;
	String url_get;
	String URL;

	SharedPreferences login_shared;
	SharedPreferences user_settings;

	
	public static SimpleDateFormat fmt;
	//GetTaskByDay repeato;
	
	boolean Synched;

	
	
	ObligationsByDayFragment thisFragment;
	public String current_day_name;
	public ObligationAdapter cta;
	
	boolean front_page;
	
	//RefreshFragment refresh_fragment;

	TextView t;
	int grade_id;
	Context parent_context;
	Button change_fragment;
	String url_get_obligations;
	
	Obligation obligation_to_remove;
	
	public ObligationsByDayFragment g(Context context, GregorianCalendar this_date) {
		the_date = this_date;
    	fmt = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    	cta = new ObligationAdapter(context,android.R.layout.simple_list_item_checked);
    	URL = SolkoApplication.getDefaultURL();
		parent_context = context;
    	url_get_obligations = "/obligations/get_obligations/";
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);

		
    	return this;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		View view = inflater.inflate(R.layout.obligation_list, container, false);
		
		ListView obligations = (ListView)view.findViewById(R.id.obligation_list_view);
		obligations.setAdapter(cta);
		SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
		t = (TextView) view.findViewById(R.id.date_name);
		t.setText(outFormat.format(the_date.getTime()).toString().toUpperCase(Locale.getDefault()));
		context = getActivity();

		GetObligationByDay gobd = new GetObligationByDay();
		gobd.execute();
		
		obligations.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				obligation_to_remove = cta.getItem(position);
				Intent intent = new Intent(parent_context, EditObligationActivity.class);
				intent.putExtra("obligation", obligation_to_remove.toJSON().toString());
				startActivityForResult(intent, 300);
				
			}
		});
		
		return view;
	}
	
	

	public void remove_and_add(Obligation o)
	{
		
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
    			jsonValues.put("android_date_time", o.date_to_jsonArray(the_date, false));
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		HttpResponse hr = rjc.postTokenAndJsonObject(URL + String.valueOf(grade_id) + url_get_obligations, token, jsonValues);
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
			Synched = true;
    		
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



