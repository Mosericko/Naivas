package com.mdevs.naivas.databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.mdevs.naivas.helperclasses.User;


public class PrefManager {
    public static final String USER_TYPE = "userType";
    private static final String PREF_NAME = "com.mdevs.naivas";
    private static final String ID = "userID";
    private static final String IS_LOGGED_IN = "loggedIn";
    @SuppressLint("StaticFieldLeak")
    private static PrefManager prefInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public PrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (prefInstance == null) {
            prefInstance = new PrefManager(context);
        }
        return prefInstance;
    }

    public void setUserLogin(User user) {

        sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt(ID, user.getId());
        editor.putString(USER_TYPE, user.getUsertype());
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();
    }
    public boolean isLoggedIn() {

        sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);

    }

    public int UserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ID, -1);
    }



    public String UserType() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_TYPE, null);
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}