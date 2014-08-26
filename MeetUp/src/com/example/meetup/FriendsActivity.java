package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import meetup_objects.AppUserInfo;
import meetup_objects.MUModel;
import meetup_objects.MeetUpUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.meetup.Utils.PhoneContactsUtil;
import com.example.meetup.Utils.SessionsUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends Activity implements MURepository.MURepositoryObserver {

    public static class SerializeKeys {
        public static final String kFriendsSerialized = "kFriends";
    }
    ArrayList<Map<String, ?>> mFriendsList = new ArrayList<Map<String, ?>>();
    HashMap<String, MeetUpUser> mSelectedFriends = new HashMap<String, MeetUpUser>();
    HashMap<String, MeetUpUser> mUserList = new HashMap<String, MeetUpUser>();
    private SimpleAdapter simpleAdpt;
    private View mSpinner;
    private MURepository mRepository;
    private ListView mListView;

    public static class FriendsHashKeys {
        public static String FB_ID = "fbid";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        if(getIntent() != null && getIntent().getSerializableExtra(SerializeKeys.kFriendsSerialized) != null) {
            MeetUserList selectedUserList = (MeetUserList) getIntent().getSerializableExtra(SerializeKeys.kFriendsSerialized);
            mSelectedFriends = selectedUserList.mUsers;
        }

        setUpRepository();
        mSpinner = findViewById(R.id.overlay_spinner_layout);
        mSpinner.setVisibility(View.VISIBLE);

        simpleAdpt = new CustomAdapter(this, mFriendsList,
                R.layout.friend_list_item, new String[]{"name"},
                new int[]{R.id.friend_text});

        mListView = (ListView) findViewById(R.id.friends_list_view);
        mListView.setAdapter(simpleAdpt);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                View icon = view.findViewById(R.id.right_icon);
                if (icon == null) {
                    return;
                }

                if (icon.getVisibility() == View.VISIBLE) {
                    deSelectFriend(position, icon);
                } else {
                    selectFriend(position, icon);
                }
            }

            private void selectFriend(int position, View icon) {
                if (mFriendsList.get(position).get("user") != null
                        && position != 0) {
                    icon.setVisibility(View.VISIBLE);
                    HashMap<String, MeetUpUser> userInList = (HashMap<String, MeetUpUser>)mFriendsList.get(position);
                    mSelectedFriends.put(userInList.get("user").uniqueKey(), userInList.get("user"));
                }
            }

            private void deSelectFriend(int position, View icon) {
                Object objectToRemove = mFriendsList.get(position);
                if (objectToRemove != null && position != 0) {
                    icon.setVisibility(View.GONE);
                    MeetUpUser deselectedUser = ((HashMap<String, MeetUpUser>)mFriendsList.get(position)).get("user");
                    mSelectedFriends.remove(deselectedUser.uniqueKey());
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRepository.addObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRepository.removeObserver(this);
    }

    private void setUpRepository() {
        mRepository = MURepository
                .getSingleton(MURepository.SINGLETON_KEYS.KFRIENDS);
        ArrayList<MeetUpUser> usersInContacts = PhoneContactsUtil.getContacts(this);
        JSONArray contactPhoneNumbers = new JSONArray();
        for (MeetUpUser user : usersInContacts) {
            contactPhoneNumbers.put(user.getPhoneNumbers());
        }
        JSONObject wrapper = new JSONObject();
        try {
            wrapper.put("contact_numbers", contactPhoneNumbers);
        } catch (JSONException e) {
            Log.v("json exception", e.getMessage());
        }

        mRepository.makeSyncRequest(wrapper, this);
    }

//	private void makeFacebookFriendsRequest() {
//		FacebookUtil.getFriends(mUserList,
//				new FacebookUtil.FacebookEventListener() {
//
//					@Override
//					public void onFriendsListPopulated() {
//
//						populateFriendsArray(mUserList);
//						simpleAdpt.notifyDataSetChanged();
//						mSpinner.setVisibility(View.GONE);
//					}
//
//					private void populateFriendsArray(
//							HashMap<String, MeetUpUser> mUserList) {
//						Iterator<Entry<String, MeetUpUser>> it = mUserList
//								.entrySet().iterator();
//						ArrayList<Map<String, String>> mFriendsWithoutApp = new ArrayList<Map<String, String>>();
//						while (it.hasNext()) {
//							Map.Entry pairs = (Map.Entry) it.next();
//							MeetUpUser user = (MeetUpUser) pairs.getValue();
//							HashMap<String, String> userForList = createHashmap(
//									"name", user.getName());
//							userForList.put(FriendsHashKeys.FB_ID,
//									user.getFacebookId());
//							if (user.hasApp()) {
//								mFriendsList.add(userForList);
//							} else {
//								mFriendsWithoutApp.add(userForList);
//							}
//						}
//						sortFriendList(mFriendsWithoutApp);
//						sortFriendList(mFriendsList);
//						mFriendsList.add(createHashmap("seperator",
//								"Friends Without App"));
//						mFriendsList.addAll(mFriendsWithoutApp);
//						addSeperators();
//
//					}
//
//					private void sortFriendList(
//							List<Map<String, String>> friendList) {
//						Collections.sort(friendList,
//								new Comparator<Map<String, String>>() {
//									public int compare(Map<String, String> b1,
//											Map<String, String> b2) {
//										return b1
//												.get("name")
//												.toLowerCase()
//												.compareTo(
//														b2.get("name")
//																.toLowerCase());
//									}
//								});
//					}
//				});
//	}


    private void addSeperators() {
        AppUserInfo user = SessionsUtil.getUser(this);

        mFriendsList.add(
                0,
                createHashmap("user",
                        new MeetUpUser(user.getName() + "(me)", ""))
        );

        HashMap seperatorWith = new HashMap<String, String>();
        seperatorWith.put("seperator", "Friends");
        mFriendsList.add(1, seperatorWith);

//        HashMap seperatorWithout = new HashMap<String, String>();
//        seperatorWithout.put("seperator", "Contacts with the app");
//        mFriendsList.add(seperatorWithout);
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.friend_list_item, null);
            TextView text = (TextView) vi.findViewById(R.id.friend_text);
            if (text != null) {
                if (data.get(position).get("user") == null) {
                    text.setBackgroundColor(Color.BLACK);
                    text.setText((CharSequence) data.get(position).get(
                            "seperator"));
                    text.setTextColor(Color.WHITE);
                } else {
                    MeetUpUser user = (MeetUpUser) data.get(position).get("user");
                    text.setText(user.getName());
                    text.setBackgroundColor(Color.WHITE);
                    text.setTextColor(Color.BLACK);

                    View icon = vi.findViewById(R.id.right_icon);
                    if (icon != null && position > 0) {
                        icon.setVisibility(mSelectedFriends.containsKey(user.uniqueKey()) ? View.VISIBLE : View.GONE);
                    }
                }
            }
            return vi;
        }
    }

    @Override
    public void onBackPressed() {
        MeetUserList friends = new MeetUserList(mSelectedFriends);
        Intent intent = getIntent();
        intent.putExtra(SerializeKeys.kFriendsSerialized, friends);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void repositoryDidSync(MURepository repository) {
        mSpinner.setVisibility(View.GONE);
        mFriendsList.clear();
        ArrayList friendsWithApp = new ArrayList();
        Iterator it = repository.getItems().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            MeetUpUser user = (MeetUpUser) pairs.getValue();
            if (user.isFriend()) {
                mFriendsList.add(createHashmap("user", user));
            } else {
                friendsWithApp.add(createHashmap("user", user));
            }
        }
        addSeperators();
        mFriendsList.addAll(friendsWithApp);
        simpleAdpt.notifyDataSetChanged();
    }


    @Override
    public void repositoryDidFailToUpdate(MURepository repository) {
        mSpinner.setVisibility(View.GONE);
    }

    @Override
    public void repositoryDidUpdateItems(ArrayList<? extends MUModel> items) {

    }

    public static HashMap<String, MeetUpUser> createHashmap(String key, MeetUpUser user) {
        HashMap<String, MeetUpUser> planet = new HashMap<String, MeetUpUser>();
        planet.put(key, user);
        return planet;
    }
}
