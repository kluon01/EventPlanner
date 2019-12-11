package com.example.eventplanner.presenter.firebase;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.eventplanner.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableEmitter;

public class CreateEventPresenter {
    private FirebaseFirestore firestoreDB;
    private String TAG = "CREATEEVENTPRESENTER";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public CreateEventPresenter() {
        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void addEventToFirebase(ObservableEmitter<Boolean> emitter, Event event) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("dateAndTime", event.getDateAndTime());
        docData.put("info", event.getInfo());
        docData.put("latitude", event.getLatitude());
        docData.put("longitude", event.getLongitude());
        docData.put("subtitle", event.getSubtitle());
        docData.put("title", event.getTitle());

        firestoreDB.collection("events")
                .add(docData)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentReference newEventDocument = task.getResult();

                        Map<String, Object> data = new HashMap<>();
                        data.put("attendant", mUser.getUid());

                        newEventDocument.collection("attendees")
                                .document(mUser.getUid())
                                .set(data)
                                //.update("attendant", mUser.getUid())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Log.d(TAG, "Created new event");
                                        emitter.onNext(true);
                                    }
                                    else{
                                        Log.d(TAG, "Error creating new event" + task1.getException());
                                        emitter.onNext(false);
                                    }
                                });
                    }
                    else{
                        Log.d(TAG, "Error creating event: " + task.getException());
                    }
                });

    }

    public void getLocationFromAddress(String strAddress, Context context, ObservableEmitter<LatLng> emitter) {

        Geocoder coder = new Geocoder(context);
        List<Address> address = null;
        GeoPoint p1;
        try {
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null) {
                return;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            Log.d(TAG, "Address Latitude: " + location.getLatitude());
            Log.d(TAG, "Address Longitude: " + location.getLongitude());

            LatLng eventCoord = new LatLng(location.getLatitude(), location.getLongitude());
            emitter.onNext(eventCoord);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

}
