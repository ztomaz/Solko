package tpo.solko.login;

import tpo.solko.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class IntroFragment extends Fragment {
	
	private ProgressBar progressDialog;
	
	
	Context context;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_intro, container, false);
		
		return view;
	}
		
}
