package tpo.solko.obligation;

import org.apache.http.HttpResponse;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialogFragment;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.NumberPicker;
import org.holoeverywhere.widget.NumberPicker.OnValueChangeListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	Activity parent_context;
	String url_enroll_me;
	String url_save_obligation;
	
	public ObligationAdapter(Activity context, int textViewResourceId) {
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
        v = vi.inflate(R.layout.obligation_row, null);
        
        holder = new ObjectHolder();
        holder.obligation = getItem(position);
        holder.name = (TextView)v.findViewById(R.id.subject_text);
        holder.type = (TextView)v.findViewById(R.id.obligation_type);
        holder.grade_me = (ImageButton) v.findViewById(R.id.oceni_obveznost);
        holder.ll = (LinearLayout)v.findViewById(R.id.layout_id);
        holder.grade_me.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				oceni_me(v);
				
			}
		});
        holder.grade_me.setTag(holder.obligation);
        
        v.setTag(holder);
        setupItem(holder);
        return v;
	}
	


	private void setupItem(ObjectHolder holder) {
		holder.name.setText(holder.obligation.subject);
		holder.type.setText(holder.obligation.type);
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
		ImageButton grade_me;
		
	}
	public void oceni_me(View v) {
		final Obligation obl = (Obligation)v.getTag();
	    final PopupMenu popup = new PopupMenu(parent_context, v);
	    final MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.actions, popup.getMenu());

	    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	   
	            switch (item.getItemId()) {
	                case R.id.grade_and_done:
	                	obl.score.completed = 1;
	                	final Dialog d = new Dialog(parent_context);
	                	
	                    d.setTitle("NumberPicker");
	                    d.setContentView(R.layout.dialog);
	                    Button b1 = (Button) d.findViewById(R.id.button1);
	                    Button b2 = (Button) d.findViewById(R.id.button2);
	                    final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
	                    np.setMaxValue(10); // max value 100
	                    np.setMinValue(1);   // min value 0
	                    np.setWrapSelectorWheel(false);
	                    np.setOnValueChangedListener(new OnValueChangeListener() {
							
							@Override
							public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
								// TODO Auto-generated method stub
								
							}
						});
	                    b1.setOnClickListener(new OnClickListener()
	                    {
	                     @Override
	                     public void onClick(View v) {

	 	                		obl.score.score = np.getValue();
	 	                		d.dismiss();
	 	                		OceniMe o1 = new OceniMe(obl);
	 		    	            o1.execute();
	                      }    
	                     });
	                    b2.setOnClickListener(new OnClickListener()
	                    {
	                     @Override
	                     public void onClick(View v) {
	                         d.dismiss(); // dismiss the dialog
	                      }    
	                     });
	                  d.show();
	                	
	                    return true;
	                case R.id.done:
	                	obl.score.completed = 1;
	                	obl.score.score = 1;
	    	            OceniMe o2 = new OceniMe(obl);
	    	            o2.execute();
	    	            return true;
	                case R.id.failed:
	                	obl.score.completed = 2;
	                	obl.score.score = -1;
	    	            OceniMe o3 = new OceniMe(obl);
	    	            o3.execute();
	                    return true;
	                default:
	                    return false;
	            }
	        }
	    });
	    
	    
	    popup.show();
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