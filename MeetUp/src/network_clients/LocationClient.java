package network_clients;

import android.util.Log;

import com.example.meetup.NetworkRequestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import meetup_objects.MeetUpLocation;


public class LocationClient extends MUNetworkClient implements
        NetworkRequestUtil.NetworkRequestListener {

    private final String mPath = "locations";

    @Override
    protected void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {

    }

    @Override
    public void postWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePostRequest(mPath, this, body, parameters);
    }

    @Override
    public void postToServer(JSONArray body, ArrayList<NameValuePair> parameters) {
        JSONObject locations = new JSONObject();
        try {
            locations.put("locations", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkRequestUtil.makePostRequest(mPath, this, locations, parameters);
    }
    //Network Request Listener

    @Override
    public void requestSucceededWithJSON(JSONObject object) {

        try {
            ArrayList<MeetUpLocation> locationsArray = new ArrayList<MeetUpLocation>();
            if(object.has("locations")) {
                Type listType = new TypeToken<ArrayList<MeetUpLocation>>() {
                }.getType();

                MeetUpLocation.JsonTimeDeserializer timeDeserializer = new MeetUpLocation.JsonTimeDeserializer();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, timeDeserializer).create();

                locationsArray = gson.fromJson(object.getJSONArray("locations").toString(), listType);
                for(MeetUpLocation location : locationsArray) {
                    location.setSentToServer(true);
                }
            } else {
                Log.v("meetup", "Didnt have locations in json response");
                mListener.requestFailedWithError();
            }
            if(mListener != null) {
                mListener.requestSucceededWithResponse(locationsArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestFailedWithJSON(JSONObject object) {
        mListener.requestFailedWithError();
    }

    @Override
    public void requestFailed(Exception e) {
        mListener.requestFailedWithError();
    }

}
