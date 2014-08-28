package com.example.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

import java.util.HashMap;

import meetup_objects.MUModel;
import meetup_objects.MeetUpEvent;

public class MapActivity extends Activity {
	private MapFragment mMapFragment;
	private GoogleMap mMap;
	private MarkerOptions mLongClickMarkerOptions;
	private Marker mCurrentMapMarker;
	private View mLoadingSpinner;
    private MURepository mEventsRepository;
    private HashMap mShownEventMarkers = new HashMap();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
        mEventsRepository = MURepository.getSingleton(MURepository.SINGLETON_KEYS.KEVENTS);
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

    private void addMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(43.63, -79.719));
        markerOptions.title("some titme");
        markerOptions.snippet("test ");
        Marker marker = mMap.addMarker(markerOptions);
    }

    private void showEvent(MeetUpEvent muEvent) {
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
            addMarker();
//            SessionsUtil.destroyAccount(this);
//            finish();
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
