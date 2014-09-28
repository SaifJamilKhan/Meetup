package meetup_objects;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import providers.LocationProvider;

public class MeetUpLocation extends MUModel implements Serializable{

    @SerializedName("id")private String mID = null;
    @SerializedName("user_id")private Number mUserID;
    @SerializedName("latitude")private Double mLatitude;
    @SerializedName("longitude")private Double mLongitude;
    @SerializedName("recorded_at")private Date mRecordedAt;
    private Boolean sentToServer;

    public MeetUpLocation (Double latitude, Double longitude, Number userID, Date recordedAt){
        mLatitude = latitude;
        mLongitude = longitude;
        mUserID = userID;
        mRecordedAt = recordedAt;
    }

    public static ArrayList<MeetUpLocation> getAllLocations(Cursor cursor) {
        ArrayList<MeetUpLocation> locations = new ArrayList<MeetUpLocation>();
        while(cursor.moveToNext()) {
            MeetUpLocation location = new MeetUpLocation(cursor);
            locations.add(location);
        }
        return locations;
    }

    public MeetUpLocation (Cursor cursor) {
        mLatitude = cursor.getDouble(cursor.getColumnIndex(LocationProvider.Columns.LATITUDE));
        mLongitude = cursor.getDouble(cursor.getColumnIndex(LocationProvider.Columns.LONGITUDE));
        sentToServer = cursor.getInt(cursor.getColumnIndex(LocationProvider.Columns.SENT_TO_SERVER)) == 1;
        mUserID = cursor.getInt(cursor.getColumnIndex(LocationProvider.Columns._USER_ID));
        String dateTime = cursor.getString(cursor.getColumnIndex(LocationProvider.Columns.RECORDED_AT));
        try {
            mRecordedAt = dateFromString(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Date dateFromString(String string) throws ParseException {
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = iso8601Format.parse(string);
        return date;
    }
    @Override
    public String uniqueKey() {
        return mID;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public Number getUserID() {
        return mUserID;
    }

    public void setUserID(Number mUserID) {
        this.mUserID = mUserID;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Date getRecordedAt() {
        return mRecordedAt;
    }

    public void setRecordedAt(Date mRecordedAt) {
        this.mRecordedAt = mRecordedAt;
    }

    public Boolean getSentToServer() {
        return sentToServer;
    }

    public void setSentToServer(Boolean sentToServer) {
        this.sentToServer = sentToServer;
    }
}
