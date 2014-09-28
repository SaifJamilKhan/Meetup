package com.example.meetup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.meetup.Utils.MiscUtil;

import org.json.JSONException;
import org.json.JSONObject;

import network_clients.SessionsClient;
import network_clients.SessionsClient.LoginResponse;

public class LoginActivity extends Activity implements SessionsClient.SessionsClientListener {

	private EditText mUserEmailText;
	private EditText mPasswordEditText;
	private Button mLoginButton;
	private SessionsClient mLoginClient;
	private SharedPreferences mPreferences;
	private View mSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mSpinner = findViewById(R.id.overlay_spinner_layout);
		mLoginClient = SessionsClient.getInstance();
		mUserEmailText = (EditText) findViewById(R.id.login_email_text);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

		mPasswordEditText = (EditText) findViewById(R.id.login_password_text);
		mPasswordEditText
				.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							login();
						}
						return false;
					}
				});

		mLoginButton = (Button) findViewById(R.id.login_button);

		mLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});

		MiscUtil.requestFocusForTextView(mUserEmailText, this);
	}

	private void login() {
		mSpinner.setVisibility(View.VISIBLE);
		JSONObject body = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put("email", mUserEmailText.getText());
			user.put("password", mPasswordEditText.getText());
			body.put("user", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mLoginClient.makeLoginCall(this, body);
	}

	@Override
	public void requestSucceededWithResponse(final LoginResponse response) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (response.isSuccess()) {
					saveAccount(response);
					MiscUtil.launchActivity(MapActivity.class, null,
							LoginActivity.this);
				} else {
					Toast.makeText(LoginActivity.this, response.getError(),
							Toast.LENGTH_LONG).show();
				}
				mSpinner.setVisibility(View.INVISIBLE);
			}
		});
	}

	private void saveAccount(LoginResponse response) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString("AuthToken", response.getAuth_token());
		editor.commit();
	}

	@Override
	public void requestFailedWithError() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mSpinner.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Unexpected error, check internet!",
                        Toast.LENGTH_SHORT).show();
            }
        });
	}
}
