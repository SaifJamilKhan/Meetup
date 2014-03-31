package com.example.meetup.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUtil {

	private static SQLiteDatabase mDatabase;
	private static String mUserInfoTableName = "user_info";
	private static String mEventsTableName = "events";

	private static class EventTableColumns {
		public static String name = "event_name";
		public static String description = "event_description";
		public static String address = "event_address";
		public static String startDate = "start_date";
		public static String endDate = "end_date";
	}

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
			mDatabase
					.execSQL("CREATE TABLE IF NOT EXISTS "
							+ mEventsTableName
							+ " (event_name VARCHAR, event_description VARCHAR, event_address VARCHAR, start_date INTEGER, end_date INTEGER);");

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
			String eventDescription, double startDate, double endDate,
			String address) {

		try {
			mDatabase = context.openOrCreateDatabase("MeetUp",
					Context.MODE_PRIVATE, null);

			/* Insert data to a Table */
			mDatabase.execSQL("INSERT INTO " + mEventsTableName + " [("
					+ EventTableColumns.name + ","
					+ EventTableColumns.description + ", "
					+ EventTableColumns.startDate + ", "
					+ EventTableColumns.endDate + ", "
					+ EventTableColumns.address + ")]" + " VALUES ('"

					+ eventName + ", " + eventDescription + ", " + startDate
					+ ", " + endDate + ", " + address + ", " + "');");

		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			if (mDatabase != null)
				mDatabase.close();
		}
	}

	public static String getCurrentUserName(Context context) {
		mDatabase = context.openOrCreateDatabase("MeetUp",
				Context.MODE_PRIVATE, null);

		Cursor c = mDatabase.rawQuery("SELECT * FROM " + mUserInfoTableName,
				null);

		int column = c.getColumnIndex("user_name");

		// Check if our result was valid.
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

		// Check if our result was valid.
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