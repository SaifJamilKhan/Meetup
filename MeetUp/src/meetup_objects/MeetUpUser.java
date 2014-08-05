package meetup_objects;

import java.util.ArrayList;

public class MeetUpUser {
    public static class UserTableColumns {
		public static String name = "user_name";
		public static String hasApp = "has_app";
	}

    private String mName;
	private ArrayList mPhoneNumbers;
	private boolean mHasApp;

	public MeetUpUser(String name, ArrayList phoneNumber) {
		mName = name;
        mPhoneNumbers = phoneNumber;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

    public ArrayList getPhoneNumber() {
        return mPhoneNumbers;
    }

    public void setPhoneNumber(ArrayList phoneNumber) {
        mPhoneNumbers = phoneNumber;
    }

	public boolean hasApp() {
		return mHasApp;
	}

	public void setHasApp(boolean hasApp) {
		this.mHasApp = hasApp;
	}
}