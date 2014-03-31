package com.example.meetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

public class FriendsActivity extends Activity implements Parcelable {

	List<Map<String, String>> mFriendsList = new ArrayList<Map<String, String>>();
	List<Map<String, String>> mSelectedFriends = new ArrayList<Map<String, String>>();

	private SimpleAdapter simpleAdpt;

	private View mSpinner;

	public FriendsActivity(Parcel in) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		initList();
		FacebookUtil.getFriends(mFriendsList,
				new FacebookUtil.FacebookEventListener() {

					@Override
					public void onFriendsListPopulated() {
						simpleAdpt.notifyDataSetChanged();
						mSpinner.setVisibility(View.GONE);
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

	private void initList() {
		mFriendsList.add(FacebookUtil.createItem("name",
				"" + DatabaseUtil.getCurrentUserName(this) + " (me)"));
		mFriendsList.add(FacebookUtil.createItem("seperator",
				"Friends With App"));
	}

	class CustomAdapter extends SimpleAdapter {
		Context context;
		List<? extends Map<String, ?>> data;
		private LayoutInflater inflater = null;

		public CustomAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

			// TODO Auto-generated constructor stub
			this.context = context;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mSelectedFriends.size());
		for (int x = 0; x < mSelectedFriends.size(); x++) {
			dest.writeString((String) mSelectedFriends.get(x).keySet()
					.toArray()[0]);
			dest.writeString((String) mSelectedFriends.get(x).get("name"));
		}

		dest.writeInt(mFriendsList.size());
		for (int x = 0; x < mFriendsList.size(); x++) {
			dest.writeString((String) mFriendsList.get(x).keySet().toArray()[0]);
			dest.writeString((String) mFriendsList.get(x).get("name"));
		}

	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public FriendsActivity createFromParcel(Parcel in) {
			return new FriendsActivity(in);
		}

		public FriendsActivity[] newArray(int size) {
			return new FriendsActivity[size];
		}
	};

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

	@Override
	public void onBackPressed() {
		Intent intent = getIntent();
		intent.putExtra("friends", this);
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();
	}
}
