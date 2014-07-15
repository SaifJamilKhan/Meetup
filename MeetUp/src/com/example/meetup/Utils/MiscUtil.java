package com.example.meetup.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MiscUtil {

	public static boolean isOnline(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static String getStringFromId(int id, final Context context) {
		return context.getString(id);
	}

	public static void launchActivity(Class<?> activty, Bundle bundle,
			Context context) {
		Intent myIntent = new Intent(context, activty);
		if (bundle != null) {
			myIntent.putExtras(bundle);
		}
		context.startActivity(myIntent);
	}

	public static void requestFocusForTextView(EditText editText,
			Context context) {
		editText.requestFocus();
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static String getDevicePhoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}
}
