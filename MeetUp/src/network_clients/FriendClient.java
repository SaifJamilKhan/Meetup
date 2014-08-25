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
import meetup_objects.MeetUpUser;


public class FriendClient extends MUNetworkClient implements
        NetworkRequestUtil.NetworkRequestListener {

    private final String mPath = "friends";

    @Override
    protected void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePutRequest(mPath, this, body, parameters);
    }

    @Override
    protected void postWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePostRequest(mPath, this, body, parameters);
    }

    //Network Request Listener

    @Override
    public void requestSucceededWithJSON(JSONObject object) {
        Type listType = new TypeToken<ArrayList<MeetUpUser>>() {
        }.getType();

        try {
            ArrayList<MeetUpUser> listOfFriendUsers = new Gson().fromJson(object.getJSONArray("friends").toString(), listType);

            for(MeetUpUser user : listOfFriendUsers) {
                user.setHasApp(true);
                user.setIsFriend(true);
            }

            if(mListener != null) {
                mListener.requestSucceededWithResponse(listOfFriendUsers);
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
