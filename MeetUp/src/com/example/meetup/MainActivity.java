package com.example.meetup;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.MiscUtil;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	private View mLoadingSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "0CJwfnob0Q5w0cUUKe63I4w2lfD85slKLYP6IjSN",
				"fqYtQf9txZs0ji1w76WJGvYBnOP3qKA9yLdEuWj9");
		ParseFacebookUtils.initialize("225423810986687");
		DatabaseUtil.createDatabase(this);
		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			makeMeRequest();
		}

		setContentView(R.layout.activity_main);
		mLoadingSpinner = findViewById(R.id.overlay_spinner_layout);
		Button loginButton = (Button) findViewById(R.id.launch_login_button);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLoadingSpinner.setVisibility(View.VISIBLE);
				makeLoginThroughFacebookCall();
			}
		});

	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {

							JSONObject userProfile = new JSONObject();
							try {
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if (user.getLocation().getProperty("name") != null) {
									userProfile.put("location", (String) user
											.getLocation().getProperty("name"));
								}
								if (user.getProperty("gender") != null) {
									userProfile.put("gender",
											(String) user.getProperty("gender"));
								}
								if (user.getBirthday() != null) {
									userProfile.put("birthday",
											user.getBirthday());
								}
								if (user.getProperty("relationship_status") != null) {
									userProfile
											.put("relationship_status",
													(String) user
															.getProperty("relationship_status"));
								}

							} catch (JSONException e) {
							}
							DatabaseUtil.setUser(MainActivity.this, user
									.getName().toString());
							// createGoogleUser();
							launchMapActivity();
						} else if (response.getError() != null) {
							MiscUtil.showOkDialog("Login Response Error",
									response.getError().getErrorMessage(),
									MainActivity.this);
						}
					}
				});
		request.executeAsync();

	}

	private void makeLoginThroughFacebookCall() {

		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					makeMeRequest();
				} else if (err != null
						&& err.equals(ParseException.CONNECTION_FAILED)) {
					MiscUtil.showNoInternetConnectionDialog(MainActivity.this);
				} else if (err != null) {
					MiscUtil.showOkDialog("Error", err.getMessage(),
							MainActivity.this);
				} else {
					makeMeRequest();
				}
				mLoadingSpinner.setVisibility(View.GONE);
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO check result code
		// if (requestCode == REQUEST_CODE_FB) {
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// createGoogleUser();
			// addUser();
			// launchMapActivity();
		}
		// } else {
		super.onActivityResult(requestCode, resultCode, data);
		// }

		mLoadingSpinner.setVisibility(View.GONE);
	}

	private void addUser() {
		Session session = ParseFacebookUtils.getSession();
	}

	private void createGoogleUser() {
		// TODO create google account and skip login if it exists
		// final AccountManager accountMgr =
		// AccountManager.get(MainActivity.this);
		// accountMgr.addAccount(Constants.ACCOUNT_TYPE, null, null, null,
		// MainActivity.this, null, null);
		// for (int x = 0; x < accountMgr.getAccounts().length; x++) {
		// Log.v("", "" + accountMgr.getAccounts()[x]);
		// }
	}

	private void launchMapActivity() {
		Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
		MainActivity.this.startActivity(myIntent);
		finish();
	}

}
