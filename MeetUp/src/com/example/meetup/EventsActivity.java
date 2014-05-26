package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meetup_objects.MeetUpEvent;
import android.app.Activity;
import android.content.Context;
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
import com.example.meetup.Utils.MiscUtil;

public class EventsActivity extends Activity {

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
		mListOfEvents = new ArrayList<MeetUpEvent>();
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mEventsData = new ArrayList<HashMap<String, String>>();
		simpleAdpt = new CustomAdapter(this, mEventsData,
				R.layout.events_list_item,
				new String[] { EventAttributes.EVENT_NAME },
				new int[] { R.id.event_title });

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

		getLocalEvents();
		finishLoadingData();
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
}
