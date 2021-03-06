package com.example.meetup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.meetup.EventsActivity.EventAttributes;
import com.example.meetup.Utils.DialogUtil;
import com.example.meetup.Utils.MiscUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import meetup_objects.MUModel;
import meetup_objects.MeetUpEvent;
import meetup_objects.MeetUpUser;

public class CreateEventActivity extends Activity implements
		OnFocusChangeListener, MURepository.MURepositoryObserver{

	private EditText mTimePickerText;
	private EditText mDatePickerText;
	private EditText mAddressText;
	private EditText mFriendsInvitedText;
	private MeetUserList mSelectedFriends;
	private Button mCreateButton;
	private EditText mEventNameText;
	private EditText mEventDescription;
	private long mDateTimeSinceInSeconds;
	private long mTimeTimeSinceInSeconds;
	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    private MURepository mEventsRepository;
    private View mSpinner;
    private double mLatitude;
    private double mLongitude;
    private MURepository mFriendsRepository;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

        mSpinner = findViewById(R.id.overlay_spinner_layout);

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
					i.putExtra(FriendsActivity.SerializeKeys.kFriendsSerialized, mSelectedFriends);
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
                mSpinner.setVisibility(View.VISIBLE);

                ArrayList<MeetUpUser> participants = new ArrayList<MeetUpUser>();
                if(mSelectedFriends != null) {
                    for (MeetUpUser user : mSelectedFriends.getUsers().values()) {
                        participants.add(user);
                    }
                }
                MeetUpEvent event = new MeetUpEvent(mEventNameText.getText().toString(), mEventDescription.getText().toString(), mAddressText.getText().toString(),
                                                    new Date((mTimeTimeSinceInSeconds + mDateTimeSinceInSeconds) * 1000), participants, mLatitude, mLongitude);
                try {
                    MeetUpEvent.JsonTimeSerializer timeSerializer = new MeetUpEvent.JsonTimeSerializer();

                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Date.class, timeSerializer).create();

                    mEventsRepository.makePostRequest(new JSONObject(gson.toJson(event)), CreateEventActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}

		});
		mEventNameText = (EditText) findViewById(R.id.eventNameText);
		mEventDescription = (EditText) findViewById(R.id.eventDescription);

		mAddressText = (EditText) findViewById(R.id.addressText);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mAddressText.setText(bundle.getString("address"));
			mLatitude = bundle.getDouble("lat");
			mLongitude = bundle.getDouble("lon");
		}
        mEventsRepository = MURepository.getSingleton(MURepository.SINGLETON_KEYS.KEVENTS);
        mFriendsRepository = MURepository.getSingleton(MURepository.SINGLETON_KEYS.KFRIENDS);
        mEventsRepository.addObserver(this);
	}

    @Override
    protected void onDestroy() {
        mEventsRepository.removeObserver(this);
        super.onDestroy();
    }

    private boolean isFormValid() {
		if (mEventNameText.getText().length() == 0) {
			DialogUtil.showOkDialog("Invalid Event Name",
					"You forgot to add an event name!", this);
			return false;
		}
		if (mDatePickerText.getText().length() == 0) {
			DialogUtil.showOkDialog("Invalid Date",
					"You forgot to add a start date!", this);
			return false;
		}
		if (mTimePickerText.getText().length() == 0) {
			DialogUtil.showOkDialog("Invalid Time",
					"You forgot to add a start time!", this);
			return false;
		}
        if (mSelectedFriends == null || mSelectedFriends.getUsers().size() == 0) {
            DialogUtil.showOkDialog("No friends",
                    "You need to invite friends to make a meet up!", this);
            return false;
        }
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			mSelectedFriends = (MeetUserList) data
					.getSerializableExtra(FriendsActivity.SerializeKeys.kFriendsSerialized);
			if (mSelectedFriends != null) {
				mFriendsInvitedText.setText(mSelectedFriends.getUsers().size()
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
								.getDate()/1000;
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

    //MURepository observer

    @Override
    public void repositoryDidSync(MURepository repository) {

    }

    @Override
    public void repositoryDidUpdateItems(ArrayList<? extends MUModel> items) {
        final MeetUpEvent createdEvent = (MeetUpEvent)items.get(0);

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = new Bundle();
                bundle.putString(EventAttributes.EVENT_NAME, createdEvent.getName());
                bundle.putString(EventAttributes.EVENT_DESCRIPTION, createdEvent.getDescription());
                HashMap<String, MeetUpUser> participants = new HashMap<String, MeetUpUser>();
                for(MeetUpUser participant : createdEvent.getListOfFriends()) {
                     participants.put(participant.uniqueKey(), participant);
                }

                bundle.putSerializable(EventAttributes.EVENT_PARTICIPANTS, new MeetUserList(participants));
                MiscUtil.launchActivity(EventDetailsActivity.class, bundle,
                        CreateEventActivity.this);
                finish();
            }
        });
    }
    @Override
    public void repositoryDidFailToUpdate(MURepository repository) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                mSpinner.setVisibility(View.GONE);
                DialogUtil.showOkDialog("Failed to create event", "Please try again later", CreateEventActivity.this);
            }
        });
    }
}
