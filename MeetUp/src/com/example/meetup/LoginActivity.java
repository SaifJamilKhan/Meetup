package com.example.meetup;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.meetup.R.id;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Button loginButton = (Button) findViewById(id.login_account_button);
		// loginButton.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// makeLoginThroughFacebookCall();
		// Intent myIntent = new Intent(LoginActivity.this,
		// MapActivity.class);
		// LoginActivity.this.startActivity(myIntent);
		// LoginActivity.this.finish();
		// TextView loginUserName = (TextView)
		// findViewById(id.login_username_text);
		// TextView loginPassword = (TextView)
		// findViewById(id.login_password_text);
		// View loginSpinnerView = findViewById(id.overlay_spinner_layout);
		// loginSpinnerView.setVisibility(View.VISIBLE);

		// ParseUser.logInInBackground(loginUserName.getText().toString(),
		// loginPassword.getText().toString(),
		// new LogInCallback() {
		//
		// View loginSpinnerView =
		// findViewById(id.overlay_spinner_layout);
		//
		// public void done(ParseUser user, ParseException e) {
		// if (user != null) {
		// DialogUtil.showOkDialog("Login Success!",
		// "Good job kid", LoginActivity.this);
		// } else {
		// DialogUtil.showOkDialog("Login Failed!",
		// "Error: " + e.getMessage(),
		// LoginActivity.this);
		// }
		// loginSpinnerView.setVisibility(View.GONE);
		// }
		// });
		// }

		// });
	}

	private void makeLoginThroughFacebookCall() {
		View loginSpinnerView = findViewById(id.overlay_spinner_layout);
		loginSpinnerView.setVisibility(View.VISIBLE);
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {

				} else if (user.isNew()) {

				} else {

				}
			}
		});
	}
}
