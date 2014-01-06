package tpo.solko.obligation;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ObligationAdapter extends ArrayAdapter<Obligation> {

	String token;
	String URL;
	String get_subjects;
	String enroll_me;
	int grade_id;
	int current_item;
	
	Context parent_context;
	String url_enroll_me;
	
	public ObligationAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		URL = SolkoApplication.getDefaultURL();
	    token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		parent_context = context;
		url_enroll_me = "/subjects/enroll_me/";
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	    ObjectHolder holder = null;
	   
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(R.layout.obligation_row, null);
        
        holder = new ObjectHolder();
        holder.obligation = getItem(position);
        holder.name = (TextView)v.findViewById(R.id.subject_text);
        holder.type = (TextView)v.findViewById(R.id.obligation_type);
        holder.grade_me = (ImageButton) v.findViewById(R.id.oceni_obveznost);
        holder.grade_me.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//oceni_me(v);
				
			}
		});
        holder.grade_me.setTag(holder.obligation);
        
        v.setTag(holder);
        setupItem(holder);
        return v;
	}
	


	private void setupItem(ObjectHolder holder) {
		holder.name.setText(holder.obligation.toString());
		holder.type.setText(holder.obligation.type);
	}

	public static class ObjectHolder {
		Obligation obligation;
		TextView name;
		TextView type;
		ImageButton grade_me;
		
	}
	

	public void oceni_me(View v) {
		Obligation item = (Obligation)v.getTag();
		OceniMe emt = new OceniMe(item.id, item);
		emt.execute();
	}
	
	public class OceniMe extends AsyncTask<Void, Void, JSONArray> {

	JSONArray json;
	int obligation_id;
	Obligation o;
	
	
	public OceniMe(int obligation_id, Obligation score)
	{
		this.obligation_id = obligation_id;
		this.o = score;
	}
	
	@Override
	protected void onPreExecute(){
		
	}
	
	@Override
	protected JSONArray doInBackground(Void... params) {

		RestJsonClient sendget = new RestJsonClient();
		
		json = null;
		
		
		HttpResponse req = sendget.getResponseWithToken(URL + String.valueOf(grade_id) + url_enroll_me + String.valueOf(obligation_id), token);//, token, jsonData)();
		
		if (req != null && req.getStatusLine().getStatusCode() == 200){
			json = sendget.GetArrayFromResponse(req);
		}
	
		return json;
		
	}

	@Override
	protected void onPostExecute(JSONArray json2) {
		
		int i = getPosition(o);
		//getItem(i).enrolled = true;//.getItem(s).enrolled = true;
		notifyDataSetChanged();
	}
	

}
	
}