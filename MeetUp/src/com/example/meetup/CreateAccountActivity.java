package com.example.meetup;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.meetup.NetworkRequestUtil.NetworkRequestListener;

public class CreateAccountActivity extends Activity implements
		NetworkRequestListener {

	private EditText mUserNameText;
	private EditText mPasswordText;
	private EditText mPasswordConfirmText;
	private EditText mEmailText;
	private Button mCreateAccountButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		mUserNameText = (EditText) findViewById(R.id.create_account_user_name_text);
		mPasswordText = (EditText) findViewById(R.id.create_account_password_text);
		mPasswordConfirmText = (EditText) findViewById(R.id.create_account_password_confirm_text);
		mEmailText = (EditText) findViewById(R.id.create_account_email_text);

		mCreateAccountButton = (Button) findViewById(R.id.send_create_account_button);
		mCreateAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject body = new JSONObject();
				JSONObject user = new JSONObject();
				try {
					user.put("email", mEmailText.getText());
					user.put("password", mPasswordText.getText());
					user.put("password_confirmation",
							mPasswordConfirmText.getText());
					user.put("name", mUserNameText.getText());
					body.put("user", user);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				NetworkRequestUtil.makePostRequest("registrations",
						CreateAccountActivity.this, body);
			}
		});
	}

	@Override
	public void requestSucceededWithJSON() {

	}

	@Override
	public void requestFailed() {

	}
}
