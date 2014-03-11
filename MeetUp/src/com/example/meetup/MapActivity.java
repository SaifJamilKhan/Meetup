package com.example.meetup;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.meetup.Utils.MiscUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity {
	private MapView mMapView;
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_map);
		super.onCreate(savedInstanceState);
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.onCreate(savedInstanceState);
//		mMap = mMapView.getMap();
//		mMap.setMyLocationEnabled(true);
//		centerMapOnMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!MiscUtil.isOnline(this)){
			MiscUtil.showNoInternetConnectionDialog(this);
		}
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
