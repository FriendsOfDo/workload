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

import de.friendsofdo.workload.android.api.Event;
import de.friendsofdo.workload.android.api.EventService;
import de.friendsofdo.workload.android.api.RetrofitInstance;
import retrofit2.Call;

public class LogEventService extends Service {

    private static final String TAG = "LogEventService";

    public static final String ACTION_EVENT_IN = "de.friendsofdo.workload.android.EVENT_IN";
    public static final String ACTION_EVENT_OUT = "de.friendsofdo.workload.android.EVENT_OUT";

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

        eventService = RetrofitInstance.get().create(EventService.class);
        userId = UserIdProvider.get(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

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

        Event event = new Event();
        event.setDate(now);
        event.setType(eventType);

        Log.i(TAG, "Storing event with type " + event.getType() + " and time " + event.getDate().toString() + " to backend.");

        new SaveEventTask().execute(event);

        return super.onStartCommand(intent, flags, startId);
    }

    class SaveEventTask extends AsyncTask<Event, Void, Event> {

        @Override
        protected Event doInBackground(Event... events) {
            Event event = events[0];
            Call<Event> save = eventService.save(userId, event);
            try {
                return save.execute().body();
            } catch (IOException e) {
                Log.e(TAG, "Storing event to backend failed: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Event event) {
            if(event != null) {
                Log.i(TAG, "Event successfully stored to backend. Event ID: " + event.getId());
            }
        }
    }
}
