package com.example.meetup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.meetup.R.id;
import com.example.meetup.Utils.DialogUtil;
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
			TextView emailText = (TextView) findViewById(id.create_account_email_text);

			@Override
			public void onClick(View v) {
				if (!checkCreateValues()) {
					return;
				}
				View loginSpinnerView = findViewById(id.overlay_spinner_layout);
				loginSpinnerView.setVisibility(View.VISIBLE);

				ParseUser user = new ParseUser();
				user.setUsername(userNameText.getText().toString());
				user.setPassword(passwordText.getText().toString());
				user.setEmail(emailText.getText().toString());
				// other fields can be set just like with ParseObject
				user.put("phone", "650-555-0000");

				user.signUpInBackground(new SignUpCallback() {
					View loginSpinnerView = findViewById(id.overlay_spinner_layout);

					public void done(ParseException e) {
						if (e == null) {
							new AlertDialog.Builder(CreateAccountActivity.this)
									.setTitle("Successfully created account")
									.setMessage("Thank you")
									.setPositiveButton(
											android.R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// continue with delete
												}
											}).show();
						} else {
							if (e.getCode() == ParseException.USERNAME_TAKEN) {
								DialogUtil.showOkDialog(
										"Account already exists",
										"Please retry",
										CreateAccountActivity.this);
							} else {
								DialogUtil.showOkDialog("Error",
										"Error: " + e.getMessage(),
										CreateAccountActivity.this);// TODO
																	// Change
																	// before
																	// release;

							}
						}
						loginSpinnerView.setVisibility(View.GONE);

					}
				});

			}

		});
	}

	private boolean checkCreateValues() {
		TextView userNameText = (TextView) findViewById(id.create_account_username_text);
		TextView passwordText = (TextView) findViewById(id.create_account_password_text);
		TextView emailText = (TextView) findViewById(id.create_account_email_text);
		if (!isEmailValid(emailText.getText())) {
			DialogUtil.showOkDialog("Email invalid",
					"Please check the email field", this);
			return false;
		}
		if (userNameText.getText().length() < 3) {
			DialogUtil.showOkDialog("User name too short",
					"Your user name needs to be atleast 3 letters", this);
			return false;
		}
		if (passwordText.getText().length() < 3) {
			DialogUtil.showOkDialog("Password is too short",
					"Your password needs to be atleast 3 letters", this);
			return false;
		}
		return true;
	}

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
