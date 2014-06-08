package com.example.meetup;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.meetup.EventsActivity.EventAttributes;
import com.example.meetup.Utils.DatabaseUtil;
import com.example.meetup.Utils.MiscUtil;

public class CreateEventActivity extends Activity implements
		OnFocusChangeListener {

	private EditText mTimePickerText;
	private EditText mDatePickerText;
	private EditText mAddressText;
	private EditText mFriendsInvitedText;
	private FacebookUsersList mSelectedFriends;
	private Button mCreateButton;
	private EditText mEventNameText;
	private EditText mEventDescription;
	private long mDateTimeSinceInSeconds;
	private long mTimeTimeSinceInSeconds;
	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_event);

		mTimePickerText = (EditText) findViewById(R.id.timePickerText);
		mTimePickerText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}
		});

		mDatePickerText = (EditText) findViewById(R.id.datePickerText);
		mDatePickerText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		mFriendsInvitedText = (EditText) findViewById(R.id.friendsInvitedText);
		mFriendsInvitedText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(CreateEventActivity.this,
						FriendsActivity.class);
				if (mSelectedFriends != null) {
					i.putExtra("friends", mSelectedFriends);
				}
				startActivityForResult(i, 1);
			}
		});

		mCreateButton = (Button) findViewById(R.id.createEventButton);
		mCreateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isFormValid()) {
					return;
				}
				DatabaseUtil.addEvent(CreateEventActivity.this, mEventNameText
						.getText().toString(), mEventDescription.getText()
						.toString(),
						(mDateTimeSinceInSeconds + mTimeTimeSinceInSeconds),
						mAddressText.getText().toString());

				Bundle bundle = new Bundle();
				bundle.putString(EventAttributes.EVENT_NAME, mEventNameText
						.getText().toString());
				bundle.putString(EventAttributes.EVENT_DESCRIPTION,
						mEventDescription.getText().toString());
				MiscUtil.launchActivity(EventDetailsActivity.class, bundle,
						CreateEventActivity.this);
			}

		});
		mEventNameText = (EditText) findViewById(R.id.eventNameText);
		mEventDescription = (EditText) findViewById(R.id.eventDescription);

		mAddressText = (EditText) findViewById(R.id.addressText);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mAddressText.setText(bundle.getString("address"));
			// int latitude = bundle.getInt("lat");
			// int longitude = bundle.getInt("lon");
		}
		super.onCreate(savedInstanceState);
	}

	private boolean isFormValid() {
		if (mEventNameText.getText().length() == 0) {
			MiscUtil.showOkDialog("Invalid Event Name",
					"You forgot to add an event name!", this);
			return false;
		}
		if (mDatePickerText.getText().length() == 0) {
			MiscUtil.showOkDialog("Invalid Date",
					"You forgot to add a start date!", this);
			return false;
		}
		if (mTimePickerText.getText().length() == 0) {
			MiscUtil.showOkDialog("Invalid Time",
					"You forgot to add a start time!", this);
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("some", " on activtivy ");

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			mSelectedFriends = (FacebookUsersList) data
					.getSerializableExtra("friends");
			if (mSelectedFriends != null) {
				mFriendsInvitedText.setText(mSelectedFriends.getList().size()
						+ " friends selected");
			}
		}
	}

	private String getFormattedDate(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		return (MONTHS[month] + " " + day + " - " + year);
	}

	private String getFormattedTime(TimePicker timePicker) {
		int minute = timePicker.getCurrentMinute();
		int hour = timePicker.getCurrentHour();

		return (hour - (int) (12 * Math.floor(hour / 13)) + ":"
				+ String.format("%02d", minute) + (hour > 12 ? "PM" : "AM"));
	}

	private void showTimePickerDialog() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		TimePickerDialog dialog = new TimePickerDialog(this,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						mTimePickerText.setText(getFormattedTime(view));
						mTimeTimeSinceInSeconds = view.getCurrentHour() * 3600
								+ view.getCurrentMinute() * 60;
						clearViewInput(view);
					}

				}, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
		dialog.show();
	}

	private void showDatePickerDialog() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		DatePickerDialog dialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int year,
							int monthOfYear, int dayOfMonth) {
						mDatePickerText.setText(getFormattedDate(datePicker));
						clearViewInput(datePicker);
						mDateTimeSinceInSeconds = datePicker.getCalendarView()
								.getDate();
					}

				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	@Override
	public void onFocusChange(final View view, final boolean hasFocus) {
	}

	private void clearViewInput(View view) {
		InputMethodManager imm = (InputMethodManager) CreateEventActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
