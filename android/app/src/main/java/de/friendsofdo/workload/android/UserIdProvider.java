package de.friendsofdo.workload.android;


import android.content.Context;
import android.provider.Settings;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class UserIdProvider {

    @RootContext
    protected Context context;

    public String get() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
