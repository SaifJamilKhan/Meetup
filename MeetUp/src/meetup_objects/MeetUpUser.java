package meetup_objects;

public class MeetUpUser {
	public static class UserTableColumns {
		public static String name = "user_name";
		public static String facebookID = "facebook_id";
		public static String hasApp = "has_app";
	}

	private String mName;
	private String mFacebookId;
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