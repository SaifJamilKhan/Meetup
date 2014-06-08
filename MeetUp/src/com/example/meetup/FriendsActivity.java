package com.example.meetup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import meetup_objects.MeetUpUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.FacebookUtil;

public class FriendsActivity extends Activity {

	ArrayList<Map<String, String>> mFriendsList = new ArrayList<Map<String, String>>();
	ArrayList<Map<String, String>> mSelectedFriends = new ArrayList<Map<String, String>>();
	HashMap<String, MeetUpUser> mUserList = new HashMap<String, MeetUpUser>();

	private SimpleAdapter simpleAdpt;

	private View mSpinner;

	public static class FriendsHashKeys {
		public static String FB_ID = "fbid";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		FacebookUtil.getFriends(mUserList,
				new FacebookUtil.FacebookEventListener() {

					@Override
					public void onFriendsListPopulated() {

						populateFriendsArray(mUserList);
						simpleAdpt.notifyDataSetChanged();
						mSpinner.setVisibility(View.GONE);
					}

					private void populateFriendsArray(
							HashMap<String, MeetUpUser> mUserList) {
						Iterator<Entry<String, MeetUpUser>> it = mUserList
								.entrySet().iterator();
						ArrayList<Map<String, String>> mFriendsWithoutApp = new ArrayList<Map<String, String>>();
						while (it.hasNext()) {
							Map.Entry pairs = (Map.Entry) it.next();
							MeetUpUser user = (MeetUpUser) pairs.getValue();
							HashMap<String, String> userForList = createHashmap(
									"name", user.getName());
							userForList.put(FriendsHashKeys.FB_ID,
									user.getFacebookId());
							if (user.hasApp()) {
								mFriendsList.add(userForList);
							} else {
								mFriendsWithoutApp.add(userForList);
							}
						}
						sortFriendList(mFriendsWithoutApp);
						sortFriendList(mFriendsList);
						mFriendsList.add(createHashmap("seperator",
								"Friends Without App"));
						mFriendsList.addAll(mFriendsWithoutApp);
						addSeperators();

					}

					private void sortFriendList(
							List<Map<String, String>> friendList) {
						Collections.sort(friendList,
								new Comparator<Map<String, String>>() {
									public int compare(Map<String, String> b1,
											Map<String, String> b2) {
										return b1
												.get("name")
												.toLowerCase()
												.compareTo(
														b2.get("name")
																.toLowerCase());
									}
								});
					}
				});
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mSpinner.setVisibility(View.VISIBLE);
		simpleAdpt = new CustomAdapter(this, mFriendsList,
				R.layout.friend_list_item, new String[] { "name" },
				new int[] { R.id.friend_text });

		ListView lv = (ListView) findViewById(R.id.friends_list_view);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				View icon = view.findViewById(R.id.right_icon);
				if (icon == null)
					return;
				if (icon.getVisibility() == View.VISIBLE) {
					deSelectFriend(position, icon);
				} else {
					selectFriend(position, icon);
				}
			}

			private void selectFriend(int position, View icon) {
				if (mFriendsList.get(position).get("name") != null
						&& position != 0) {
					icon.setVisibility(View.VISIBLE);
					mSelectedFriends.add(mFriendsList.get(position));
				}
			}

			private void deSelectFriend(int position, View icon) {
				Object objectToRemove = mFriendsList.get(position);
				if (objectToRemove != null && position != 0) {
					icon.setVisibility(View.GONE);
					mSelectedFriends.remove(objectToRemove);
				}
			}

		});
		lv.setAdapter(simpleAdpt);
	}

	// private int[] quickSort(int[] array, int left, int right) {
	//
	// if (left >= right) {
	// return array;
	// }
	// int pivot = left;
	//
	// for (int x = left; x < (right - left); x++) {
	// if (array[pivot] < array[x]) {
	// for (int y = right; y > (right - x); y--) {
	// if (array[pivot] > array[y]) {
	// swap(x, y, array);
	// swap(pivot, x, array);
	//
	// quickSort(array, x + 1, y - 1);
	//
	// }
	// }
	// }
	//
	// }
	// return array;
	// }
	//
	// void swap(int i, int j, int[] arr) {
	// int t = arr[i];
	// arr[i] = arr[j];
	// arr[j] = t;
	// }

	private void addSeperators() {

		mFriendsList.add(
				0,
				createHashmap("name",
						"" + DatabaseUtil.getCurrentUserName(this) + " (me)"));
		mFriendsList.add(1, createHashmap("seperator", "Friends With App"));
	}

	private class CustomAdapter extends SimpleAdapter {
		List<? extends Map<String, ?>> data;
		private LayoutInflater inflater = null;

		public CustomAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

			this.data = data;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			if (vi == null)
				vi = inflater.inflate(R.layout.friend_list_item, null);
			TextView text = (TextView) vi.findViewById(R.id.friend_text);
			if (text != null) {
				if (data.get(position).get("name") == null) {
					text.setBackgroundColor(Color.BLACK);
					text.setText((CharSequence) data.get(position).get(
							"seperator"));
					text.setTextColor(Color.WHITE);
				} else {

					text.setText((CharSequence) data.get(position).get("name"));
					text.setBackgroundColor(Color.WHITE);
					text.setTextColor(Color.BLACK);
				}
			}
			return vi;
		}
	}

	// private FriendsActivity(Parcel in) {
	//
	// int sizeOfSelected = in.readInt();
	// for (int i = 0; i < sizeOfSelected; i++) {
	// String key = in.readString();
	// String value = in.readString();
	// mSelectedFriends.add(FacebookUtil.createItem(key, value));
	// }
	//
	// int size = in.readInt();
	// for (int i = 0; i < size; i++) {
	// String key = in.readString();
	// String value = in.readString();
	// mFriendsList.add(FacebookUtil.createItem(key, value));
	// }
	// }
	public static HashMap<String, String> createHashmap(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}

	@Override
	public void onBackPressed() {
		FacebookUsersList friends = new FacebookUsersList(mSelectedFriends);
		Intent intent = getIntent();
		intent.putExtra("friends", friends);
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();
	}
}
