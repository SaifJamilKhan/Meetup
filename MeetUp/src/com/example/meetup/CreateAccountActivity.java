package com.example.meetup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.meetup.R.id;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		Button createAccountButton = (Button) findViewById(id.create_account_button);
		createAccountButton.setOnClickListener(new OnClickListener() {
		TextView userNameText = (TextView) findViewById(id.create_account_username_text);
		TextView passwordText = (TextView) findViewById(id.create_account_password_text);
			
			@Override
			public void onClick(View v) {
				ParseUser user = new ParseUser();
				user.setUsername(userNameText.getText().toString());
				user.setPassword(passwordText.getText().toString());
				user.setEmail("email@example.com");
				  
				// other fields can be set just like with ParseObject
				user.put("phone", "650-555-0000");
				  
				user.signUpInBackground(new SignUpCallback() {
				  public void done(ParseException e) {
				    if (e == null) {
				      // Hooray! Let them use the app now.
				    } else {
				      // Sign up didn't succeed. Look at the ParseException
				      // to figure out what went wrong
				    }
				  }
				});
				
			}
		});
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}
	
	
}
