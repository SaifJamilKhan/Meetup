package com.example.meetup.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.meetup.R;

public class DialogUtil {

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
				.setTitle(
						MiscUtil.getStringFromId(R.string.no_internet_title,
								context))
				.setMessage(
						MiscUtil.getStringFromId(R.string.no_internet_message,
								context))
				.setPositiveButton(
						MiscUtil.getStringFromId(R.string.retry, context),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (!MiscUtil.isOnline(context)) {
									showNoInternetConnectionDialog(context);
								}
							}
						}).setCancelable(true).show();
	}
}
