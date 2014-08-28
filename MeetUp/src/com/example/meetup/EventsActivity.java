package com.example.meetup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meetup_objects.AppUserInfo;
import meetup_objects.MUModel;
import meetup_objects.MeetUpEvent;
import meetup_objects.MeetUpUser;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.DialogUtil;
import com.example.meetup.Utils.MiscUtil;
import com.example.meetup.Utils.PhoneContactsUtil;
import com.example.meetup.Utils.SessionsUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventsActivity extends Activity implements MURepository.MURepositoryObserver {

	private ArrayList<MeetUpEvent> mListOfEvents;
	private CustomAdapter simpleAdpt;
	private View mSpinner;
	private ArrayList<HashMap<String, ?>> mEventsData;
    private MURepository mRepository;

    public static class EventAttributes {
		public static String EVENT_NAME = "event_name";
		public static String EVENT_DESCRIPTION = "event_description";
        public static String EVENT_PARTICIPANTS = "event_friends";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
        setUpRepository();

		mListOfEvents = new ArrayList<MeetUpEvent>();
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mEventsData = new ArrayList<HashMap<String, ?>>();
		simpleAdpt = new CustomAdapter(this, mEventsData,
				R.layout.events_list_item,
				new String[] { EventAttributes.EVENT_NAME },
				new int[] { R.id.event_title });

		setUpListView();
        mSpinner = findViewById(R.id.overlay_spinner_layout);
        mSpinner.setVisibility(View.VISIBLE);
	}

    @Override
    protected void onDestroy() {
        mRepository.removeObserver(this);
        super.onDestroy();
    }

    private void setUpListView() {
		ListView lv = (ListView) findViewById(R.id.events_list_view);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
                if(mEventsData.get(position).containsKey("event")) {
                    MeetUpEvent event = ((MeetUpEvent) mEventsData.get(position).get("event"));
                    Bundle bundle = new Bundle();
                    bundle.putString(
                            EventAttributes.EVENT_NAME,
                            event.getName());

                    bundle.putString(EventAttributes.EVENT_DESCRIPTION, event.getDescription());
                    bundle.putSerializable(EventAttributes.EVENT_PARTICIPANTS, new MeetUserList(event.getListOfFriendHashmap()));
                    MiscUtil.launchActivity(EventDetailsActivity.class, bundle,
                            EventsActivity.this);
                }
			}
		});
		lv.setAdapter(simpleAdpt);
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
				vi = inflater.inflate(R.layout.events_list_item, null);
			TextView text = (TextView) vi.findViewById(R.id.event_title);
			if (text != null) {
                if(data.get(position).containsKey("event")) {
                    final MeetUpEvent event = (MeetUpEvent)data.get(position).get("event");
                    text.setText((CharSequence) event.getName());

                    final Button showOnMapButton = (Button)vi.findViewById(R.id.show_on_map_btn);
                    showOnMapButton.setText(event.shouldShowOnMap()? getString(R.string.event_hide_on_map) : getString(R.string.event_show_on_map));
                    showOnMapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            event.setShowOnMap(!event.shouldShowOnMap());
                            if(event.shouldShowOnMap()) {
                                showOnMapButton.setText(getString(R.string.event_hide_on_map));
                            } else {
                                showOnMapButton.setText(getString(R.string.event_show_on_map));
                            }
                        }
                    });
                }
			}
			return vi;
		}
	}

    private void setUpRepository() {
        mRepository = MURepository
                .getSingleton(MURepository.SINGLETON_KEYS.KEVENTS);
        mRepository.addObserver(this);
        mRepository.makeSyncRequest(this);
    }

	private void finishLoadingData() {
		simpleAdpt.notifyDataSetChanged();
		mSpinner.setVisibility(View.GONE);
	}

    //MURepository Observer

    @Override
    public void repositoryDidSync(final MURepository repository) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mSpinner.setVisibility(View.GONE);
                mListOfEvents = new ArrayList<MeetUpEvent>();
//                Collection<MeetUpEvent> events = (Collection<MeetUpEvent>) repository.getItems().values();
                mListOfEvents.addAll(repository.getItems().values());
                for(MeetUpEvent event : mListOfEvents) {
                    mEventsData.add(createHashmap("event", event));
                }
                simpleAdpt.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void repositoryDidFailToUpdate(MURepository repository) {

    }

    @Override
    public void repositoryDidUpdateItems(ArrayList<? extends MUModel> items) {

    }

    public static HashMap<String, MeetUpEvent> createHashmap(String key, MeetUpEvent event) {
        HashMap<String, MeetUpEvent> planet = new HashMap<String, MeetUpEvent>();
        planet.put(key, event);
        return planet;
    }
}
