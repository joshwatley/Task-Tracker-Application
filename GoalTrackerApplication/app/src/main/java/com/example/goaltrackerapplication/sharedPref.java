package com.example.goaltrackerapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class sharedPref {

    public static SharedPreferences sharepreferences;

    public static sharedPref instance = null;

    public static sharedPref getInstance()
    {

        if (instance == null) {
            synchronized (sharedPref.class) {
                instance = new sharedPref();
            }
        }
        return instance;
    }
    public static void saveISLogged_IN(Context context, Boolean isLoggedin) {
        sharepreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharepreferences.edit();
        editor.putBoolean("IS_LOGIN", isLoggedin);
        editor.commit();
    }

    public static boolean getISLogged_IN(Context context) {
        sharepreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepreferences.getBoolean("IS_LOGIN", false);
    }
}
