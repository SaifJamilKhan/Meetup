package meetup_objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MeetUpEvent extends MUModel{

    public static class EventTableColumns {
		public static String name = "event_name";
		public static String id = "id";
		public static String description = "event_description";
		public static String address = "event_address";
		public static String startDate = "start_date";
	}

    @SerializedName("name")private String mName;
    @SerializedName("description")private String mDescription;
    @SerializedName("address")private String mAddress;
    @SerializedName("start_time")private Date mStartTime;
    @SerializedName("id")private String mID;
    @SerializedName("participants")private ArrayList<MeetUpUser> mListOfFriends;
    @SerializedName("latitude")private Double mLatitude;
    @SerializedName("longitude")private Double mLongitude;
    private boolean showOnMap;

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        this.mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        this.mLongitude = longitude;
    }

    public ArrayList<MeetUpUser> getListOfFriends() {
        return mListOfFriends;
    }

    public void setListOfFriends (ArrayList<MeetUpUser> mListOfFriends) {
        this.mListOfFriends = mListOfFriends;
    }

    public HashMap<String, MeetUpUser> getListOfFriendHashmap() {
        HashMap<String, MeetUpUser> hashMap = new HashMap<String, MeetUpUser>();
        for(MeetUpUser user : mListOfFriends) {
            hashMap.put(user.uniqueKey(), user);
        }
        return hashMap;
    }

    public MeetUpEvent(String name, String description, String address,
                       Date startDate, ArrayList<MeetUpUser> listOfFriendIds, Double latitude, Double longitude) {
		setName(name);
		setDescription(description);
		setAddress(address);
		setStartDate(startDate);
        setListOfFriends(listOfFriendIds);
        setLatitude(latitude);
        setLongitude(longitude);
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
		return mStartTime;
	}

	public void setStartDate(Date mStartDate) {
		this.mStartTime = mStartDate;
	}

    public boolean shouldShowOnMap() {
        return showOnMap;
    }

    public void setShowOnMap(boolean showOnMap) {
        this.showOnMap = showOnMap;
    }


    public static class JsonTimeSerializer implements com.google.gson.JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime()/1000);
        }

    }

    public static class JsonTimeDeserializer implements JsonDeserializer {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsInt() * 1000);
        }
    }

}
