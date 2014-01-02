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

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.obligation.Obligation;
import tpo.solko.obligation.ObligationAdapter;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class ObligationsByDayFragment extends Fragment {
	
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
	private TextView current_date;
	private TextView current_day;
	private TextView number_of_tasks;
	private TextView all_task_time;
	private Button previous_day;
	private Button next_day;

	
	/*
	 * Other
	 */
	Context parent_context;
	Context context;
	
	String token;
	String current_date_string;
	String url_get;static
	String URL;

	SharedPreferences login_shared;
	SharedPreferences user_settings;

	String mUsername;
	
	int group_id;
	
	public static SimpleDateFormat fmt;
	//GetTaskByDay repeato;
	
	boolean Synched;

	
	
	ObligationsByDayFragment thisFragment;
	public String current_day_name;
	public ObligationAdapter cta;
	
	boolean front_page;
	
	//RefreshFragment refresh_fragment;

	TextView t;
	
	Button change_fragment;
	
	public ObligationsByDayFragment g(GregorianCalendar this_date) {
		the_date = this_date;
    	fmt = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    	return this;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//cta = new ObligationAdapter(context,android.R.layout.simple_list_item_checked);
    	
		View view = inflater.inflate(R.layout.obligation_list, container, false);
		t = (TextView) view.findViewById(R.id.date_name);
		t.setText(the_date.getTime().toString());
		context = getActivity();
		return view;
	}
	
	
	private class GetObligationByDay extends AsyncTask<Void, Integer, JSONObject>
	{
		
		
		@Override
		protected void onPreExecute()
		{
		}
		@Override
		protected JSONObject doInBackground(Void... params) {
			RestJsonClient rjc = new RestJsonClient();
    		JSONObject jsonValues = new JSONObject();
    		try {
    			Obligation o = new Obligation(-1);
    			jsonValues.put("android_date_time", o.date_to_jsonArray(the_date, false));
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		HttpResponse hr = rjc.postTokenAndJsonObject(URL + "/" + String.valueOf(group_id) + url_get, token, jsonValues);
    		JSONObject js = rjc.GetObjectFromResponse(hr);
    		return js;
			
		}
		@Override  
        protected void onProgressUpdate(Integer... values)  
        {   
			
        }  
        @Override  
        protected void onPostExecute(JSONObject result)  
        {   
			cta.clear();
			Synched = true;
    		
			try {
        		if (result != null && result.has("entries"))
        		{
        			JSONArray cal_array = result.getJSONArray("entries");
        		
					JSONObject current_object;
					CalTime ct;
					for (int i = 0; i < cal_array.length(); i++)
					{
						current_object = cal_array.getJSONObject(i);
						ct = new CalTime(current_object, theDate);
						cta.add(ct);
					}
					try
					{
						detail_fragment = new DetailFragment().newInstance(cta);
						
						getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, detail_fragment).commit();
						current_fragment = detail_fragment;
					}catch(IllegalStateException e)
					{
						
					}
				}
        		else
        		{
        			
        			getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, refresh_fragment).commit();
        			current_fragment = refresh_fragment;
        		}
        		
        		set_texts();
        		
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	
        }  
        
	}
*/
	public void updateUI(GregorianCalendar day) {
		the_date = day;
		
	
	}

	public ObligationsByDayFragment newInstance(GregorianCalendar g) {
		the_date = g;
		return this;
	}
	
	
}



