package com.example.meetup;

import meetup_objects.AppUser;
import network_clients.CreateAccountClient;
import network_clients.CreateAccountClient.CreateAccountClientListener;
import network_clients.CreateAccountClient.CreateAccountResponse;
import network_clients.CreateAccountClient.CreateAccountResponse.CreateAccountErrorCodes;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meetup.Utils.MiscUtil;
import com.example.meetup.Utils.SessionsUtil;

public class CreateAccountActivity extends Activity implements
		CreateAccountClientListener {

	private EditText mUserNameText;
	private EditText mPasswordText;
	private EditText mPasswordConfirmText;
	private EditText mEmailText;
	private Button mCreateAccountButton;
	private CreateAccountClient mRequestClient;
	private View mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		mUserNameText = (EditText) findViewById(R.id.create_account_user_name_text);
		mPasswordText = (EditText) findViewById(R.id.create_account_password_text);
		mPasswordConfirmText = (EditText) findViewById(R.id.create_account_password_confirm_text);
		mEmailText = (EditText) findViewById(R.id.create_account_email_text);
		mRequestClient = CreateAccountClient.getInstance();
		mSpinner = (View) findViewById(R.id.overlay_spinner_layout);

		mCreateAccountButton = (Button) findViewById(R.id.send_create_account_button);
		mCreateAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSpinner.setVisibility(View.VISIBLE);
				JSONObject body = new JSONObject();
				JSONObject user = new JSONObject();
				try {
					user.put("email", mEmailText.getText());
					user.put("password", mPasswordText.getText());
					user.put("password_confirmation",
							mPasswordConfirmText.getText());
					user.put("name", mUserNameText.getText());
					user.put("phone_number", MiscUtil
							.getDevicePhoneNumber(CreateAccountActivity.this));
					body.put("user", user);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mRequestClient.makeCreateAccountCall(
						CreateAccountActivity.this, body);

			}
		});
	}

	@Override
	public void requestSucceededWithResponse(
			final CreateAccountResponse response) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (response.isSuccess()) {
                    AppUser user = new AppUser(response.getEmail(), response.getAuth_token(), response.getName());
					SessionsUtil.saveAccount(user, CreateAccountActivity.this);
					MiscUtil.launchActivity(MapActivity.class, null,
							CreateAccountActivity.this);
				} else {
					handleError(response.getErrorCode());
				}
				mSpinner.setVisibility(View.INVISIBLE);
			}
		});
	}

	private void handleError(final CreateAccountErrorCodes errorCode) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (errorCode == CreateAccountErrorCodes.EMAIL_TAKEN) {
					Toast.makeText(CreateAccountActivity.this,
							"Email is already taken!", Toast.LENGTH_LONG)
							.show();
				} else if (errorCode == CreateAccountErrorCodes.DEFAULT) {
					Toast.makeText(CreateAccountActivity.this,
							"Something went wrong", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void requestFailedWithError() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSpinner.setVisibility(View.INVISIBLE);
				Toast.makeText(CreateAccountActivity.this,
						"Unexpected error, check internet!", Toast.LENGTH_SHORT)
						.show();
			};
		});
	}
}
