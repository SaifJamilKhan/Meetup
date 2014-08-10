package com.example.meetup;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;
import com.google.gson.Gson;

import meetup_objects.MUModel;
import network_clients.FriendClient;
import network_clients.MUNetworkClient;

public class MURepository implements MUNetworkClient.MUNetworkClientListener {

    protected  ArrayList<MURepositoryObserver> mObservers = new ArrayList<MURepositoryObserver>();

    public interface MURepositoryObserver {
        public void repositoryDidSync(MURepository repository);
        public void repositoryDidFailToUpdate(MURepository repository);
    }

	private static final HashMap<String, MURepository> instances = new HashMap<String, MURepository>();

    private MUNetworkClient mClient;
    private HashMap items = new HashMap();

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
            MUNetworkClient client = key.getNetworkClient();
			repo = new MURepository(client);
            client.setListener(repo);
			instances.put(key.getNetworkClient().getClass().toString(), repo);
		}
		return repo;
	}

    public void makeSyncRequest(JSONObject body, ArrayList<NameValuePair> parameters) {
        mClient.syncWithServer(body, parameters);
    }

    @Override
    public void requestSucceededWithResponse(ArrayList<? extends MUModel> responses) {
        for(MUModel model : responses) {
            items.put(model.uniqueKey(), model);
        }
        for(MURepositoryObserver observer : mObservers) {
            observer.repositoryDidSync(this);
        }
    }

    @Override
    public void requestFailedWithError() {
        for(MURepositoryObserver observer : mObservers) {
            observer.repositoryDidFailToUpdate(this);
        }
    }
}
