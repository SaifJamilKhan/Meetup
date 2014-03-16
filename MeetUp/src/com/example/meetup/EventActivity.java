package com.example.meetup;

import android.app.Activity;
import android.os.Bundle;

public class EventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// getEvents();
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
