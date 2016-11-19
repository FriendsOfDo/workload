package de.friendsofdo.workload.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import de.friendsofdo.workload.android.api.Event;
import de.friendsofdo.workload.android.api.RetrofitInstance;
import de.friendsofdo.workload.android.api.Status;
import de.friendsofdo.workload.android.helper.PermissionsHelper;
import retrofit2.Response;

@EActivity(R.layout.activity_workload)
public class WorkloadActivity extends BaseActivity {

    private static final String TAG = "WorkloadActivity";

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @ViewById(R.id.nav_view)
    protected NavigationView navigationView;

    @Bean
    protected RetrofitInstance retrofitInstance;

    @Bean
    protected UserIdProvider userIdProvider;

    private Event.Type currentState;

    @AfterViews
    protected void setup() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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

        updateCurrentState();
        if (PermissionsHelper.isLocationPermissionsGranted(this) == PermissionsHelper.PermissionType.GRANTED) {
            startObservingLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionsHelper.REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startObservingLocationUpdates();
        }
    }


    @Click(R.id.fab)
    protected void clickedFloatingActionButton() {
        storeEvent();
    }

    private void startObservingLocationUpdates() {
        Intent serviceIntent = new Intent(this, LogEventService_.class);
        serviceIntent.setAction(LogEventService.ACTION_OBSERVE_LOCATION_UPDATES);
        startService(serviceIntent);
    }

    private void storeEvent() {
        Intent serviceIntent = new Intent(getApplicationContext(), LogEventService_.class);

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

    @Background
    protected void updateCurrentState() {
        String userId = userIdProvider.get();

        try {
            Response<Status> call = retrofitInstance.getStatusService().get(userId).execute();

            if (call.isSuccessful()) {
                setStatus(call.body());
            }

        } catch (IOException e) {
            Log.e(TAG, "Restoring status from backend failed: " + e.getMessage(), e);
        }
    }

    @UiThread
    protected void setStatus(Status status) {
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
