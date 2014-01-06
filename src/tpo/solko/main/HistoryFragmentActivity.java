package tpo.solko.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


import tpo.solko.R;
import tpo.solko.SolkoApplication;
import tpo.solko.obligation.EditObligationActivity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class HistoryFragmentActivity 
				extends ActionBarActivity {
	
	public ViewPager _mViewPager;
	public String token;
	
    String id;
    int group_id;
    
    Context context;
    LinearLayout linear_drawer;
    
    boolean mReturningWithResult;
    /*
     * URLS
     */
    String URL = SolkoApplication.getDefaultURL();;
    String url_get;	
    String url_get_tasks = "/mobile/mobile-get-tasks";
	String url_get_time = "mobile/mobile-get-time";
	
	SharedPreferences user_settings;
	int current_time_id;
	String mUsername;
	SharedPreferences login_shared;
	
	GregorianCalendar theDate;
	
	/*
	 * DETAILS
	 */
	LinearLayout current_working;
	
	TextView current_working_timer;
	
	boolean customTitleSupported;
	
	/**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

	FragmentManager myFragmentManager;
	GregorianCalendar today;
	GregorianCalendar tommorow;
	TextView dateName;
	GregorianCalendar yesterday;
	int focus_page;
	ObligationsByDayFragment[] list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        today = new GregorianCalendar();
        dateName = (TextView) findViewById(R.id.date_name);
        dateName.setText(today.getTime().toString());
        context = this;
        
        findViewById(R.id.next_month).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				change_date(1);
				
			}
		});
        findViewById(R.id.prev_month).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				change_date(-1);
				
			}
		});
        

		findViewById(R.id.date_name).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnDateSetListener callBack = new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						GregorianCalendar gc = new GregorianCalendar(year, monthOfYear, dayOfMonth, today.get(Calendar.HOUR), today.get(Calendar.MINUTE));
						change_date(gc);
					}
					
				};
				DatePickerDialog dpd = new DatePickerDialog(context, callBack , today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
				dpd.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						change_date(0);//.setText(sdt.format(date.getTime()).toString());
						
						
					}
				});
				
				dpd.show();
				
			}
		});
		
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_fragment, new ObligationsByDayFragment().g(getApplicationContext(), today), "FRAGMENT_1");
        ft.commit();
        
        
        change_date(0);
    }
    
    
    public void change_date(int i)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    	today.add(Calendar.DAY_OF_YEAR, i);
        dateName.setText(sdf.format(today.getTime()).toString());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, new ObligationsByDayFragment().g(getApplicationContext(), today), "FRAGMENT_1");
        ft.commit();
        
    }
    public void change_date(GregorianCalendar i)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    	today = i;
        dateName.setText(sdf.format(today.getTime()).toString());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, new ObligationsByDayFragment().g(getApplicationContext(), today), "FRAGMENT_1");
        ft.commit();
        
    }
    public void changeTitleText()
    {
        setTitle("HISTORY");
        
    }
   
   
    private void setupActionBar() {
		
		getSupportActionBar().setTitle("HISTORY");
    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	
	}
    


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mReturningWithResult = true;
    	

    }

	@Override
	protected void onPostResume() {
	    super.onPostResume();
	    if (mReturningWithResult) {
	    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	        ft.replace(R.id.main_fragment, new ObligationsByDayFragment().g(getApplicationContext(), today), "FRAGMENT_1");
	        ft.commit();
	    }
	
	    // Reset the boolean flag back to false for next time.
	    mReturningWithResult = false;
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
	
		case R.id.action_add_new_subject:
			Intent intent = new Intent(getApplicationContext(), EditObligationActivity.class);
			startActivityForResult(intent, 200);
			return true;
		}
			
		return super.onOptionsItemSelected(item);
	}
    
}
        
	
    