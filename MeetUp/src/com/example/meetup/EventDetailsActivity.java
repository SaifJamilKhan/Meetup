package com.example.meetup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.meetup.EventsActivity.EventAttributes;

public class EventDetailsActivity extends Activity {

	// private TextView mListOfFriendsText;
	private TextView mEventNameText;
	private TextView mEventDescriptionText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		// mListOfFriendsText = (TextView)
		// findViewById(R.id.event_list_of_friends_invited);
		mEventNameText = (TextView) findViewById(R.id.event_name);
		mEventDescriptionText = (TextView) findViewById(R.id.event_description);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// mListOfFriendsText.setText(bundle.getString(AttributeNames.)
			// + " Friends");
			mEventNameText
					.setText(bundle.getString(EventAttributes.EVENT_NAME));

			mEventDescriptionText.setText(bundle
					.getString(EventAttributes.EVENT_DESCRIPTION));

			mEventNameText
					.setText(bundle.getString(EventAttributes.EVENT_NAME));

		}
	}
}
