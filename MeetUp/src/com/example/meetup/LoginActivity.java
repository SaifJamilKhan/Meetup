package com.example.meetup;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.meetup.Utils.MiscUtil;

public class LoginActivity extends Activity {

	private EditText mUserNameEditText;
	private EditText mPasswordEditText;
	private Button mLoginButton;
	private View mLoadingSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mLoadingSpinner = findViewById(R.id.overlay_spinner_layout);

		mUserNameEditText = (EditText) findViewById(R.id.login_username_text);

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

		MiscUtil.requestFocusForTextView(mUserNameEditText, this);
	}

	private void login() {
		mLoadingSpinner.setVisibility(View.VISIBLE);
	}
}
