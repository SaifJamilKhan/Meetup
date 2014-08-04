//package com.example.meetup.Utils;
//
//import java.util.ArrayList;
//import java.util.EventListener;
//import java.util.HashMap;
//import java.util.List;
//
//import meetup_objects.MeetUpUser;
//import android.os.Bundle;
//
//import com.facebook.Request;
//import com.facebook.Request.GraphUserListCallback;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
//
//public class FacebookUtil {
//	public static void getFriends(final HashMap<String, MeetUpUser> friends,
//			final FacebookEventListener listener) {
//
//		Session activeSession = Session.getActiveSession();
//		if (activeSession != null && activeSession.getState().isOpened()) {
//			Request friendRequest = Request.newMyFriendsRequest(activeSession,
//					new GraphUserListCallback() {
//						@Override
//						public void onCompleted(List<GraphUser> users,
//								Response response) {
//							try {
//
//								for (int x = 0; x < users.size(); x++) {
//									GraphUser gUser = users.get(x);
//									String id = gUser.getId();
//									String name = gUser.getName();
//									MeetUpUser user = new MeetUpUser(name, id);
//									friends.put(id, user);
//								}
//
////								getFriendsThatHaveTheApp(users, friends,
////										listener);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//						}
//					});
//			Bundle params = new Bundle();
//			params.putString("fields", "id, name, picture");
//			friendRequest.setParameters(params);
//			friendRequest.executeAsync();
//		}
//	}
//
////	public static List<ParseUser> getFriendsThatHaveTheApp(
////			List<GraphUser> users, final HashMap<String, MeetUpUser> friends,
////			FacebookEventListener listener) {
////
////		List<String> friendsListIds = new ArrayList<String>();
////		try {
////
////			for (GraphUser user : users) {
////				friendsListIds.add(user.getId());
////			}
////
////			// Construct a ParseUser query that will find friends whose
////			// facebook IDs are contained in the current user's friend list.
////			ParseQuery friendQuery = ParseQuery.getUserQuery();
////			friendQuery.whereContainedIn("fbId", friendsListIds);
////
////			// findObjects will return a list of ParseUsers that are friends
////			// with
////			// the current user
////			List<ParseUser> friendUsers = friendQuery.find();
////
////			addFriendsFromList(friends, friendUsers);
////			listener.onFriendsListPopulated();
////			return friendUsers;
////		} catch (Exception e) {
////			return null;
////		}
////	}
////
////	private static void addFriendsFromList(
////			final HashMap<String, MeetUpUser> friends,
////			List<ParseUser> friendUsers) {
////		// Collections.sort(listToAdd, new Comparator<ParseObject>() {
////		// public int compare(ParseObject b1, ParseObject b2) {
////		// return b1.getString("name").toLowerCase()
////		// .compareTo(b2.getString("name").toLowerCase());
////		// }
////		// });
////
////		// Collections.sort(friendUsers.getString("name"),
////		// String.CASE_INSENSITIVE_ORDER);
////		// HashMap<String, MeetUpUser> friendsWithApp = new HashMap<String,
////		// MeetUpUser>();
////
////		for (int x = 0; x < friendUsers.size(); x++) {
////			// MeetUpUser user = new MeetUpUser(
////			// listToAdd.get(x).getString("name"), listToAdd.get(x)
////			// .getString("fbid"));
////			String id = (String) friendUsers.get(x).getString("fbId");
////			if (friends.containsKey(id)) {
////				friends.get(id).setHasApp(true);
////			}
////			// friendsWithApp.put(listToAdd.get(x).getString("fbid"), user);
////			//
////			// HashMap<String, String> friend = createItem("name", listToAdd
////			// .get(x).getString("name"));
////			// friend.put(listToAdd.get(x).getString("fbId"), "fbId");
////			// Log.v("saif", "put id" + listToAdd.get(x).getString("fbId"));
////			// friends.add(friend);
////		}
////
////	}
//
//	public interface FacebookEventListener extends EventListener {
//		public void onFriendsListPopulated();
//	}
//}
