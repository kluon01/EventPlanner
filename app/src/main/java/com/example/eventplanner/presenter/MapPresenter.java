package com.example.eventplanner.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

public class MapPresenter {
    private Activity context;
    private LatLng latLng;
    private Location location;
    private LocationManager locationManager;

    public MapPresenter(Activity context) {
        this.context = context;
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
    }

    public LatLng getCurrentLocation() {
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

}
