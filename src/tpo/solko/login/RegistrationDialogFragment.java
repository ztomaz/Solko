package tpo.solko.login;


import org.json.JSONObject;

import tpo.solko.R;
import tpo.solko.RestJsonClient;
import tpo.solko.SolkoApplication;



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationDialogFragment extends DialogFragment  {
 	
 	

	EditText emailAddresView;
	EditText passwordView;
	EditText firstNameView;
	EditText lastNameView;
	String register_email;
	String register_password;
	String register_first_name;
	String register_last_name;

	static String URL = SolkoApplication.getDefaultURL();
	
	String urlToRegister = URL + "user/register";

    
    @Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 		getDialog().setTitle("Registration form");
 		View v = inflater.inflate(R.layout.register_form, container, false);
 		
        //myDialog.setCancelable(false);
        
        Button login = (Button) v.findViewById(R.id.register_button);
        Button goBack = (Button) v.findViewById(R.id.go_back);

        emailAddresView = (EditText) v.findViewById(R.id.EditUserName);
        passwordView = (EditText) v.findViewById(R.id.EditPassword);
        firstNameView = (EditText) v.findViewById(R.id.FirstName);
        lastNameView = (EditText) v.findViewById(R.id.LastName);
        
        //myDialog.show();
        goBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RegistrationDialogFragment.this.dismiss();
			}
		});
        login.setOnClickListener(new OnClickListener()
        {

           @Override
           public void onClick(View v)
           {
                   register();
                   RegistrationDialogFragment.this.dismiss();
           }
       });
        return v;
 	}


	public boolean register(){
		register_email = emailAddresView.getText().toString();
		register_password = passwordView.getText().toString();
		register_first_name = firstNameView.getText().toString();
		register_last_name = lastNameView.getText().toString();
		
		if (register_email == null || register_password == null ||
				register_first_name==null || register_last_name == null)
			return false;
		if (android.util.Patterns.EMAIL_ADDRESS.matcher(register_email).matches()
				&& register_password.length()>=6){
			UserRegisterTask rTask = new UserRegisterTask();
			rTask.execute();
			return true;
		}
		else
			return false;
			
		
	}
 	
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		private final ProgressDialog dialog = new ProgressDialog(getActivity());
		private boolean success;
		
		@Override
		protected void onPreExecute(){
			this.dialog.setMessage("Processing..."); 
			this.dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			success = true;
			sendRegisterPost();
			return success;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				this.dialog.cancel();
			} else {
				
			}
			
		}
		

	}
	public void sendRegisterPost()  {
		
		RestJsonClient sendget = new RestJsonClient();
		//JSONObject req = sendget.sendRegister(urlToRegister, register_email, register_password, register_first_name, register_last_name);
		//req.toString();
			
	}	
	
 	
}