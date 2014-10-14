package com.example.meetup;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.meetup.Utils.DialogUtil;
import com.example.meetup.Utils.GoogleMapsUtil;
import com.example.meetup.Utils.MiscUtil;
import com.example.meetup.Utils.SessionsUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import location.LocationService;
import meetup_objects.MeetUpEvent;
import meetup_objects.MeetUpLocation;
import providers.LocationProvider;

public class MapActivity extends Activity {
	private MapFragment mMapFragment;
	private GoogleMap mMap;
	private MarkerOptions mLongClickMarkerOptions;
	private Marker mCurrentMapMarker;
	private View mLoadingSpinner;
    private MURepository mEventsRepository;
    private HashMap mShownEventMarkers = new HashMap();
    private Button mTrackingButton;
    private SharedPreferences mSharedPrefs;
    private final String KIS_TRACKING = "is_tracking";
    private ArrayList<Polyline> mCurrentUserPolylines = new ArrayList<Polyline>();
    private ArrayList<MeetUpLocation> mLocations = new ArrayList<MeetUpLocation>();
    private Button mSyncWithServer;
    private MURepository mLocationsRepository;
//    private HashMap mAddedEvents = new HashMap();
    private MeetUpEvent currentlyShowingEvent;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
        mTrackingButton = (Button)findViewById(R.id.tracking_button);
        setUpTrackingButton();
        mSyncWithServer = (Button)findViewById(R.id.sync_with_server_button);
        setUpSyncServerButton();

        mEventsRepository = MURepository.getSingleton(MURepository.SINGLETON_KEYS.KEVENTS);
        mLocationsRepository = MURepository.getSingleton(MURepository.SINGLETON_KEYS.KLOCATIONS);

		mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapview));
		mMap = mMapFragment.getMap();

		mLoadingSpinner = findViewById(R.id.overlay_spinner_layout);
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				addLocationMarker(point);
			}
		});

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
                if(mShownEventMarkers.containsValue(marker)) {
                    //take user to events detail page
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lat", marker.getPosition().latitude);
                    bundle.putDouble("lon", marker.getPosition().longitude);
                    bundle.putString("address", marker.getSnippet());
                    MiscUtil.launchActivity(CreateEventActivity.class, bundle,
                            MapActivity.this);
                    marker.remove();
                }
			}
		});
		mMap.setMyLocationEnabled(true);
		centerMapOnMyLocation();
        showEvents();
	}

    private void setUpSyncServerButton() {
        mSyncWithServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationsRepository.makeSyncRequest(MapActivity.this, getEventIDParams());
            }
        });
    }

    private ArrayList<NameValuePair> getEventIDParams() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("event_id", currentlyShowingEvent.getID()));
        return null;
    }


    private void setUpTrackingButton() {
        mSharedPrefs = this.getSharedPreferences("Tracking",
                Context.MODE_PRIVATE);
        if (!mSharedPrefs.contains(KIS_TRACKING)) {
            mSharedPrefs.edit().putBoolean(KIS_TRACKING, false).apply();
        }
        mSharedPrefs.edit().putBoolean(KIS_TRACKING, false).apply();
        mTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTracking = switchIsTracking();
                if(isTracking) {
                    if(!isMyServiceRunning(LocationService.class)) {
                        ComponentName comp = new ComponentName(MapActivity.this.getPackageName(), LocationService.class.getName());
                        startService(new Intent().setComponent(comp));
                        LocalBroadcastManager.getInstance(MapActivity.this).registerReceiver(mMessageReceiver,
                                new IntentFilter(LocationService.KNEW_LOCATION));
                    }
                    mTrackingButton.setEnabled(false);
                } else {
                    LocalBroadcastManager.getInstance(MapActivity.this).unregisterReceiver(mMessageReceiver);
                }
            }
        });
    }
    private boolean switchIsTracking() {
        boolean isTracking = !mSharedPrefs.getBoolean(KIS_TRACKING, false);
        mSharedPrefs.edit().putBoolean(KIS_TRACKING, isTracking).apply();
        setTrackingTag(isTracking ? 1 : 0);
        return isTracking;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            MeetUpLocation newLocation = (MeetUpLocation)intent.getSerializableExtra(LocationService.KLOCATION);
            if(newLocation != null) {
                addLocation(newLocation);
            }
            switchIsTracking();
            setTrackingTag(0);
            mTrackingButton.setEnabled(true);
        }
    };

    private void addLocation(MeetUpLocation location) {
        mLocations.add(location);
        if(mLocations.size() > 1) {
            ArrayList<MeetUpLocation> lastTwo = new ArrayList<MeetUpLocation>();
            lastTwo.add(mLocations.get(mLocations.size() -2));
            lastTwo.add(mLocations.get(mLocations.size() -1));
            mCurrentUserPolylines.add(addLocationsToMap(lastTwo));
        }
    }

    private void syncLocations() {
        Uri locationsURI = Uri.parse(LocationProvider.URL);
        Cursor cursor = getContentResolver().query(locationsURI, null, null, null, null);
        mLocations = MeetUpLocation.getAllLocations(cursor);
        if(mCurrentUserPolylines != null) {
            for(Polyline line : mCurrentUserPolylines)
            {
                line.remove();
            }
            mCurrentUserPolylines.clear();
        }
        mCurrentUserPolylines.add(addLocationsToMap(mLocations));
    }

    private Polyline addLocationsToMap(ArrayList<MeetUpLocation> locations) {
        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true);
        for(MeetUpLocation location : locations) {
            polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return mMap.addPolyline(polylineOptions);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setTrackingTag(Integer tag) {
        mTrackingButton.setTag(tag);
        if(tag == 0){
            mTrackingButton.setText(R.string.tracking_button_start);
        } else {
            mTrackingButton.setText(R.string.tracking_button_stop);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isMyServiceRunning(LocationService.class)) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter(LocationService.KNEW_LOCATION));
        }
        syncLocations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showEvents();
    }

    private void showEvents() {
        for(Object marker : mShownEventMarkers.values()) {
            Marker mapMarker = (Marker)marker;
            mapMarker.remove();
        }

        for(Object event : mEventsRepository.getItems().values()) {
            MeetUpEvent muEvent = (MeetUpEvent)event;
            if(muEvent.shouldShowOnMap()) {
                showEvent(muEvent);
            }
        }
    }

    private void showEvent(MeetUpEvent muEvent) {
        currentlyShowingEvent = muEvent;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(muEvent.getLatitude(), muEvent.getLongitude()));
        markerOptions.title(muEvent.getName());
        markerOptions.snippet("test ");
        if(mShownEventMarkers.containsKey(muEvent.uniqueKey())) {
            Marker marker = (Marker)mShownEventMarkers.get(muEvent.uniqueKey());
            marker.remove();
        }
        Marker marker = mMap.addMarker(markerOptions);
        mShownEventMarkers.put(muEvent.uniqueKey(), marker);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(MapActivity.this).unregisterReceiver(mMessageReceiver);
    }

    private void addLocationMarker(LatLng point) {
		if (mCurrentMapMarker != null) {
			mCurrentMapMarker.remove();
		}
		mLongClickMarkerOptions = new MarkerOptions();
		mLongClickMarkerOptions.position(point);

		mLoadingSpinner.setVisibility(View.VISIBLE);
		String address = GoogleMapsUtil.getAddress(MapActivity.this,
				point.latitude, point.longitude);
		mLongClickMarkerOptions.title("Tap here to add event at this location");
		mLongClickMarkerOptions.snippet(address);
		mCurrentMapMarker = mMap.addMarker(mLongClickMarkerOptions);
		mLoadingSpinner.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_events:
			MiscUtil.launchActivity(EventsActivity.class, null, this);
			break;
		case R.id.action_friends:
			MiscUtil.launchActivity(FriendsActivity.class, null, this);
			break;
		case R.id.action_settings:
			MiscUtil.launchActivity(SettingsActivity.class, null, this);
			break;
        case R.id.action_logout:
            DialogUtil.showOkDialog("Logout", "Are you sure you want to log out?", this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SessionsUtil.destroyAccount(MapActivity.this);
                    MapActivity.this.finish();
                }
            });
        }
        return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void centerMapOnMyLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		Location location = locationManager
				.getLastKnownLocation(locationManager.getBestProvider(criteria,
						false));
		if (location != null) {
			mMap.setMyLocationEnabled(true);
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					location.getLatitude(), location.getLongitude()), 13));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(location.getLatitude(), location
							.getLongitude())) // Sets the center of the map to
												// location user
					.zoom(17) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(40) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
	}
}
