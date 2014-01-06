package tpo.solko.subject;

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
import android.widget.TextView;

public class SubjectAdapter extends ArrayAdapter<Subject> {

	String token;
	String URL;
	String get_subjects;
	String enroll_me;
	int grade_id;
	int current_item;
	int textViewResourceId;
	
	Context parent_context;
	String url_enroll_me;
	
	public SubjectAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	    
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		URL = SolkoApplication.getDefaultURL();
	    token = settings.getString("token", "");
		grade_id = settings.getInt("grade_id", -1);
		parent_context = context;
		this.textViewResourceId = textViewResourceId;
		url_enroll_me = "/subjects/enroll_me/";
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	    SubjectHolder holder = null;
	   
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(textViewResourceId, null);
        if (textViewResourceId == android.R.layout.simple_spinner_item)
        {
        	return super.getView(position, convertView, parent);
        }
        holder = new SubjectHolder();
        holder.subject = getItem(position);
        holder.name = (TextView)v.findViewById(R.id.subject_text);
        holder.enroll_me = (Button) v.findViewById(R.id.enroll_me);
        holder.enroll_me.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enrollMe(v);
				
			}
		});
        holder.enroll_me.setTag(holder.subject);
        
        v.setTag(holder);
        setupItem(holder);
        return v;
	}
	


	private void setupItem(SubjectHolder holder) {
		holder.name.setText(holder.subject.toString());
		if (holder.subject.enrolled)
			holder.enroll_me.setVisibility(View.INVISIBLE);
	}

	public static class SubjectHolder {
		Subject subject;
		TextView name;
		Button enroll_me;
		
	}
	

	public void enrollMe(View v) {
		Subject item = (Subject)v.getTag();
		EnrollMeTask emt = new EnrollMeTask(item.id, item);
		emt.execute();
	}
	
	public class EnrollMeTask extends AsyncTask<Void, Void, JSONArray> {

	JSONArray json;
	int subject_id;
	Subject s;
	
	public EnrollMeTask(int subject_id, Subject subject)
	{
		this.subject_id = subject_id;
		this.s = subject;
	}
	
	@Override
	protected void onPreExecute(){
		
	}
	
	@Override
	protected JSONArray doInBackground(Void... params) {

		RestJsonClient sendget = new RestJsonClient();
		
		json = null;
		
		
		HttpResponse req = sendget.getResponseWithToken(URL + String.valueOf(grade_id) + url_enroll_me + String.valueOf(subject_id), token);//, token, jsonData)();
		
		if (req != null && req.getStatusLine().getStatusCode() == 200){
			json = sendget.GetArrayFromResponse(req);
		}
	
		return json;
		
	}

	@Override
	protected void onPostExecute(JSONArray json2) {
		
		int i = getPosition(s);
		getItem(i).enrolled = true;//.getItem(s).enrolled = true;
		notifyDataSetChanged();
	}
	

}
	
}