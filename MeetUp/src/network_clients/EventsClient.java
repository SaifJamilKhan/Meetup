package network_clients;

import android.util.Log;

import com.example.meetup.NetworkRequestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import meetup_objects.MUModel;
import meetup_objects.MeetUpEvent;
import meetup_objects.MeetUpUser;


public class EventsClient extends MUNetworkClient implements
        NetworkRequestUtil.NetworkRequestListener {

    private final String mPath = "events";

    @Override
    protected void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makeGetRequest(mPath, this, parameters);
    }

    @Override
    public void postWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePostRequest(mPath, this, body, parameters);
    }
    //Network Request Listener

    @Override
    public void requestSucceededWithJSON(JSONObject object) {

        try {
            ArrayList<MUModel> eventArray = new ArrayList<MUModel>();
            if(object.has("event")) {
                Type listType = new TypeToken<MeetUpEvent>() {
                }.getType();

                MeetUpEvent.JsonTimeDeserializer timeDeserializer = new MeetUpEvent.JsonTimeDeserializer();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, timeDeserializer).create();

                MeetUpEvent event = gson.fromJson(object.getJSONObject("event").toString(), listType);
                eventArray.add(event);
            } else if(object.has("events")) {
                Type listType = new TypeToken<ArrayList<MeetUpEvent>>() {
                }.getType();
                MeetUpEvent.JsonTimeDeserializer timeDeserializer = new MeetUpEvent.JsonTimeDeserializer();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, timeDeserializer).create();

                eventArray = gson.fromJson(object.getJSONArray("events").toString(), listType);
            } else {
                Log.v("meetup", "Didnt have event or events in json response");
                mListener.requestFailedWithError();
            }
            if(mListener != null) {
                mListener.requestSucceededWithResponse(eventArray);
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
