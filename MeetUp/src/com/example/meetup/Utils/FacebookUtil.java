package com.example.meetup.Utils;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import meetup_objects.MeetUpUser;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class FacebookUtil {
	public static void getFriends(final HashMap<String, MeetUpUser> friends,
			final FacebookEventListener listener) {

		Session activeSession = Session.getActiveSession();
		if (activeSession != null && activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							try {
								// GraphObject graphObject = response
								// .getGraphObject();
								// JSONObject jsonObject = graphObject
								// .getInnerJSONObject();
								//
								// JSONArray array = jsonObject
								// .getJSONArray("data");
								// HashMap<String, MeetUpUser> friends = new
								// HashMap<String, MeetUpUser>();
								for (int x = 0; x < users.size(); x++) {
									GraphUser gUser = users.get(x);
									String id = gUser.getId();
									String name = gUser.getName();
									MeetUpUser user = new MeetUpUser(name, id);
									friends.put(id, user);
								}
								// for (int x = 0; x < array.length(); x++) {
								// String id = ((JSONObject) array.get(x))
								// .getString("fbId");
								// String name = ((JSONObject) array.get(x))
								// .getString("name");
								// MeetUpUser user = new MeetUpUser(name, id);
								// friends.put(id, user);
								// Log.v("saif", "put id" + id);
								// }

								// ArrayList<String> names = new
								// ArrayList<String>();
								// for (int i = 0; i < array.length(); i++) {
								// JSONObject friend = array.getJSONObject(i);
								// names.add(friend.getString("name"));
								// }
								// Collections.sort(names,
								// String.CASE_INSENSITIVE_ORDER);

								// Collections.sort(names,
								// new Comparator<JSONObject>() {
								// public int compare(JSONObject b1,
								// JSONObject b2) {
								// return b1
								// .getJSONObject(i)
								// .toLowerCase()
								// .compareTo(
								// b2.getString(
								// "name")
								// .toLowerCase());
								// }
								// });

								// for (int x = 0; x < array.length(); x++) {
								// friendsList.add(createItem("name",
								// names.get(x)));
								//
								// HashMap<String, String> friend = createItem(
								// "name", names.get(x));
								// friend.put(
								// listToAdd.get(x).getString("fbId"),
								// "fbId");
								// Log.v("saif", "put id"
								// + listToAdd.get(x)
								// .getString("fbId"));
								// friendsList.add(friend);
								//
								// }
								getFriendsThatHaveTheApp(users, friends,
										listener);
							} catch (Exception e) {
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

	public static List<ParseUser> getFriendsThatHaveTheApp(
			List<GraphUser> users, final HashMap<String, MeetUpUser> friends,
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
			List<ParseUser> friendUsers = friendQuery.find();
			// List<String> names = new ArrayList<String>();
			// for (ParseObject user : friendUsers) {
			// names.add(user.getString("name"));
			// }

			addFriendsFromList(friends, friendUsers);
			listener.onFriendsListPopulated();
			return friendUsers;
		} catch (Exception e) {
			return null;
		}
	}

	private static void addFriendsFromList(
			final HashMap<String, MeetUpUser> friends,
			List<ParseUser> friendUsers) {
		// Collections.sort(listToAdd, new Comparator<ParseObject>() {
		// public int compare(ParseObject b1, ParseObject b2) {
		// return b1.getString("name").toLowerCase()
		// .compareTo(b2.getString("name").toLowerCase());
		// }
		// });

		// Collections.sort(friendUsers.getString("name"),
		// String.CASE_INSENSITIVE_ORDER);
		// HashMap<String, MeetUpUser> friendsWithApp = new HashMap<String,
		// MeetUpUser>();

		for (int x = 0; x < friendUsers.size(); x++) {
			// MeetUpUser user = new MeetUpUser(
			// listToAdd.get(x).getString("name"), listToAdd.get(x)
			// .getString("fbid"));
			String id = (String) friendUsers.get(x).getString("fbId");
			if (friends.containsKey(id)) {
				friends.get(id).setHasApp(true);
			}
			// friendsWithApp.put(listToAdd.get(x).getString("fbid"), user);
			//
			// HashMap<String, String> friend = createItem("name", listToAdd
			// .get(x).getString("name"));
			// friend.put(listToAdd.get(x).getString("fbId"), "fbId");
			// Log.v("saif", "put id" + listToAdd.get(x).getString("fbId"));
			// friends.add(friend);
		}

	}

	public interface FacebookEventListener extends EventListener {
		public void onFriendsListPopulated();
	}
}
