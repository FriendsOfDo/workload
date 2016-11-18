package de.friendsofdo.workload.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import de.friendsofdo.workload.android.api.Event;
import de.friendsofdo.workload.android.api.RetrofitInstance;
import de.friendsofdo.workload.android.api.StatusService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class WorkloadActivity extends BaseActivity {

    private static final int REQUEST_LOCATION = 110;

    private Event.Type currentState;
    private StatusService statusService;
    private static final String TAG = "WorkloadActivity";
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeEvent();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userId = UserIdProvider.get(this);
        statusService = RetrofitInstance.get().create(StatusService.class);

        new GetStatusFromBackendTask().execute();

        Log.i(TAG, "Workload activity created.");
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Object currentState = savedInstanceState.get("currentState");
        if (currentState instanceof String) {
            this.currentState = Event.Type.valueOf((String) currentState);
            adaptViewToCurrentState();
        }

        Log.i(TAG, "WorkloadActivity state restored.");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (currentState != null) {
            outState.putString("currentState", currentState.name());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        askForLocationPermissions();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForLocationPermissions() {
        boolean coarsePermissions = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean finePermissions = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (coarsePermissions || finePermissions) {
            Log.d(TAG, "Location permissions already granted");
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private void storeEvent() {
        Intent serviceIntent = new Intent(getApplicationContext(), LogEventService.class);
        if (currentState == Event.Type.IN) {
            serviceIntent.setAction(LogEventService.ACTION_EVENT_OUT);
            currentState = Event.Type.OUT;
        } else if (currentState == Event.Type.OUT) {
            serviceIntent.setAction(LogEventService.ACTION_EVENT_IN);
            currentState = Event.Type.IN;
        } else {
            Log.e(TAG, "Unknown status.");
        }
        startService(serviceIntent);
        adaptViewToCurrentState();
    }

    private void adaptViewToCurrentState() {
        if (currentState == Event.Type.IN) {
            ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_local_bar_black_24dp);
            ((TextView) findViewById(R.id.status_text)).setText(R.string.status_work);
            ((ImageView) findViewById(R.id.status_icon)).setImageResource(R.drawable.ic_work_black_24dp);
        } else {
            ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_work_black_24dp);
            ((TextView) findViewById(R.id.status_text)).setText(R.string.status_freetime);
            ((ImageView) findViewById(R.id.status_icon)).setImageResource(R.drawable.ic_local_bar_black_24dp);
        }
    }

    class GetStatusFromBackendTask extends AsyncTask<Void, Void, de.friendsofdo.workload.android.api.Status> {

        @Override
        protected de.friendsofdo.workload.android.api.Status doInBackground(Void... voids) {
            Call<de.friendsofdo.workload.android.api.Status> call = statusService.get(userId);
            try {
                Response<de.friendsofdo.workload.android.api.Status> execute = call.execute();
                if (!execute.isSuccessful()) {
                    ResponseBody responseBody = execute.errorBody();
                    Log.e(TAG, "Restoring status from backend failed: " + (responseBody != null ? responseBody.string() : "no info"));
                }
                return execute.body();
            } catch (IOException e) {
                Log.e(TAG, "Restoring status from backend failed: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(de.friendsofdo.workload.android.api.Status status) {
            if (status != null) {
                WorkloadActivity.this.currentState = status.isAtWork() ? Event.Type.IN : Event.Type.OUT;
                Log.i(TAG, "Status successfully restored from backend: " + WorkloadActivity.this.currentState.name());
            } else {
                WorkloadActivity.this.currentState = Event.Type.OUT;
                Log.i(TAG, "Using default status (OUT)");
            }
            WorkloadActivity.this.adaptViewToCurrentState();
        }
    }
}
