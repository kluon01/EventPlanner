package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class EventsFragmentPresenter {
    private FirebaseFirestore firestoreDB;
    private String TAG = "EVENTSPRESENTER";

    public EventsFragmentPresenter() {
        firestoreDB = FirebaseFirestore.getInstance();
    }

    // Prototyping way, will retrieve all events
    public void activateListener(ObservableEmitter<List<Event>> emitter) {

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


    // Retrieves events that user is in
    // TODO: implement a way that only events that have changed in some way ware updated, deleted events notify user, event changes date notifies user, etc
    public void getEventsRealTime(ObservableEmitter<List<Event>> emitter) {

        firestoreDB.collection("cities").whereEqualTo("capital", true)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (queryDocumentSnapshots == null) {
                        Log.w(TAG, "Snap shots are null");
                        return;
                    }

                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d(TAG, "Retrieved Data " + document.getData());
                        Event event = document.toObject(Event.class);
                        events.add(event);
                    }
                    emitter.onNext(events);
                });
    }
}
