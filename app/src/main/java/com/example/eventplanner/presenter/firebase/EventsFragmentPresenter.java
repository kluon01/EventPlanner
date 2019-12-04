package com.example.eventplanner.presenter.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.eventplanner.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addEventattendee() {
        firestoreDB.collection("events")
                .document("CI5WMC5IynKVoeRKQlIw")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String eventTitle = (String) task.getResult().get("title");
                        Log.d(TAG, "Event title found " + eventTitle);

                        Map<String, Object> docData = new HashMap<>();
                        docData.put("PNvkRfetAuU4V92NVDV4", FirebaseAuth.getInstance().getCurrentUser().getEmail());


                        firestoreDB.collection("eventAttendees")
                                .document(eventTitle)
                                .set(docData)
                                .addOnCompleteListener(task1 -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User was added to event");
                                    }
                                });
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
