package com.example.eventplanner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.eventplanner.R;
import com.example.eventplanner.presenter.MapPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyFragment extends Fragment {

    private static final String TAG = "Nearby";
    private MapPresenter mapPresenter;
    private PageViewModel pageViewModel;
    private SupportMapFragment mapFragment;
    private FragmentManager fragmentManager;

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
        mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.nearby_map);

        pageViewModel.getText().observe(this, s -> {
            if(mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.nearby_map, mapFragment, TAG)
                        .commit();
            }
            mapFragment.getMapAsync(googleMap -> {
                LatLng coordinate = mapPresenter.getCurrentLocation();
                Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinate));
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate,10);
                googleMap.animateCamera(location);
            });
        });
        return root;
    }

    // For testing only show events within 20 miles
    public void getNearbyEvents(){

    }
}