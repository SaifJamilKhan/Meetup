package com.example.meetup.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GoogleMapsUtil {

	public static String getAddress(Context context, double lat, double lng) {
		try {
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				return (addresses.get(0).getFeatureName() + " "
						+ addresses.get(0).getThoroughfare() + ", " + addresses
						.get(0).getLocality());

			}
			return sb.toString();
		} catch (IOException e) {
			return "";
		}
	}
}
