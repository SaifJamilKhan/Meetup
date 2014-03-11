package com.example.meetup.Utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.meetup.R;

public class MiscUtil {
	public static void showOkDialog(String title, String message, Context context) {
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
	public static void showNoInternetConnectionDialog(final Context context) {
		new AlertDialog.Builder(context)
		.setTitle(getStringFromId(R.string.no_internet_title, context))
				.setMessage(getStringFromId(R.string.no_internet_message, context))
				.setPositiveButton(getStringFromId(R.string.retry, context),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if(!isOnline(context)){
									showNoInternetConnectionDialog(context);
								}
							}
						})
					.setCancelable(true).show();
	}
	
	public static boolean isOnline(final Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public static String getStringFromId(int id, final Context context){
		return context.getString(id);
	}
}
