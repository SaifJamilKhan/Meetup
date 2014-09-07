package location;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import providers.LocationProvider;

public class LocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    final public static String KNEW_LOCATION = "new_location";
    final public static String KLOCATION= "location";
    private int KSECONDS_BETWEEN_LOCATIONS = 3;
    private Timer mTimer;
    private boolean mInProgress;
    private LocationRequest mLocationRequest;
    private Boolean servicesAvailable;
    private LocationClient mLocationClient;
    private final String KIS_TRACKING = "is_tracking";
    private SharedPreferences mSharedPrefs;

    public class LocalBinder extends Binder {
        public LocationService getServerInstance() {
            return LocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        deleteOldLocations();
        mTimer = new Timer();

        mInProgress = false;
        // Create the LocationRequest object

        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // Set t`he update interval to 5 seconds
        mLocationRequest.setInterval(KSECONDS_BETWEEN_LOCATIONS);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1);

        servicesAvailable = servicesConnected();
        
        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        mSharedPrefs = this.getSharedPreferences("Tracking",
                Context.MODE_PRIVATE);
    }

    private class LocationTimer extends TimerTask {

        @Override
        public void run() {
            if(!mSharedPrefs.contains(KIS_TRACKING) || !mSharedPrefs.getBoolean(KIS_TRACKING, false)) {
                stopSelf();
                return;
            }

            if(!servicesAvailable || mLocationClient.isConnected() || mInProgress)
                return;

            setUpLocationClientIfNeeded();
            if(!mLocationClient.isConnected() || !mLocationClient.isConnecting() && !mInProgress)
            {
                mInProgress = true;
                mLocationClient.connect();
            } else {
                mLocationClient.requestLocationUpdates(mLocationRequest, LocationService.this);
            }
        }
    }

    private void setUpLocationClientIfNeeded()
    {
        if(mLocationClient == null)
            mLocationClient = new LocationClient(this, this, this);
    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {

            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onDestroy() {
        // Turn off the request flag
        mInProgress = false;
        if(servicesAvailable && mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
            // Destroy the current location client
            mLocationClient = null;
        }
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer.scheduleAtFixedRate(new LocationTimer(), KSECONDS_BETWEEN_LOCATIONS, KSECONDS_BETWEEN_LOCATIONS);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Google Play services connection call back
    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        mInProgress = false;
        // Destroy the current location client
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d("adding meetup location", msg);
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(LocationProvider.Columns.LATITUDE, location.getLatitude());
        values.put(LocationProvider.Columns.LONGITUDE, location.getLongitude());

        values.put(LocationProvider.Columns._USER_ID, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        values.put(LocationProvider.Columns.RECORDED_AT, dateFormat.format(date));

        Uri uri = getContentResolver().insert(
                LocationProvider.CONTENT_URI, values);

        Log.d("added meetup location", uri.toString());
        notifyLocationRecieved(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mInProgress = false;

        if (connectionResult.hasResolution()) {
            //can resolve
        } else {
           //locations not happening
        }
    }

    private void deleteOldLocations() {
        Uri locationsURI = Uri.parse(LocationProvider.URL);
        getContentResolver().delete(locationsURI, null, null);
    }

    private void notifyLocationRecieved(Location location) {
        Intent intent = new Intent(KNEW_LOCATION);
        intent.putExtra(KLOCATION, location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
