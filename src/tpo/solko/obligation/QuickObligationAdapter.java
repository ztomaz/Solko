package tpo.solko.obligation;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuickObligationAdapter extends ArrayAdapter<Obligation> {

	String token;
	String URL;
	String get_subjects;
	String enroll_me;
	int grade_id;
	int current_item;
	
	Activity parent_context;
	String url_enroll_me;
	String url_save_obligation;
	
	public QuickObligationAdapter(Activity context, int textViewResourceId) {
	    super(context, textViewResourceId);

		url_save_obligation = "/obligations/save_obligation/";
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
        v = vi.inflate(R.layout.quick_obligation_row, null);
        
        holder = new ObjectHolder();
        holder.obligation = getItem(position);
        holder.name = (TextView)v.findViewById(R.id.subject_text);
        holder.type = (TextView)v.findViewById(R.id.obligation_type);
        holder.grade_me = (TextView) v.findViewById(R.id.oceni_obveznost_datum);
        holder.ll = (LinearLayout)v.findViewById(R.id.layout_id);
       
        holder.grade_me.setTag(holder.obligation);
        
        v.setTag(holder);
        setupItem(holder);
        return v;
	}
	


	private void setupItem(ObjectHolder holder) {
		holder.name.setText(holder.obligation.subject);
		holder.type.setText(holder.obligation.type);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		holder.grade_me.setText(sdf.format(holder.obligation.date.getTime()));
		if (holder.obligation.score.completed==1)
		{
			holder.ll.setBackgroundColor(parent_context.getResources().getColor(R.color.green_not_highlighted));
		}
		
		else if (holder.obligation.score.completed==2)
			holder.ll.setBackgroundColor(parent_context.getResources().getColor(R.color.red_not_highlighted));
		else
			holder.ll.setBackgroundColor(parent_context.getResources().getColor(R.color.dark_grey));
	}

	public static class ObjectHolder {
		LinearLayout ll;
		Obligation obligation;
		TextView name;
		TextView type;
		TextView grade_me;
		
	}
	
	public class OceniMe extends AsyncTask<Void, Void, JSONObject> {

	JSONArray json;
	int obligation_id;
	Obligation o;
	
	
	public OceniMe(Obligation score)
	{
		this.o = score;
	}
	
	@Override
	protected void onPreExecute(){
		
	}
	
	@Override
	protected JSONObject doInBackground(Void... params) {

		RestJsonClient sendget = new RestJsonClient();
		
		JSONObject jo = o.toJSON();
		JSONObject response = null;
		
		HttpResponse req = sendget.postTokenAndJsonObject(URL + String.valueOf(grade_id) + url_save_obligation + String.valueOf(o.subject_id) + "/" + o.id, token, jo);//, token, jsonData)();
		
		if (req != null && req.getStatusLine().getStatusCode() == 200){
			response = sendget.GetObjectFromResponse(req);
		}
	
		return response;
		
	}

	@Override
	protected void onPostExecute(JSONObject json2) {
		if (json2 != null)
		{
			finish_activity();
		}
	}
	

}
	
	public void finish_activity()
	{
		notifyDataSetChanged();
	}
	
}