package tpo.solko.login;


import tpo.solko.R;
import tpo.solko.SolkoApplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginFragment extends Fragment {

	private String mUsername = "";
	//private String mPassword = "";

	private EditText mUsernameView;
	private EditText mPasswordView;

	Dialog myDialog;
	
	
	static String URL = SolkoApplication.getDefaultURL();
	String url_auth = URL + "/login/api-token-auth";	
	
	
	String urlToAdd;

	boolean automatic = false;
	boolean rememberMe = false;
	
	CheckBox automaticLogin;
	CheckBox rememberUsername;
	
	Context context;
	
	public String token = "";
	
	SolkoApplication app;
	
	OnLoginListener mLogin;
	
	public interface OnLoginListener {
		public void login(String mUsername, String mPassword, Boolean usernameChaked, Boolean automaticChacked);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mLogin = (OnLoginListener) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.activity_login, container, false);
		mUsernameView = (EditText) view.findViewById(R.id.user);
		mPasswordView = (EditText) view.findViewById(R.id.password);

		
		context = getActivity();
		/*
		 * Shared Preferences for Login
		 */
		
		//SharedPreferences userC = this.getSharedPreferences("Login", 0);
		
		//app = (JobTasksApplication)getApplicationContext();
		SharedPreferences loginSharedPreferences = getActivity().getSharedPreferences("Login", 0);
		
		mUsername=loginSharedPreferences.getString("username", "");
		//mPassword=loginSharedPreferences.getString("password", "");
		
		rememberMe = loginSharedPreferences.getBoolean("rememberMe", false);
		
		rememberUsername = (CheckBox)view.findViewById(R.id.rememberMe);
		automaticLogin = (CheckBox)view.findViewById(R.id.automaticLogin);
		
		
		if (rememberMe){
			mUsernameView.setText(mUsername);
		}
		
		
		view.findViewById(R.id.register).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RegistrationDialogFragment rdf = new RegistrationDialogFragment();
				rdf.show(getFragmentManager(), "bla");
			}
		});
		
		view.findViewById(R.id.sign_in_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mLogin.login(mUsernameView.getText().toString(), mPasswordView.getText().toString(),
								rememberUsername.isChecked(), automaticLogin.isChecked());
					}
				});
		
		return view;
	}
	

	
}