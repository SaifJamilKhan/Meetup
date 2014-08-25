package network_clients;

import com.example.meetup.NetworkRequestUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
            if(object.getJSONObject("event") != null) {
                Type listType = new TypeToken<MeetUpEvent>() {
                }.getType();
                MeetUpEvent event = new Gson().fromJson(object.getJSONObject("event").toString(), listType);
                eventArray.add(event);
            } else if(object.getJSONObject("events") != null) {
                Type listType = new TypeToken<ArrayList<MeetUpEvent>>() {
                }.getType();
                ArrayList<MeetUpEvent> events = new Gson().fromJson(object.getJSONObject("events").toString(), listType);
                eventArray.addAll(events);
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

    }

    @Override
    public void requestFailed(Exception e) {

    }

}
