package network_clients;

import com.example.meetup.NetworkRequestUtil;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendClient extends MUNetworkClient implements
        NetworkRequestUtil.NetworkRequestListener {

    private static FriendClient instance;

    private final String mPath = "friends";

    public static FriendClient getInstance() {
        if (instance == null) {
            instance = new FriendClient();
        }
        return instance;
    }

    @Override
    protected void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body, ArrayList<NameValuePair> parameters) {
        NetworkRequestUtil.makePutRequest(mPath, this, body, parameters);
    }

    //Network Request Listener

    @Override
    public void requestSucceededWithJSON(JSONObject object) {

    }

    @Override
    public void requestFailedWithJSON(JSONObject object) {

    }

    @Override
    public void requestFailed(Exception e) {

    }

}
