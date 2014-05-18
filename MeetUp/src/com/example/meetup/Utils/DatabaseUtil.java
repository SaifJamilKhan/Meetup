package com.example.meetup.Utils;

import java.util.ArrayList;

import meetup_objects.MeetUpEvent;
import meetup_objects.MeetUpEvent.EventTableColumns;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUtil {

	private static SQLiteDatabase mDatabase;
	private static String mUserInfoTableName = "user_info";
	private static String mEventsTableName = "events";

	public static void createDatabase(Context context) {

		/* Create a Database. */
		try {
			mDatabase = context.openOrCreateDatabase("MeetUp",
					Context.MODE_PRIVATE, null);

			/* Create User Table in the Database. */
			mDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
					+ mUserInfoTableName
					+ " (user_name VARCHAR, user_email VARCHAR);");

			/* Create Event Table in the Database. */
			mDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + mEventsTableName
					+ " (" + MeetUpEvent.EventTableColumns.name + " VARCHAR, "
					+ MeetUpEvent.EventTableColumns.description + " VARCHAR, "
					+ MeetUpEvent.EventTableColumns.address + " VARCHAR, "
					+ MeetUpEvent.EventTableColumns.startDate + " INTEGER, "
					+ MeetUpEvent.EventTableColumns.endDate + " INTEGER);");

		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			if (mDatabase != null)
				mDatabase.close();
		}
	}

	public static void setUser(Context context, String name) {

		try {
			mDatabase = context.openOrCreateDatabase("MeetUp",
					Context.MODE_PRIVATE, null);

			/* Insert data to a Table */
			mDatabase.execSQL("INSERT INTO " + mUserInfoTableName
					+ " (user_name)" + " VALUES ('" + name + "');");

		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			if (mDatabase != null)
				mDatabase.close();
		}
	}

	public static void addEvent(Context context, String eventName,
			String eventDescription, double startDate, String address) {

		try {
			mDatabase = context.openOrCreateDatabase("MeetUp",
					Context.MODE_PRIVATE, null);

			/* Insert data to a Table */
			mDatabase.execSQL("INSERT INTO " + mEventsTableName + " ("
					+ EventTableColumns.name + ","
					+ EventTableColumns.description + ", "
					+ EventTableColumns.startDate + ", "
					+ EventTableColumns.address + ")" + " VALUES ('"

					+ eventName + ", " + eventDescription + ", " + startDate
					+ ", " + address + "');");

		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			if (mDatabase != null)
				mDatabase.close();
		}
	}

	public static ArrayList<MeetUpEvent> getAllEvents(Context context) {
		mDatabase = context.openOrCreateDatabase("MeetUp",
				Context.MODE_PRIVATE, null);

		Cursor c = mDatabase
				.rawQuery("SELECT * FROM " + mEventsTableName, null);

		ArrayList<MeetUpEvent> events = new ArrayList<MeetUpEvent>();

		c.moveToFirst();
		if (c != null) {
			while (c.moveToNext()) {
				String name = c.getString(c
						.getColumnIndex(EventTableColumns.name));
				String description = c.getString(c
						.getColumnIndex(EventTableColumns.description));
				String address = c.getString(c
						.getColumnIndex(EventTableColumns.address));
				String endDate = c.getString(c
						.getColumnIndex(EventTableColumns.endDate));
				String startDate = c.getString(c
						.getColumnIndex(EventTableColumns.startDate));

				MeetUpEvent event = new MeetUpEvent(name, description, address,
						startDate, endDate);
				events.add(event);
			}
			return events;
		} else {
			return null;
		}
	}

	public static String getCurrentUserName(Context context) {
		mDatabase = context.openOrCreateDatabase("MeetUp",
				Context.MODE_PRIVATE, null);

		Cursor c = mDatabase.rawQuery("SELECT * FROM " + mUserInfoTableName,
				null);

		int column = c.getColumnIndex("user_name");

		c.moveToFirst();
		if (c != null) {
			// Loop through all Results
			return c.getString(column);
		} else {
			return null;
		}
	}

	public static String getCurrentUserEmail(Context context) {

		Cursor c = mDatabase.rawQuery("SELECT * FROM " + mUserInfoTableName,
				null);

		int column = c.getColumnIndex("user_email");

		c.moveToFirst();
		if (c != null) {
			// Loop through all Results
			return c.getString(column);
		} else {
			return null;
		}
	}
}

// /* retrieve data from database */
// Cursor c = myDB.rawQuery("SELECT * FROM " + TableName, null);
//
// int Column1 = c.getColumnIndex("user_name");
// int Column2 = c.getColumnIndex("user_email");
//
// // Check if our result was valid.
// c.moveToFirst();
// if (c != null) {
// // Loop through all Results
// String Name = c.getString(Column1);
// }