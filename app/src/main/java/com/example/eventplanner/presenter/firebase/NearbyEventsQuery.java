package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

       /* firestoreDB.collectionGroup("events")
                .where
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (queryDocumentSnapshots == null) {
                        Log.w(TAG, "Snap shots are null");
                        return;
                    }

                    final int length = queryDocumentSnapshots.getDocuments().size();
                    Log.d(TAG, "Retrieved Data of length: " + length);

                    List<String> documentIds = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d(TAG, "User event document Ids " + document.getReference().getParent().getParent().getId());
                        documentIds.add(document.getReference().getParent().getParent().getId());
                    }

                    emitter.onNext(documentIds);
                });*/
    }
}
