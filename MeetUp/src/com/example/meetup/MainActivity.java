package com.example.meetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.Parse;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	Parse.initialize(this, "0CJwfnob0Q5w0cUUKe63I4w2lfD85slKLYP6IjSN", "fqYtQf9txZs0ji1w76WJGvYBnOP3qKA9yLdEuWj9");
	
		setContentView(R.layout.activity_main);
		Button loginButton = (Button) findViewById(R.id.launch_login_button);
		
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});

		Button createAccountButton = (Button) findViewById(R.id.launch_create_account_button);
		createAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, CreateAccountActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
