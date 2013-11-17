package tpo.solko;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class SolkoApplication extends Application {
	
	/*
	 * Default settings, token session, userId, etc.
	 */
	
	public static String defaultURL = "http://192.168.1.5:8000/taskmanager";
	//public static String defaultURL = "http://blocklogic.net/taskmanager";
	
	public static String getDefaultURL(){
		return defaultURL;
	}
	
	
	public boolean statusHandler(Context context, int status){
		Toast toast;
		switch(status){
			case 200:
				return true;
			case 201:
				toast = Toast.makeText(context, "created", Toast.LENGTH_SHORT);
				toast.show();
				return true;
			case 500:
				toast = Toast.makeText(context, "Service unaveliable", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			default:
				toast = Toast.makeText(context, "wrong request", Toast.LENGTH_SHORT);
				toast.show();
				return false;
		}
	}
}

