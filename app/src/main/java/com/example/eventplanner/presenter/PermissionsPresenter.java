package com.example.eventplanner.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsPresenter {

    private Activity context;

    // Array of required permissions
    private final String[] reqPermissions = new String[]{
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.INTERNET"
    };

    public PermissionsPresenter(Activity context) {
        this.context = context;
    }

    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // Device has camera
            return true;
        } else {
            // Device has no camera
            Toast.makeText(context,"This device does not have a camera, app cannot function.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Request for all permissions
    public void requestPermissions() {
        ActivityCompat.requestPermissions(context, reqPermissions, 101);
    }

    // Check if all permissions given
    public boolean hasAllPermissions() {
        for (String permission : reqPermissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Permissions denied, please allow all permissions.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
