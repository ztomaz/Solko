package tpo.solko.main;

import tpo.solko.R;
import tpo.solko.obligation.EditObligationActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainFragmentActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_fragment);
		Button b = (Button)findViewById(R.id.subjects_button);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
				startActivity(intent);
				
			}
		});
		findViewById(R.id.obveznost_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), HistoryFragmentActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
