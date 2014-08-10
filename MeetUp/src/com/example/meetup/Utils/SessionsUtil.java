package com.example.meetup.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import meetup_objects.AppUserInfo;

public class SessionsUtil {

    public static void saveAccount(AppUserInfo user, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AuthToken", user.getAuth_token());
        editor.putString("Email", user.getEmail());
        editor.putString("Name", user.getName());
        editor.commit();
    }

    public static void destroyAccount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit().clear();
        editor.commit();
    }

    public static AppUserInfo getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        AppUserInfo user = new AppUserInfo(preferences.getString("Email", null), preferences.getString("AuthToken", null), preferences.getString("Name", null));
        return user;

    }
}
