package network_clients;

import com.example.meetup.NetworkRequestUtil;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class MUNetworkClient implements NetworkRequestUtil.NetworkRequestListener {

    protected  ArrayList<MUNetworkClientObserver> mObservers;

    public interface MUNetworkClientObserver {
        public void requestSucceededWithResponse(ArrayList responses);

        public void requestFailedWithError();
    }

    public void addObserver(MUNetworkClientObserver observer) {mObservers.add(observer);}

    protected abstract void syncRequestWithParameters(NetworkRequestUtil.NetworkRequestListener listener, JSONObject body,  ArrayList<NameValuePair> parameters);

    public void syncWithServer(JSONObject body, ArrayList<NameValuePair> parameters) {
        this.syncRequestWithParameters(this, body, parameters);
    }
}
