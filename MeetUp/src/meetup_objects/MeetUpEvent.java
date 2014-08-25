package meetup_objects;

import java.util.ArrayList;
import java.util.Date;

public class MeetUpEvent extends MUModel{

    public static class EventTableColumns {
		public static String name = "event_name";
		public static String id = "id";
		public static String description = "event_description";
		public static String address = "event_address";
		public static String startDate = "start_date";
	}

	private String mName;
	private String mDescription;
	private String mAddress;
	private Date mStartDate;
	private String mID;
    private ArrayList mListOfFriends;

    public ArrayList getListOfFriends() {
        return mListOfFriends;
    }

    public void setListOfFriends(ArrayList mListOfFriends) {
        this.mListOfFriends = mListOfFriends;
    }

	public MeetUpEvent(String name, String description, String address,
                       Date startDate, ArrayList listOfFriendIds) {
		setName(name);
		setDescription(description);
		setAddress(address);
		setStartDate(startDate);
        setListOfFriends(listOfFriendIds);
	}

    @Override
    public String uniqueKey() {
        return mID;
    }

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getID() {
		return mID;
	}

	public void setID(String id) {
		mID = id;
	}

	public Date getStartDate() {
		return mStartDate;
	}

	public void setStartDate(Date mStartDate) {
		this.mStartDate = mStartDate;
	}

}
