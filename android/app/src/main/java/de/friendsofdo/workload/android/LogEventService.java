package de.friendsofdo.workload.android;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LogEventService extends Service {

    private static final String BACKEND_BASE_URL = "http://10.0.2.2:8080";
    private static final String TAG = "LogEventService";

    public static final String ACTION_EVENT_IN = "de.friendsofdo.workload.android.EVENT_IN";
    public static final String ACTION_EVENT_OUT = "de.friendsofdo.workload.android.EVENT_OUT";

    private EventService eventService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        eventService = retrofit.create(EventService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Received intent to store event");

        String action = intent.getAction();

        Event.Type eventType;

        if (action.equals(ACTION_EVENT_IN)) {
            eventType = Event.Type.IN;
        } else if (action.equals(ACTION_EVENT_OUT)) {
            eventType = Event.Type.OUT;
        } else {
            throw new IllegalArgumentException("Invalid event type received. Has to be '" + ACTION_EVENT_IN
                    + "' or '" + ACTION_EVENT_OUT + "'");
        }

        Date now = new Date();
        String userId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Event event = new Event();
        event.setDate(now);
        event.setType(eventType);
        event.setUserId(userId);

        Log.i(TAG, "Storing event with type " + event.getType() + " and time " + event.getDate().toString() + " to backend.");

        new CallBackendService().execute(event);

        return super.onStartCommand(intent, flags, startId);
    }

    class CallBackendService extends AsyncTask<Event, Void, Void> {

        @Override
        protected Void doInBackground(Event... events) {
            Event event = events[0];
            Call<Event> save = eventService.save(event);
            try {
                save.execute();
            } catch (IOException e) {
                Log.e(TAG, "Storing event to backend failed: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i(TAG, "Event successfully stored to backend.");
        }
    }
}
