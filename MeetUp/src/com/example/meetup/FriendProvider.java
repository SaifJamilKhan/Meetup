package com.example.meetup;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FriendProvider extends ContentProvider {

	static final String PROVIDER_NAME = "com.example.meetup.FriendProvider";
	static final String URL = "content://" + PROVIDER_NAME + "/friends";
	static final Uri CONTENT_URI = Uri.parse(URL);

	static final String _ID = "_id";
	static final String NAME = "name";
	static final String EMAIL = "email";
	static final String PHONE_NUMBER = "phone_number";

	static final int FRIENDS = 1;
	static final int FRIEND_ID = 2;

	static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "students", FRIENDS);
		uriMatcher.addURI(PROVIDER_NAME, "students/#", FRIEND_ID);
	}

	/**
	 * Database specific constant declarations
	 */
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "Friend";
	static final String FRIENDS_TABLE_NAME = "friends";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE = " CREATE TABLE " + FRIENDS_TABLE_NAME
			+ " (_id TEXT PRIMARY KEY, " + " name TEXT NOT NULL, "
			+ " phone_number TEXT NOT NULL, " + " email TEXT NOT NULL);";

	/**
	 * Helper class that actually creates and manages the provider's underlying
	 * data repository.
	 */

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DB_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE_NAME);
			onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		// create db
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		long rowID = db.insert(FRIENDS_TABLE_NAME, "", values);

		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(FRIENDS_TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case FRIEND_ID:
			qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == "") {
			sortOrder = NAME;
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case FRIENDS:
			count = db.delete(FRIENDS_TABLE_NAME, selection, selectionArgs);
			break;
		case FRIEND_ID:
			String id = uri.getPathSegments().get(1);
			count = db.delete(FRIENDS_TABLE_NAME, _ID
					+ " = "
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case FRIENDS:
			count = db.update(FRIENDS_TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case FRIEND_ID:
			count = db.update(
					FRIENDS_TABLE_NAME,
					values,
					_ID
							+ " = "
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case FRIENDS:
			return "vnd.android.cursor.dir/vnd.example.students";
		case FRIEND_ID:
			return "vnd.android.cursor.item/vnd.example.students";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
}