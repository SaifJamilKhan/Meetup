package com.example.meetup.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FacebookUtil {
	public static void getFriends(final List<Map<String, String>> friendsList,
			final FacebookEventListener listener) {

		Session activeSession = Session.getActiveSession();
		if (activeSession != null && activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							getFriendsThatHaveTheApp(users, friendsList,
									listener);
							try {
								GraphObject graphObject = response
										.getGraphObject();
								JSONObject jsonObject = graphObject
										.getInnerJSONObject();

								JSONArray array = jsonObject
										.getJSONArray("data");
								ArrayList<String> names = new ArrayList<String>();
								for (int i = 0; i < array.length(); i++) {
									JSONObject friend = array.getJSONObject(i);
									names.add(friend.getString("name"));
								}
								Collections.sort(names,
										String.CASE_INSENSITIVE_ORDER);

								for (int x = 0; x < array.length(); x++) {
									friendsList.add(createItem("name",
											names.get(x)));
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, picture");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}

	public static List<ParseObject> getFriendsThatHaveTheApp(
			List<GraphUser> users, final List<Map<String, String>> friendsList,
			FacebookEventListener listener) {

		List<String> friendsListIds = new ArrayList<String>();
		try {

			for (GraphUser user : users) {
				friendsListIds.add(user.getId());
			}

			// Construct a ParseUser query that will find friends whose
			// facebook IDs are contained in the current user's friend list.
			ParseQuery friendQuery = ParseQuery.getUserQuery();
			friendQuery.whereContainedIn("fbId", friendsListIds);

			// findObjects will return a list of ParseUsers that are friends
			// with
			// the current user
			List<ParseObject> friendUsers = friendQuery.find();
			List<String> names = new ArrayList<String>();
			for (ParseObject user : friendUsers) {
				names.add(user.getString("name"));
			}
			Collections.sort(names, String.CASE_INSENSITIVE_ORDER);

			for (int x = 0; x < names.size(); x++) {
				friendsList.add(createItem("name", names.get(x)));
			}
			friendsList.add(createItem("seperator", "Friends Without App"));
			listener.onFriendsListPopulated();
			return friendUsers;
		} catch (Exception e) {
			return null;
		}
	}

	public static HashMap<String, String> createItem(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}

	public interface FacebookEventListener extends EventListener {
		public void onFriendsListPopulated();
	}
}
