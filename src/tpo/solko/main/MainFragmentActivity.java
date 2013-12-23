package tpo.solko.main;

import tpo.solko.R;
import tpo.solko.R.layout;
import tpo.solko.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainFragmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
