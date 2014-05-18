package meetup_objects;

public class MeetUpUser {
	private String mFacebookId;
	private String mName;
	private boolean mHasApp;

	public MeetUpUser(String name, String facebookId) {
		mName = name;
		mFacebookId = facebookId;
	}

	public String getFacebookId() {
		return mFacebookId;
	}

	public void setFacebookId(String facebookId) {
		this.mFacebookId = facebookId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public boolean hasApp() {
		return mHasApp;
	}

	public void setHasApp(boolean hasApp) {
		this.mHasApp = hasApp;
	}
}