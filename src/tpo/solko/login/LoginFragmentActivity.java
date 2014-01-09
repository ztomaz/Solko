package tpo.solko.login;


import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;
import org.holoeverywhere.widget.FrameLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpo.solko.SolkoApplication;

import tpo.solko.Interfaces.OnLoginInterface;
import tpo.solko.Interfaces.onFragmentChangeInterface;
import tpo.solko.main.MainFragmentActivity;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;
import tpo.solko.R;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
//import tpo.solko.TasksByDay;
//import tpo.solko.group.Group;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class LoginFragmentActivity extends Activity implements OnLoginInterface, onFragmentChangeInterface{
	private UserLoginTask mAuthTask = null;
	
	private String mEmail;
	private String mPassword;

	Context context;
	FrameLayout fragment_content;
	ArrayList<Fragment> fragments;

	String URL = SolkoApplication.getDefaultURL();
	String url_auth = URL + "login/api-token-auth/";
	String token;
	Fragment fragment;
	private boolean automaticLogin;
	
	int group_id;
	int user_id;
	
	Boolean rememberU = false;
	SharedPreferences introShared;
	/*
	 * TABS
	 */

	FragmentManager myFragmentManager;
	
	final static String TAG_1 = "FRAGMENT_1";
	final static String TAG_2 = "FRAGMENT_2";
	final static String TAG_3 = "FRAGMENT_3";
	

	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private TextView company;
    
	int grade_id;
	
	@Override
	public void attemptLogin(String mEmail, String mPassword, Boolean rememberU, Boolean rememberA)
	{
		this.mEmail = mEmail;
		this.mPassword = mPassword;
		this.rememberU = rememberU;
		this.automaticLogin = rememberA;
		if (mAuthTask != null)
		{
			return;
		}
		else
		{
			
		}
		
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		
		showProgress(true);
		mAuthTask = new UserLoginTask();
		mAuthTask.execute((Void)null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    	
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);

        context = this;
         

        AccountManager am = AccountManager.get(context);
        
        introShared = this.getSharedPreferences("Login", 0);
		automaticLogin = introShared.getBoolean("automatic", false);
		

		mLoginFormView = findViewById(R.id.login_content);

        company = (TextView) findViewById(R.id.introCompanyName);

        myFragmentManager = getSupportFragmentManager();
    	mLoginFormView = findViewById(R.id.login_content);
    	mLoginStatusView = findViewById(R.id.login_status);
    	mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
        
        fragments = new ArrayList<Fragment>();
        fragments.add(new IntroFragment());
        fragments.add(new LoginFragment());
        fragments.add(new RegistrationFragment());
        if (automaticLogin){
			mEmail = introShared.getString("username", "");
			mPassword = introShared.getString("password", "");
			attemptLogin(mEmail, mPassword, true, true);
		}
        else
        {
        	if(savedInstanceState == null)
            {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.login_content, fragments.get(0), TAG_1);
                ft.commit();
            }
        }
        
        
        
		            
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	


	
	private class UserLoginTask extends AsyncTask<Void, Integer, Integer>
	{
		
		@Override
		protected Integer doInBackground(Void... params) {
			int status = 500;
			try
			{				
				RestJsonClient sendget = new RestJsonClient();
				HttpResponse loginResponse = sendget.getResponseSendingLogin(url_auth, mEmail, mPassword);
				
				if (loginResponse != null)
				{
					status = loginResponse.getStatusLine().getStatusCode();
					if (status != 200)
					{
						return status;
					}
					else
					{
						JSONObject req = sendget.GetObjectFromResponse(loginResponse);
						if (req == null)
						{
							return 500;
						}
						else
						{

							try{
								token = req.getString("token");
								grade_id = req.getInt("grade_id");
								return status;
								
							} catch (JSONException e) {
								return 500;
							}
							}
							
						}
				}
				Thread.sleep(2000);
				
			} catch (InterruptedException e){
				return 500;
			}
			return status;
		}
			
		
		@Override
        protected void onPostExecute(Integer status)  
        {  
			mAuthTask = null;
			showProgress(false);
			
        	if (token == null){
        		return;
        	}
        	else{
        		continueToLists();
        		finish();
        	}
        }  
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
	}
	
	public void continueToLists(){
		
		SharedPreferences userSettings = PreferenceManager.getDefaultSharedPreferences(context);
		Editor uEditor = userSettings.edit();
		
		uEditor.putString("token", token);
		/*
		 * SESSION INFORMATION
		 */
		uEditor.putInt("user_id", user_id);
		uEditor.putInt("grade_id", grade_id);
		
		uEditor.commit();
		introShared = this.getSharedPreferences("Login", 0);
		
		Editor loginEditor = introShared.edit();
		
		loginEditor.putString("username", mEmail);
		
		if (rememberU)
		{
			loginEditor.putBoolean("automatic", rememberU);
		}
		if (automaticLogin)
		{
			loginEditor.putString("username", mEmail);
			loginEditor.putString("password", mPassword);
			loginEditor.putBoolean("automatic", automaticLogin);
		}
		
		loginEditor.commit();
		
		Intent intent = new Intent(context, MainFragmentActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void changeFragment(int i) {
		FragmentTransaction ft;
		switch(i)
		{
			case 1:
			ft = getSupportFragmentManager().beginTransaction();
			ft.addToBackStack(TAG_1);
			ft.replace(R.id.login_content, new LoginFragment(), TAG_2).commit();
			break;
			
			case 2:
			ft = getSupportFragmentManager().beginTransaction();
			ft.addToBackStack(TAG_1);
			ft.replace(R.id.login_content, new RegistrationFragment(), TAG_3).commit();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		if (myFragmentManager.findFragmentById(R.id.login_content) instanceof IntroFragment)
		{
			this.finish();
		}
		else
		{
			ft.replace(R.id.login_content, fragments.get(0), TAG_1).commit();
		}
	}
	  
}
