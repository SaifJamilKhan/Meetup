package com.example.meetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.meetup.R.id;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button loginButton = (Button) findViewById(id.login_account_button);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(LoginActivity.this, MapActivity.class);
				LoginActivity.this.startActivity(myIntent);
				LoginActivity.this.finish();
//				TextView loginUserName = (TextView) findViewById(id.login_username_text);
//				TextView loginPassword = (TextView) findViewById(id.login_password_text);
//				View loginSpinnerView = findViewById(id.overlay_spinner_layout);
//				loginSpinnerView.setVisibility(View.VISIBLE);
//
//				ParseUser.logInInBackground(loginUserName.getText().toString(),
//						loginPassword.getText().toString(),
//						new LogInCallback() {
//
//							View loginSpinnerView = findViewById(id.overlay_spinner_layout);
//
//							public void done(ParseUser user, ParseException e) {
//								if (user != null) {
//									DialogUtil.showOkDialog("Login Success!",
//											"Good job kid", LoginActivity.this);
//								} else {
//									DialogUtil.showOkDialog("Login Failed!",
//											"Error: " + e.getMessage(),
//											LoginActivity.this);
//								}
//								loginSpinnerView.setVisibility(View.GONE);
//							}
//						});
			}
		});
	}
}
