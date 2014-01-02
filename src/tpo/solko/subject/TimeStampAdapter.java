package tpo.solko.subject;

import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeStampAdapter extends ArrayAdapter<TimeStamp> {

	String token;
	String URL;
	String get_subjects;
	String enroll_me;
	int grade_id;
	String url_enroll_me;
	int current_item;
	
	Context parent_context;
	
	public TimeStampAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		URL = SolkoApplication.getDefaultURL();
	    token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		url_enroll_me = "/subjects/enroll_me/";
		parent_context = context;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.time_stamp_row, null);
	
	    }
	
	    TimeStamp t = getItem(position);
	
	    if (t != null) {
	    	final Spinner simple_spinner =  (Spinner) v.findViewById(R.id.spinner1);
	    	String[] list = parent_context.getResources().getStringArray(R.array.Days);
	    	final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
	    			android.R.layout.simple_spinner_item, list);
	    	dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    	simple_spinner.setAdapter(dataAdapter);
	    	
	        TextView tt = (TextView) v.findViewById(R.id.time_info);
	        
	        String day = t.day;
	        int j = 0;
	        for (int i = 0; i < simple_spinner.getCount(); i++)
	        {
	        	if (simple_spinner.getItemAtPosition(i).equals(day))
	        	{
	        		j = i;
	        		break;
	        	}
	        }
	        
	        simple_spinner.setSelection(j);
	        
	        simple_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View arg1,
						int position2, long arg3) {
					getItem(position).day = dataAdapter.getItem(position2);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					return;
				}
			});
	        
	        tt.setText(t.time);
	        
	        tt.setOnClickListener(new OnClickListener() {
	        	String[] hour_minute = getItem(position).time.split(":");
				int current_hour = Integer.valueOf(hour_minute[0]);
				int current_minute = Integer.valueOf(hour_minute[1]);
				@Override
				public void onClick(View v) {
					TimePickerDialog newFragment = new TimePickerDialog(parent_context, new OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							getItem(position).time = (String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute));
							notifyDataSetChanged();
							
						}
					}, current_hour, current_minute, true);
					newFragment.show();
				}
				
			});
	        
	        v.findViewById(R.id.delete_me).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					remove(getItem(position));
					notifyDataSetChanged();
					
				}
			});
	        
	    }
	
	    return v;
	
	}
	
}