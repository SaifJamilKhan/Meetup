package com.example.meetup.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.meetup.R;

public class MiscUtil {
	public static void showOkDialog(String title, String message,
			Context context) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}

	public static void showOkDialog(String title, String message,
			Context context,
			android.content.DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton(android.R.string.ok, listener).show();
	}

	public static void showNoInternetConnectionDialog(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle(getStringFromId(R.string.no_internet_title, context))
				.setMessage(
						getStringFromId(R.string.no_internet_message, context))
				.setPositiveButton(getStringFromId(R.string.retry, context),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (!isOnline(context)) {
									showNoInternetConnectionDialog(context);
								}
							}
						}).setCancelable(true).show();
	}

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
}
