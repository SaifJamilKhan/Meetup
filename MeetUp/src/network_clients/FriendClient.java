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

    private static FriendClient instance;

    private final String mPath = "friends";

    @Override
    protected void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePutRequest(mPath, this, body, parameters);
    }

    //Network Request Listener

    @Override
    public void requestSucceededWithJSON(JSONObject object) {
        Type listType = new TypeToken<ArrayList<MeetUpUser>>() {
        }.getType();

        try {
            ArrayList<MeetUpUser> listOfNonFriendUsers = new Gson().fromJson(object.getJSONArray("non_friends_with_app").toString(), listType);
            ArrayList<MeetUpUser> listOfFriendUsers = new Gson().fromJson(object.getJSONArray("friends").toString(), listType);
            ArrayList<MeetUpUser> users = new ArrayList<MeetUpUser>();
            for(MeetUpUser user : listOfNonFriendUsers) {
                user.setHasApp(true);
                user.setIsFriend(false);
            }

            for(MeetUpUser user : listOfFriendUsers) {
                user.setHasApp(true);
                user.setIsFriend(true);
            }
            users.addAll(listOfNonFriendUsers);
            users.addAll(listOfFriendUsers);
            if(mListener != null) {
                mListener.requestSucceededWithResponse(users);
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
