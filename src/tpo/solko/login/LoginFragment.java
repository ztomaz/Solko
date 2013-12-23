package tpo.solko.login;


import tpo.solko.Interfaces.OnLoginInterface;
import tpo.solko.R;
import tpo.solko.SolkoApplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginFragment extends Fragment {

	private String mEmail;
	private String mPassword = "";

	private EditText mEmailView;
	private EditText mPasswordView;

	Dialog myDialog;
	
	
	String URL = SolkoApplication.getDefaultURL();
	String url_auth = URL + "/login/api-token-auth";	
	
	
	String urlToAdd;

	boolean automatic = false;
	boolean rememberMe = false;
	
	CheckBox automaticLogin;
	CheckBox rememberUsername;
	
	Context context;
	
	public String token = "";
	
	
	OnLoginInterface mLogin;
	
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mLogin = (OnLoginInterface) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Configuration c = newConfig;
		c.setToDefaults();
		c = null;
	    super.onConfigurationChanged(newConfig);
	    // Checks whether a hardware keyboard is available
	    if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
	        //company.setVisibility(View.GONE);
	    } else if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
	        //company.setVisibility(View.VISIBLE);
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		mEmailView = (EditText) view.findViewById(R.id.user);
		mPasswordView = (EditText) view.findViewById(R.id.password);
		
		
		context = getActivity();
		
		//SharedPreferences loginSharedPreferences = getActivity().getSharedPreferences("Login", 0);
		
		mEmailView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS|
				InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		rememberUsername = (CheckBox)view.findViewById(R.id.rememberMe);
		automaticLogin = (CheckBox)view.findViewById(R.id.automaticLogin);
		
		
		if (rememberMe){
			mEmailView.setText(mEmail);
		}
		
		view.findViewById(R.id.sign_in_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						attemptLogin();
						
					}
				});
		
		return view;
	}
	
	public void attemptLogin()
	{
		mEmailView.setError(null);
		mPasswordView.setError(null);
		
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		
		if (TextUtils.isEmpty(mPassword))
		{
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		else if (mPassword.length() <  4)
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			mLogin.attemptLogin(mEmail, mPassword, rememberUsername.isChecked(), automaticLogin.isChecked());
			
		}
		/*
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches())
		{
			mEmailView.setError("Please write your email");
		}
		else
		{
			mLogin.login(mEmail, mPassword,
					rememberUsername.isChecked(), automaticLogin.isChecked());
		}*/
	}
	
}