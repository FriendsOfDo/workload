package de.friendsofdo.workload.android.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public final class PermissionsHelper {

    public static final int REQUEST_LOCATION = 110;

    public enum PermissionType {
        GRANTED,
        NOT_GRANTED,
        DENIED,
        REQUESTED
    }

    private static final String TAG = "PermissionsHelper";

    public static PermissionType isLocationPermissionsGranted(Context context) {
        boolean coarsePermissions = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean finePermissions = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (coarsePermissions || finePermissions) {
            Log.d(TAG, "Location permissions already granted");
            return PermissionType.GRANTED;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "Need to ask the user for location permissions ");
            return askForLocationPermissions(context);
        } else {
            Log.i(TAG, "Location permissions are not granted but cannot ask the user");
            return PermissionType.NOT_GRANTED;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static PermissionType askForLocationPermissions(Context context) {
        boolean coarsePermissionsDenied = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED;
        boolean finePermissionsDenied = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED;

        if (coarsePermissionsDenied || finePermissionsDenied) {
            Log.i(TAG, "Location permissions are denied by the user");
            return PermissionType.DENIED;
        }

        if (context instanceof Activity) {
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            Log.i(TAG, "Location permissions requested");
            return PermissionType.REQUESTED;
        } else {
            Log.i(TAG, "Location permissions are not granted but cannot ask the user");
            return PermissionType.NOT_GRANTED;
        }
    }
}
