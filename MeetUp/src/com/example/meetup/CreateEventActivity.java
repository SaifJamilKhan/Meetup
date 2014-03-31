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
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CreateEventActivity extends Activity {

	private EditText mTimePickerText;
	private EditText mDatePickerText;
	private EditText mAddressText;
	private EditText mFriendsInvitedText;

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
				startActivityForResult(i, 1);
			}
		});

		mAddressText = (EditText) findViewById(R.id.addressText);
		Bundle bundle = getIntent().getExtras();
		mAddressText.setText(bundle.getString("address"));
		int latitude = bundle.getInt("lat");
		int longitude = bundle.getInt("lon");

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("some", " on activtivy ");

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Object friends = bundle.get("friends");

			Log.v("saif", "" + friends.getClass());
			// data.getPar
			// Do whatever you want with yourData
		}
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
					}

				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

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
						clearViewInput(view);
					}

				}, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
		dialog.show();
	}

	private void clearViewInput(View view) {
		InputMethodManager imm = (InputMethodManager) CreateEventActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
