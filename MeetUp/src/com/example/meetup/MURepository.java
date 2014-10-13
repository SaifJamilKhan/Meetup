package com.example.meetup;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.meetup.Utils.SessionsUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import meetup_objects.AppUserInfo;
import meetup_objects.MUModel;
import network_clients.EventsClient;
import network_clients.FriendClient;
import network_clients.LocationClient;
import network_clients.MUNetworkClient;

public class MURepository implements MUNetworkClient.MUNetworkClientListener {

    public MURepository(MUNetworkClient client) {
        mClient = client;
    }
	private static final HashMap<String, MURepository> instances = new HashMap<String, MURepository>();

    private MUNetworkClient mClient;
    private HashMap items = new HashMap();
    private interface PresistanceManager {
        public void saveModels(ArrayList<MUModel> models);
    }

    private PresistanceManager mPresistanceManager;
    public HashMap getItems() {
        return items;
    }

    public enum SINGLETON_KEYS {
		KFRIENDS(new FriendClient(), null), KEVENTS(new EventsClient(), null),
        KLOCATIONS(new LocationClient(), new PresistanceManager() {
            @Override
            public void saveModels(ArrayList<MUModel> models) {

            }
        });
		private MUNetworkClient client;
        private PresistanceManager presistanceManager;

        SINGLETON_KEYS(MUNetworkClient path, PresistanceManager manager) {
			this.client = path;
            this.presistanceManager = manager;
		}

		public MUNetworkClient getNetworkClient() {
			return client;
		}
        public PresistanceManager getPresistanceManager() {
            return presistanceManager;
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

    public void makePostRequest(JSONObject body, Context context) {
        mClient.postToServer(body, authenticationParameters(context));
    }

    public void makePostRequest(JSONArray body, Context context) {
        mClient.postToServer(body, authenticationParameters(context));
    }

    public void makeSyncRequest(Context context) {
        mClient.syncWithServer(authenticationParameters(context));
    }

    public void makeSyncRequest(JSONObject body, Context context) {
        mClient.syncWithServer(body, authenticationParameters(context));
    }


    public void makeSyncRequest(Context context, ArrayList<NameValuePair> additionalParameters) {
        ArrayList<NameValuePair> params = authenticationParameters(context);
        params.addAll(additionalParameters);
        mClient.syncWithServer(params);
    }


    public ArrayList<NameValuePair> authenticationParameters(Context context){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        AppUserInfo user = SessionsUtil.getUser(context);
        params.add(new BasicNameValuePair("user_email", user.getEmail()));
        params.add(new BasicNameValuePair("user_token", user.getAuth_token()));
        return params;
    }

    @Override
    public void requestSucceededWithResponse(final ArrayList<? extends MUModel> responses) {
        for(MUModel model : responses) {
            items.put(model.uniqueKey(), model);
        }

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                for(MURepositoryObserver observer : mObservers) {
                    observer.repositoryDidSync(MURepository.this);
                    observer.repositoryDidUpdateItems(responses);

                }
            }
        });
    }

    @Override
    public void requestFailedWithError() {
        for(MURepositoryObserver observer : mObservers) {
            observer.repositoryDidFailToUpdate(this);
        }
    }

    protected  ArrayList<MURepositoryObserver> mObservers = new ArrayList<MURepositoryObserver>();

    public interface MURepositoryObserver {
        public void repositoryDidSync(MURepository repository);
        public void repositoryDidFailToUpdate(MURepository repository);
        public void repositoryDidUpdateItems(ArrayList<? extends MUModel> items);
    }

    public void addObserver(MURepositoryObserver observer) {
        mObservers.add(observer);
    }

    public void removeObserver(MURepositoryObserver observer) {
        mObservers.remove(observer);
    }
}
