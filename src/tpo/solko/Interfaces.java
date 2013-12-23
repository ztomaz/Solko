package tpo.solko;

public class Interfaces
{
	public interface onGroupChangeInterface {
	    public void onGroupChange();
	    //public void cont();
	}

	public interface onRefreshInterface {
	    public void onRefresh();
	    //public void cont();
	}
	
	public interface onFragmentChangeInterface{
		public void changeFragment(int i);
	}
	public interface OnLoginInterface {
		public void attemptLogin(String mUsername, String mPassword, Boolean usernameChaked, Boolean automaticChacked);
	}
}
