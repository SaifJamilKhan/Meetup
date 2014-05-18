package com.example.meetup;

import java.util.ArrayList;

import meetup_objects.MeetUpEvent;
import android.app.Activity;
import android.os.Bundle;

import com.example.meetup.Utils.DatabaseUtil;

public class EventActivity extends Activity {

	private ArrayList<MeetUpEvent> mListOfEvents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mListOfEvents = new ArrayList<MeetUpEvent>();
		getLocalEvents();
	}

	private void getLocalEvents() {
		mListOfEvents = DatabaseUtil.getAllEvents(this);
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
