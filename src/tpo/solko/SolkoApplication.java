package tpo.solko;

import org.holoeverywhere.app.Application;

import android.content.Context;
import android.widget.Toast;

public class SolkoApplication extends Application {
	
	/*
	 * Default settings, token session, userId, etc.
	 */
	
	//public static String defaultURL = "http://127.0.0.1:8000/";
	//public static String defaultURL = "http://192.168.1.103:8000/";
	//public static String defaultURL = "http://192.168.200.119:8000/";
	//public static String defaultURL = "http://192.168.1.8:8000/";
	/*
	 *  10.0.2.2 <- IP FOR CALLING LOCALHOST FROM VIRTUAL MACHINE
	 */
	public static String defaultURL = "http://10.0.2.2:8000/";
	
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

