package com.example.meetup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.MiscUtil;

public class MainActivity extends Activity {

	private View mLoadingSpinner;
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseUtil.createDatabase(this);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

		setContentView(R.layout.activity_main);
		mLoadingSpinner = findViewById(R.id.overlay_spinner_layout);
		Button loginButton = (Button) findViewById(R.id.launch_login_button);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MiscUtil.launchActivity(LoginActivity.class, null,
						MainActivity.this);
			}
		});

		Button signUpButton = (Button) findViewById(R.id.launch_create_account_button);

		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MiscUtil.launchActivity(CreateAccountActivity.class, null,
						MainActivity.this);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPreferences.contains("AuthToken")) {
			MiscUtil.launchActivity(MapActivity.class, null, this);
            finish();
		} else {
			// TODO: MAKE A FREAKING SPLASH SCREEN, ITS SUPER SIMPLE, DO IT
		}
	}

//	private void makeMeRequest() {
//		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
//				new Request.GraphUserCallback() {
//					@Override
//					public void onCompleted(GraphUser user, Response response) {
//						if (user != null) {
//
//							ParseUser.getCurrentUser()
//									.put("fbId", user.getId());
//							ParseUser.getCurrentUser().put("name",
//									user.getName());
//							ParseUser.getCurrentUser().saveInBackground();
//							DatabaseUtil.setUser(MainActivity.this, user
//									.getName().toString());
//							// createGoogleUser();
//							launchMapActivity();
//						} else if (response.getError() != null) {
//							DialogUtil.showOkDialog("Login Response Error",
//									response.getError().getErrorMessage(),
//									MainActivity.this);
//						}
//
//						mLoadingSpinner.setVisibility(View.GONE);
//					}
//				});
//		request.executeAsync();
//
//	}

//	private void makeLoginThroughFacebookCall() {
//
//		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
//				"user_location", "read_friendlists");
//		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
//			@Override
//			public void done(ParseUser user, ParseException err) {
//				if (user == null) {
//					makeMeRequest();
//				} else if (err != null
//						&& err.equals(ParseException.CONNECTION_FAILED)) {
//					DialogUtil
//							.showNoInternetConnectionDialog(MainActivity.this);
//					mLoadingSpinner.setVisibility(View.GONE);
//				} else if (err != null) {
//					DialogUtil.showOkDialog("Error", err.getMessage(),
//							MainActivity.this);
//
//					mLoadingSpinner.setVisibility(View.GONE);
//				} else {
//					makeMeRequest();
//				}
//			}
//
//		});
//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO check result code
		// if (requestCode == REQUEST_CODE_FB) {
//		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// createGoogleUser();
			// addUser();
            MiscUtil.launchActivity(MapActivity.class, null, this);
		}
		// } else {
		super.onActivityResult(requestCode, resultCode, data);
		// }

		mLoadingSpinner.setVisibility(View.GONE);
	}
}
