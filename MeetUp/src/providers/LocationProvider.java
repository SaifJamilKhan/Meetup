package providers;

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

import java.util.HashMap;

public class LocationProvider extends ContentProvider{

    public static final String PROVIDER_NAME = "providers.LocationProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/locations";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    static final UriMatcher uriMatcher;

    public static class Columns {
        public static final String _ID = "_id";
        public static final String _USER_ID = "user_id";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String RECORDED_AT = "recorded_at";
        public static final String SENT_TO_SERVER = "sent_to_server";
    }

    static final int LOCATIONS = 1;
    static final int LOCATION_ID = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "locations/#", LOCATION_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Location";
    static final String LOCATIONS_TABLE_NAME = "Locations";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + LOCATIONS_TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " user_id LONG NOT NULL, " + " latitude FLOAT NOT NULL, "
            + " longitude FLOAT NOT NULL, " + " recorded_at DATETIME NOT NULL," +  "sent_to_server BOOL);";

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
            db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
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

    private static HashMap<String, String> LOCATIONS_PROJECTION_MAP;
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LOCATIONS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case LOCATION_ID:
                qb.appendWhere(Columns._ID + "=" + uri.getPathSegments().get(1));
                break;
            case LOCATIONS:
                qb.setProjectionMap(LOCATIONS_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = Columns.RECORDED_AT;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(LOCATIONS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (uriType) {
            case LOCATIONS:
                rowsDeleted = db.delete(LOCATIONS_TABLE_NAME, Columns.RECORDED_AT + "< ?", new String[]{"DATEADD(mi,-120,GETDATE())"});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
