package com.example.eventplanner.presenter;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapPresenter {
    private Activity context;
    private LatLng latLng;
    private Location location;
    private LocationManager locationManager;

    public MapPresenter(Activity context) {
        this.context = context;
        locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
    }

    public LatLng getCurrentLocation() {
        location = getLastKnownLocation();
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return latLng;
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager)context.getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
