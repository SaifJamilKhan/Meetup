package com.example.meetup.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import network_clients.CreateAccountClient;

public class SessionsUtil {

    public static void saveAccount(CreateAccountClient.CreateAccountResponse response, Context context) {

        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AuthToken", response.getAuth_token());
        editor.putString("Email", response.getEmail());
        editor.putString("Name", response.getName());
        editor.commit();
    }

    public static void destroyAccount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit().clear();
        editor.commit();
    }
}
