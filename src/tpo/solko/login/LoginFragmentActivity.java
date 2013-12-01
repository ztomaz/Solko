package tpo.solko.login;


import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.SolkoApplication;

import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.MainActivity;
import tpo.solko.R;
import tpo.solko.login.LoginFragment.OnLoginListener;
//import tpo.solko.TasksByDay;
//import tpo.solko.group.Group;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

public class LoginFragmentActivity extends FragmentActivity implements OnLoginListener{
	Context context;
	FrameLayout fragment_content;
	ArrayList<Fragment> fragments;
	//private ArrayList <Group> groups;

	static String URL = SolkoApplication.getDefaultURL();
	String url_auth = URL + "/login/api-token-auth";
	String token;
	Fragment fragment;
	private boolean automaticLogin;
	private String mUsername;
	private String mPassword;
	
	int group_id;
	
	Boolean rememberU = false;
	Boolean rememberA = false;
	
	SharedPreferences introShared;
	
	@Override
	public void login(String mUsername, String mPassword, Boolean rememberU, Boolean rememberA)
	{
		this.mUsername = mUsername;
		this.mPassword = mPassword;
		this.rememberU = rememberU;
		this.rememberA = rememberA;
		new LoginTask().execute();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        context = this;
         
        fragment_content = (FrameLayout) findViewById(R.id.intro_frame);
       	
        introShared = this.getSharedPreferences("Login", 0);
		automaticLogin = introShared.getBoolean("automatic", false);
		
		//groups = new ArrayList<Group>();
		
		if (automaticLogin){
			mUsername = introShared.getString("username", "");
			mPassword = introShared.getString("password", "");
		}
		
        fragments = new ArrayList<Fragment>();
        fragments.add(new IntroFragment());
        fragments.add(new LoginFragment());
		selectItem(0);
		
		new LoginTask().execute();
		            
	}
	
	

    private void selectItem(int position) {
		if (fragment == null || !fragment.equals(fragments.get(position)))
    	{
			fragment = fragments.get(position);
	    	
	        FragmentManager fragmentManager = getSupportFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.intro_frame, fragment).commit();
    	}
    }

	private class LoginTask extends AsyncTask<Void, Integer, Integer>
	{
		
		
		@Override
		protected void onPreExecute()
		{
			//progressDialog.setMax(100);
			//progressDialog.setProgress(0);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			
			RestJsonClient sendget = new RestJsonClient();
			HttpResponse loginResponse = sendget.getResponseSendingLogin(url_auth, mUsername, mPassword);
			int status = 500;
			if (loginResponse != null){
			status = loginResponse.getStatusLine().getStatusCode();
				if (status==200){
					JSONObject req = sendget.GetObjectFromResponse(loginResponse);
					if (req!=null)
					{
						try{
							JSONObject user = req.getJSONObject("user");
							
							token = req.getString("token");
							group_id = user.getInt("group_id");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//sendget.getToken(req);
					}
					
				}
			}
			return status;
			
		}
		
		@Override
        protected void onPostExecute(Integer a)  
        {  
        	if (token == null ){
        		selectItem(1);
        	}
        	else{
        		continueToLists();
        		finish();
        	}
        	
        	
        }  
		
	}
	
	public void continueToLists(){
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences groupSettings = getApplicationContext().getSharedPreferences("current_group_task", 0);
		
		
		SharedPreferences.Editor editor = setting.edit();
		editor.putString("token", token);
		
		
		Editor loginEditor = introShared.edit();
		if (rememberU)
		{
			loginEditor.putBoolean("automatic", rememberU);
			loginEditor.putString("username", mUsername);
		}
		if (automaticLogin)
		{
			loginEditor.putString("username", mUsername);
			loginEditor.putString("password", mPassword);
			loginEditor.putBoolean("automatic", rememberA);
		}
		loginEditor.commit();
		
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);
		
	}
   
	        
}
