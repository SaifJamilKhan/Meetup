package network_clients;

import com.example.meetup.NetworkRequestUtil;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
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

    public void syncWithServer(JSONObject body, ArrayList<NameValuePair> parameters) {
        this.syncRequestWithParameters(this, body, parameters);
    }
}
