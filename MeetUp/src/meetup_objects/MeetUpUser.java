package meetup_objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeetUpUser extends MUModel implements Serializable{

    public static class UserTableColumns {
		public static String name = "user_name";
		public static String hasApp = "has_app";
	}

    private String name;
    private String id;
    private String email;
    @SerializedName("phone_numbers") private String phoneNumbers;
    private boolean hasApp;
    private boolean isFriend;

	public MeetUpUser(String name, String phoneNumber) {
		this.name = name;
        phoneNumbers = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumber) {
        phoneNumbers = phoneNumber;
    }

	public boolean hasApp() {
		return hasApp;
	}

	public void setHasApp(boolean hasApp) {
		this.hasApp = hasApp;
	}

    public boolean isFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }
    @Override
    public String uniqueKey() {
        return id;
    }

}