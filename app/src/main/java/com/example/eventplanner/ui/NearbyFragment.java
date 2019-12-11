package com.example.eventplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.presenter.MapPresenter;
import com.example.eventplanner.presenter.firebase.NearbyEventsQuery;
import com.example.eventplanner.presenter.localDB.LocalDatabaseHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NearbyFragment extends Fragment {

    private static final String TAG = "Nearby";
    private MapPresenter mapPresenter;
    private PageViewModel pageViewModel;
    private SupportMapFragment mapFragment;
    private FragmentManager fragmentManager;

    private NearbyEventsQuery nearbyEventsQuery;
    private CompositeDisposable mycompositeDisposable = new CompositeDisposable();

    //@BindView(R.id.section_label) TextView textView;

    public NearbyFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment NearbyFragment.
     */
    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nearbyEventsQuery = new NearbyEventsQuery();

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        pageViewModel.setIndex(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mapview, container, false);
        //ButterKnife.bind(this, root);
        fragmentManager = getChildFragmentManager();
        mapPresenter = new MapPresenter(getActivity());
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.nearby_map);

        pageViewModel.getText().observe(this, s -> {
            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.nearby_map, mapFragment, TAG)
                        .commit();
            }
        });
        return root;
    }

    // For testing only show events within 20 miles
    public void getNearbyEvents(LatLng userLocation, GoogleMap googleMap) {
        Observable<List<Event>> eventsNearUserObservable = Observable.create(emitter -> nearbyEventsQuery.getNearbyEvents(emitter, userLocation));
        mycompositeDisposable.add(
                eventsNearUserObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(eventsNearby ->{
                            // Display markers from events latitude and longitude values
                            for(Event event : eventsNearby){
                                LatLng coordinate = new LatLng(event.getLatitude(), event.getLongitude());
                                Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinate));
                            }
                        })
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.getMapAsync(googleMap -> {
            LatLng coordinate = mapPresenter.getCurrentLocation();
            Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinate));
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
            googleMap.animateCamera(location);

            getNearbyEvents(coordinate, googleMap);
        });

        nearbyEventsQuery = new NearbyEventsQuery();
        mapPresenter = new MapPresenter(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mycompositeDisposable.clear();
    }
}