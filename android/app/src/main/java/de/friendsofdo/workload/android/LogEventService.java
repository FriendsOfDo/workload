package de.friendsofdo.workload.android;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Date;

import de.friendsofdo.workload.android.api.Event;
import de.friendsofdo.workload.android.api.EventService;
import de.friendsofdo.workload.android.api.RetrofitInstance;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LogEventService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LogEventService";

    public static final String ACTION_EVENT_IN = "de.friendsofdo.workload.android.EVENT_IN";
    public static final String ACTION_EVENT_OUT = "de.friendsofdo.workload.android.EVENT_OUT";
    public static final int LOCATION_UPDATE_INTERVAL = 1000 * 60 * 5;

    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;

    private EventService eventService;
    private String userId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Start service to log events");

        eventService = RetrofitInstance.get().create(EventService.class);
        userId = UserIdProvider.get(this);

        googleApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(null, flags, startId);
        }

        Log.i(TAG, "Received intent to store event");

        String action = intent.getAction();

        if (action == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        Event.Type eventType;

        switch (action) {
            case ACTION_EVENT_IN:
                eventType = Event.Type.IN;
                break;
            case ACTION_EVENT_OUT:
                eventType = Event.Type.OUT;
                break;
            default:
                throw new IllegalArgumentException("Invalid event type received. Has to be '" + ACTION_EVENT_IN
                        + "' or '" + ACTION_EVENT_OUT + "'");
        }

        Date now = new Date();

        Event event = new Event();
        event.setDate(now);
        event.setType(eventType);

        if (lastKnownLocation != null) {
            event.setLat(lastKnownLocation.getLatitude());
            event.setLon(lastKnownLocation.getLongitude());
        }

        Log.i(TAG, "Storing event " + event.toString() + " to backend.");

        new SaveEventTask().execute(event);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
        Log.d(TAG, "Received location update: " + location.toString());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connection to Google API established");
        startListeningToLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult res) {
        Log.e(TAG, "Connecting to Google API failed: " + res.getErrorCode() + " -> " + res.getErrorMessage());
    }

    private void startListeningToLocationUpdates() {
        boolean coarsePermissions = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean finePermissions = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (coarsePermissions || finePermissions) {
            Log.i(TAG, "Start listening to location updates");
            LocationRequest req = new LocationRequest()
                    .setInterval(LOCATION_UPDATE_INTERVAL)
                    .setPriority(LocationRequest.PRIORITY_LOW_POWER);

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, req, this);
        } else {
            Log.w(TAG, "Permissions not sufficient to listen to location updates");
        }
    }

    private class SaveEventTask extends AsyncTask<Event, Void, Event> {

        @Override
        protected Event doInBackground(Event... events) {
            Event event = events[0];
            Call<Event> save = eventService.save(userId, event);
            try {
                Response<Event> execute = save.execute();
                if (!execute.isSuccessful()) {
                    ResponseBody responseBody = execute.errorBody();
                    Log.e(TAG, "Restoring status from backend failed: " + (responseBody != null ? responseBody.string() : "no info"));
                }
                return execute.body();
            } catch (IOException e) {
                Log.e(TAG, "Storing event to backend failed: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Event event) {
            if (event != null) {
                Log.i(TAG, "Event successfully stored to backend. Event ID: " + event.getId());
            }
        }
    }
}
