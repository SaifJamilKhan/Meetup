package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.meetup.Utils.DatabaseUtil;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;

public class FriendsActivity extends Activity {

	List<Map<String, String>> friendsList = new ArrayList<Map<String, String>>();

	private SimpleAdapter simpleAdpt;

	private View mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		initList();
		getFriends();
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mSpinner.setVisibility(View.VISIBLE);
		simpleAdpt = new SimpleAdapter(this, friendsList,
				android.R.layout.simple_list_item_1, new String[] { "name" },
				new int[] { android.R.id.text1 });

		ListView lv = (ListView) findViewById(R.id.friends_list_view);
		lv.setAdapter(simpleAdpt);

	}

	private void initList() {
		// We populate the planets
		friendsList.add(createPlanet("name",
				"" + DatabaseUtil.getCurrentUserName(this)));
		friendsList.add(createPlanet("name", "Friends Without Meet Up"));
	}

	private HashMap<String, String> createPlanet(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}

	private void requestFriendsList() {
		/* make the API call */
		new Request(ParseFacebookUtils.getSession(), "/{friendlist-id}", null,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						/* handle the result */
					}
				}).executeAsync();
	}

	private void getFriends() {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							try {
								GraphObject graphObject = response
										.getGraphObject();
								JSONObject jsonObject = graphObject
										.getInnerJSONObject();

								JSONArray array = jsonObject
										.getJSONArray("data");
								for (int i = 0; i < array.length(); i++) {

									JSONObject friend = array.getJSONObject(i);

									Log.d("id", friend.getString("id"));
									Log.d("firnes", friend.toString());
									friendsList.add(createPlanet("name",
											friend.getString("name")));
									friendsList.add(createPlanet("name",
											friend.getString("id")));
									// Log.d("pic_square",
									// friend.getString("pic_square"));
								}
								simpleAdpt.notifyDataSetChanged();
								mSpinner.setVisibility(View.GONE);
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
}
