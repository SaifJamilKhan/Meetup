package network_clients;

import com.example.meetup.NetworkRequestUtil;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import meetup_objects.MUModel;

public abstract class MUNetworkClient implements NetworkRequestUtil.NetworkRequestListener {

    protected  MUNetworkClientListener mListener;

    public interface MUNetworkClientListener {
        public void requestSucceededWithResponse(ArrayList<? extends MUModel> responses);

        public void requestFailedWithError();
    }

    public void setListener(MUNetworkClientListener listener) {mListener = listener;}

    protected abstract void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body,  ArrayList<NameValuePair> parameters);

    protected abstract void postWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body,  ArrayList<NameValuePair> parameters);

    public void syncWithServer(ArrayList<NameValuePair> parameters) {
        this.syncRequestWithParameters(this, null, parameters);
    }

    public void syncWithServer(JSONObject body, ArrayList<NameValuePair> parameters) {
        this.syncRequestWithParameters(this, body, parameters);
    }

    public void postToServer(JSONObject body, ArrayList<NameValuePair> parameters) {
        this.postWithParameters(this, body, parameters);
    }

    public abstract void postToServer(JSONArray body, ArrayList<NameValuePair> parameters);
}
