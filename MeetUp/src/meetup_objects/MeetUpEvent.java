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
		mName = name;
		mDescription = description;
		mAddress = address;
		mStartDate = startDate;
		mEndDate = endDate;
	}

	public String getEndDate() {
		return mEndDate;
	}

}
