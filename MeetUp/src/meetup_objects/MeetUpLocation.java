package meetup_objects;
import java.util.Date;

public class MeetUpLocation extends MUModel {

    private String mID;
    private Number mUserID;
    private Double mLatitude;
    private Double mLongitude;
    private Date mRecordedAt;

    @Override
    public String uniqueKey() {
        return null;
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
}
