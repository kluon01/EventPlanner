package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class EventsFragmentPresenter {
    private FirebaseFirestore firestoreDB;
    private String TAG = "EVENTSPRESENTER";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public EventsFragmentPresenter() {
        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    // Only get the document Ids of events user is in
    public void getUserEventDocumentIDs(ObservableEmitter<List<String>> emitter) {
        firestoreDB.collectionGroup("attendees")
                .whereEqualTo("attendant", mUser.getUid())
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
                });
    }

    // Takes teh document IDs of user events and adds a snapshot listener to each to get realtime updates
    public void setUpRTEventUpdate(List<String> eventDocumentIds, ObservableEmitter<Event> emitter) {
        for (String eventDocumentId : eventDocumentIds) {
            firestoreDB.collection("events")
                    .document(eventDocumentId)
                    .addSnapshotListener((queryDocumentSnapshot, e) -> {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshot == null) {
                            Log.w(TAG, "Snap shots are null");
                            return;
                        }

                        Log.d(TAG, "Got updated event for " + queryDocumentSnapshot.get("title").toString());
                        // TODO: debug this with new test data, data might not convert right
                        Event updatedEvent = new Event(
                                queryDocumentSnapshot.get("title").toString(),
                                queryDocumentSnapshot.get("subtitle").toString(),
                                queryDocumentSnapshot.get("info").toString(),
                                Float.parseFloat(queryDocumentSnapshot.get("latitude").toString()),
                                Float.parseFloat(queryDocumentSnapshot.get("longitude").toString()),
                                (long) queryDocumentSnapshot.get("dateAndTime"),
                                queryDocumentSnapshot.getId()
                        );

                        emitter.onNext(updatedEvent);
                    });
        }
    }

    // Prototyping way, will retrieve all events
    public void getAllEvents(ObservableEmitter<List<Event>> emitter) {

        firestoreDB.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    List<Event> events = new ArrayList<>();
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "Retrieved Data " + document.getData());
                            Event event = document.toObject(Event.class);
                            events.add(event);
                        }
                        emitter.onNext(events);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
