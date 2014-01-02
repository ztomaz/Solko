package tpo.solko.login;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.school.School;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationFragment extends DialogFragment  {
 	
	EditText emailAddresView;
	EditText passwordView;
	EditText firstNameView;
	EditText lastNameView;
	String register_email;
	String register_password;
	String register_first_name;
	String register_last_name;

	String URL = SolkoApplication.getDefaultURL();
	
	String urlToRegister = URL + "login/register";
	String url_get_school = URL + "school/get_school";
	
	Spinner spinner_schools;
    ArrayAdapter<School> school_adapter;
	
    String grade_number;
    String grade_letter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    }
    
    @Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 		View v = inflater.inflate(R.layout.register_form, container, false);
        //myDialog.setCancelable(false);
 		Button login = (Button) v.findViewById(R.id.register_button);

        emailAddresView = (EditText) v.findViewById(R.id.EditUserName);
        passwordView = (EditText) v.findViewById(R.id.EditPassword);
        firstNameView = (EditText) v.findViewById(R.id.FirstName);
        lastNameView = (EditText) v.findViewById(R.id.LastName);
        
        spinner_schools = (Spinner) v.findViewById(R.id.spinner_schools);
        school_adapter = new ArrayAdapter<School> (getActivity(), android.R.layout.simple_spinner_item, new ArrayList<School>());
        spinner_schools.setAdapter(school_adapter);
        
        
        
        login.setOnClickListener(new OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
                   register();
                   //RegistrationDialogFragment.this.dismiss();
           }
       });
        
        getAllSchoolsTask galst = new getAllSchoolsTask();
        galst.execute();
        return v;
 	}


    public boolean register(){
		
		register_email = emailAddresView.getText().toString();
		register_password = passwordView.getText().toString();
		register_first_name = firstNameView.getText().toString();
		register_last_name = lastNameView.getText().toString();
		if (register_first_name.length()<1)
		{
			firstNameView.setError("Please write your first name");
		}
		else if (register_last_name.length()<1)
		{
			lastNameView.setError("Please write your last name");
		}
		
		else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(register_email).matches())
		{
			emailAddresView.setError("please enter valid email");
		}
		else if (register_password.length()<6)
		{
			passwordView.setError("the password needs to be at least 6 letters long");
		}
		/*else if (!register_password.equals(passwordViewRe.getText().toString()))
		{
			passwordView.setError("passwords don't match");
		}
		else if (spinner_schools.getSelectedItemPosition()<1)
		{
			//school_adapter.setError("error");
			//dataAdapter1.setError("please choose a country");
		}*/
		
		else{
			UserRegisterTask rTask = new UserRegisterTask();
			rTask.execute();
			return true;
		}
		
		return false;
			
	}
 	
public class getAllSchoolsTask extends AsyncTask<Void, Void, JSONArray> {

	JSONArray json;
	
	@Override
	protected void onPreExecute(){
		
	}
	
	@Override
	protected JSONArray doInBackground(Void... params) {

		RestJsonClient sendget = new RestJsonClient();
		
		json = null;
		
		
		HttpResponse req = sendget.getResponseWithoutToken(url_get_school, null);//, token, jsonData)();
		
		if (req != null && req.getStatusLine().getStatusCode() == 200){
			json = sendget.GetArrayFromResponse(req);
		}
	
		return json;
		
	}

	@Override
	protected void onPostExecute(JSONArray json2) {

		if (json!=null) {
			JSONObject jsonO;
			for (int i = 0; i<json.length(); i++)
			{
				try {
					jsonO = json.getJSONObject(i);
					School cur_school = new School(jsonO);
					school_adapter.add(cur_school);
					
				}
				catch(JSONException e){
					
				}
				

				school_adapter.notifyDataSetChanged();
			}
		} else {
			
		}
	}
	

}
public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

	private boolean success;
	
	@Override
	protected void onPreExecute(){
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		success = true;
		RestJsonClient sendget = new RestJsonClient();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
		    
		nameValuePairs.add(new BasicNameValuePair("password1", register_password));
		nameValuePairs.add(new BasicNameValuePair("password2", register_password));
		nameValuePairs.add(new BasicNameValuePair("first_name", register_first_name));
		nameValuePairs.add(new BasicNameValuePair("last_name", register_last_name));
		JSONObject selected_country;
		
		int selected_school = school_adapter.getItem(spinner_schools.getSelectedItemPosition()).id;
		nameValuePairs.add(new BasicNameValuePair("school_id", String.valueOf(selected_school)));
		grade_number = "1";
		grade_letter = "a";
		nameValuePairs.add(new BasicNameValuePair("email", register_email));
		nameValuePairs.add(new BasicNameValuePair("grade_number", grade_number));
		nameValuePairs.add(new BasicNameValuePair("grade_letter", grade_letter.toLowerCase()));
		
		HttpResponse req = sendget.getResponseWithoutToken(urlToRegister, nameValuePairs);
		return success;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (success) {
			
		} else {
			
		}
		
	}
	
	
	public void sendRegisterPost()  {
		
		RestJsonClient sendget = new RestJsonClient();
		//JSONObject req = sendget.(urlToRegister, register_email, register_password, register_first_name, register_last_name);
		//req.toString();
			
	}	

	
} 	
}
