package com.example.meetup;

import java.util.HashMap;

import org.json.JSONObject;

import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;

public class MURepository implements NetworkRequestListener {

	private String path;

	private static final HashMap<String, MURepository> instances = new HashMap<String, MURepository>();

	public enum SINGLETON_KEYS {
		KFRIENDS("/friends");
		private String path;

		SINGLETON_KEYS(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}

	public static MURepository getSingleton(SINGLETON_KEYS key) {
		if (instances.containsKey(key.toString())) {
			return instances.get(key.toString());
		} else {
			MURepository repo = new MURepository(key.path);
		}
		return null;

	}

	public MURepository(String path) {
		this.path = path;
	}

	@Override
	public void requestSucceededWithJSON(JSONObject response) {
	}

	@Override
	public void requestFailed(Exception e) {

	}

	public void makeSyncRequest() {
		// NetworkRequestUtil.makePostRequest(this.path, this);
	}

	@Override
	public void requestFailedWithJSON(JSONObject object) {
		// TODO Auto-generated method stub

	}

}
