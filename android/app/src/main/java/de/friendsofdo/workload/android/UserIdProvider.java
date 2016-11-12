package de.friendsofdo.workload.android;


import android.content.Context;
import android.provider.Settings;

public class UserIdProvider {

    public static String get(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
