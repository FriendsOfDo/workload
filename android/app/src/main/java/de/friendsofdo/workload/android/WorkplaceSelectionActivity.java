package de.friendsofdo.workload.android;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.widget.SeekBar;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

import de.friendsofdo.workload.android.helper.PermissionsHelper;

@EActivity
public class WorkplaceSelectionActivity extends AbstractMapActivity implements GoogleMap.OnMapClickListener, SeekBar.OnSeekBarChangeListener {

    @ViewById(R.id.radiusView)
    protected SeekBar radiusView;

    protected CircleOptions circleOptions;
    protected Circle workplaceLocation;

    @Override
    protected void onMapReady() {
        if (PermissionsHelper.isLocationPermissionsGranted(this) == PermissionsHelper.PermissionType.GRANTED) {
            setupMap();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_workplace_selection;
    }

    @AfterViews
    protected void setupUI() {
        radiusView.setMax(500);
        radiusView.setProgress(150);
        radiusView.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionsHelper.REQUEST_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupMap();
        }
    }

    protected void setupMap() {
        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(false);
        map.setTrafficEnabled(false);
        map.setOnMapClickListener(this);

        circleOptions = new CircleOptions();
        circleOptions.radius(100);
        circleOptions.fillColor(R.color.colorPrimary);
        circleOptions.visible(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (workplaceLocation == null) {
            circleOptions.center(latLng);
            workplaceLocation = map.addCircle(circleOptions);
        } else {
            workplaceLocation.setCenter(latLng);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        circleOptions.radius(i);

        if (workplaceLocation != null) {
            workplaceLocation.setRadius(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
