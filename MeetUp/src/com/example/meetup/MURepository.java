package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;

import network_clients.FriendClient;
import network_clients.MUNetworkClient;

public class MURepository implements MUNetworkClient.MUNetworkClientObserver {

	private static final HashMap<String, MURepository> instances = new HashMap<String, MURepository>();

    private MUNetworkClient mClient;

    public MURepository(MUNetworkClient client) {
        mClient = client;
    }

    public enum SINGLETON_KEYS {
		KFRIENDS(new FriendClient());
		private MUNetworkClient client;

		SINGLETON_KEYS(MUNetworkClient path) {
			this.client = path;
		}

		public MUNetworkClient getNetworkClient() {
			return client;
		}
	}

	public static MURepository getSingleton(SINGLETON_KEYS key) {
		MURepository repo;
		if (instances.containsKey(key.getNetworkClient().getClass().toString())) {
			repo = instances.get(key.getNetworkClient().getClass().toString());
		} else {
			repo = new MURepository(key.getNetworkClient());
			instances.put(key.getNetworkClient().getClass().toString(), repo);
		}
		return repo;
	}

    public void makeSyncRequest(JSONObject body, ArrayList<NameValuePair> parameters) {
        mClient.syncWithServer(body, parameters);
    }

    @Override
    public void requestSucceededWithResponse(ArrayList responses) {

    }

    @Override
    public void requestFailedWithError() {

    }
}
