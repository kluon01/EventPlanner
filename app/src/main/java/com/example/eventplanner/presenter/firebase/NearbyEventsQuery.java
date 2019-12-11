package com.example.eventplanner.presenter.firebase;

import com.example.eventplanner.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class NearbyEventsQuery {
    private String TAG = "NEARBYEVENTS";
    private FirebaseFirestore firestoreDB;

    public NearbyEventsQuery() {
        firestoreDB = FirebaseFirestore.getInstance();
    }

    public void getNearbyEvents(ObservableEmitter<List<Event>> emitter, LatLng userLocation) {

        firestoreDB.collectionGroup("events")
                .get()
                .addOnSuccessListener(task -> {

                    List<Event> nearbyEvents = new ArrayList<>();
                    for (DocumentSnapshot document : task.getDocuments()) {
                        Event event = new Event(
                                document.get("title").toString(),
                                document.get("subtitle").toString(),
                                document.get("info").toString(),
                                Float.parseFloat(document.get("latitude").toString()),
                                Float.parseFloat(document.get("longitude").toString()),
                                (long) document.get("dateAndTime"),
                                document.getId()
                        );
                        nearbyEvents.add(event);
                    }
                    emitter.onNext(nearbyEvents);
                });
    }
}
