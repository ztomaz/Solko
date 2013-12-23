package tpo.solko.login;

import tpo.solko.Interfaces.onFragmentChangeInterface;
import tpo.solko.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class IntroFragment extends Fragment {
	
	
	//private String token;
	//private int group_id;
	
	Context context;
	onFragmentChangeInterface mChangeFragment;
	
	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mChangeFragment = (onFragmentChangeInterface) activity;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_intro, container, false);
		view.findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mChangeFragment.changeFragment(2);
			}
		});
		
		view.findViewById(R.id.login_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mChangeFragment.changeFragment(1);
			}
		});
		
		return view;
	}


	
		
}
