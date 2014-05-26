package meetup_objects;

public class MeetUpEvent {
	public static class EventTableColumns {
		public static String name = "event_name";
		public static String description = "event_description";
		public static String address = "event_address";
		public static String startDate = "start_date";
		public static String endDate = "end_date";
	}

	private String mName;
	private String mDescription;
	private String mAddress;
	private String mStartDate;
	private String mEndDate;

	public MeetUpEvent(String name, String description, String address,
			String startDate, String endDate) {
		setName(name);
		setDescription(description);
		setAddress(address);
		setStartDate(startDate);
		mEndDate = endDate;
	}

	public String getEndDate() {
		return mEndDate;
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

	public String getStartDate() {
		return mStartDate;
	}

	public void setStartDate(String mStartDate) {
		this.mStartDate = mStartDate;
	}

}
