package location;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class LocationServiceManager extends BroadcastReceiver {

    private SharedPreferences mPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure we are getting the right intent
//        if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            boolean mIsTracking = false;
//            // Open the shared preferences
//            mPrefs = context.getSharedPreferences("SharedPreferences",
//                    Context.MODE_PRIVATE);
//	        /*
//	         * Get any previous setting for location updates
//	         * Gets "false" if an error occurs
//	         */
//            if (mPrefs.contains("IS_TRACKING")) {
//                mIsTracking = mPrefs.getBoolean("IS_TRACKING", false);
//            }
//            if(mIsTracking){
//                ComponentName comp = new ComponentName(context.getPackageName(), LocationService.class.getName());
//                ComponentName service = context.startService(new Intent().setComponent(comp));
//
//                if (null == service){
//                    // something really wrong here
//                    Log.e("meetup", "Could not start service " + comp.toString());
//                }
//            }
//
//        } else {
//            Log.e("meetup", "Received unexpected intent " + intent.toString());
//        }
    }
}
