package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meetup_objects.MeetUpEvent;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.MiscUtil;

public class EventsActivity extends Activity implements ActionBar.TabListener {

	private ArrayList<MeetUpEvent> mListOfEvents;
	private CustomAdapter simpleAdpt;
	private View mSpinner;
	private ArrayList<HashMap<String, String>> mEventsData;

	public static class EventAttributes {
		public static String EVENT_NAME = "event_name";
		public static String EVENT_DESCRIPTION = "event_description";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab newTab0 = actionBar.newTab();
		newTab0.setText("Tab 0 title");
		newTab0.setTabListener(this);
		ActionBar.Tab newTab1 = actionBar.newTab();
		newTab1.setText("Tab 1 title");
		newTab1.setTabListener(this);

		actionBar.addTab(newTab0);
		actionBar.addTab(newTab1);

		mListOfEvents = new ArrayList<MeetUpEvent>();
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mEventsData = new ArrayList<HashMap<String, String>>();
		simpleAdpt = new CustomAdapter(this, mEventsData,
				R.layout.events_list_item,
				new String[] { EventAttributes.EVENT_NAME },
				new int[] { R.id.event_title });

		setUpListView();

		getLocalEvents();
		finishLoadingData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.event_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void setUpListView() {
		ListView lv = (ListView) findViewById(R.id.events_list_view);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString(
						EventAttributes.EVENT_NAME,
						mEventsData.get(position).get(
								EventAttributes.EVENT_NAME));
				bundle.putString(EventAttributes.EVENT_DESCRIPTION, mEventsData
						.get(position).get(EventAttributes.EVENT_DESCRIPTION));
				MiscUtil.launchActivity(EventDetailsActivity.class, bundle,
						EventsActivity.this);
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
				if (data.get(position).get(EventAttributes.EVENT_NAME) != null) {
					text.setText((CharSequence) data.get(position).get(
							EventAttributes.EVENT_NAME));
				}
			}
			return vi;
		}
	}

	private void finishLoadingData() {
		simpleAdpt.notifyDataSetChanged();
		mSpinner.setVisibility(View.GONE);
	}

	private void getLocalEvents() {
		mListOfEvents = DatabaseUtil.getAllEvents(this);
		for (int x = 0; x < mListOfEvents.size(); x++) {
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put(EventAttributes.EVENT_NAME, mListOfEvents.get(x).getName());
			hash.put(EventAttributes.EVENT_DESCRIPTION, mListOfEvents.get(x)
					.getDescription());
			mEventsData.add(hash);
		}
	}

	// private void getEvents() {
	// /* make the API call */
	// new Request(ParseFacebookUtils.getSession(), "/{event-id}", null,
	// HttpMethod.GET, new Request.Callback() {
	// public void onCompleted(Response response) {
	// /* handle the result */
	// Log.v("saif", ""
	// + response.getGraphObject());
	// }
	// }).executeAsync();
	// }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
}
