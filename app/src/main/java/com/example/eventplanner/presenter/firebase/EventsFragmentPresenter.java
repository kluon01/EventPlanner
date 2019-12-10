package com.example.eventplanner.presenter.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.eventplanner.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void checkNewEvents(ObservableEmitter<List<String>> emitter, List<String> eventnames) {
        // TODO Query for event names in roomdb, if there is a mismatch grab new list from firebase
        emitter.onNext(eventnames);
    }

    public void getEventsGroupColl(ObservableEmitter<List<Event>> emitter) {
        Log.d(TAG, "Starting query");

        firestoreDB.collectionGroup("attendees")
                .whereEqualTo("attendant", mUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    List<Event> events = new ArrayList<>();

                    if (task.isSuccessful()) {
                        AtomicInteger tracker = new AtomicInteger(); // I needed a way to know when all the event results were added to the List
                        QuerySnapshot results = task.getResult();
                        final int length = results.getDocuments().size();

                        for (QueryDocumentSnapshot document : results) {
                            Log.d(TAG, "Retrieved Data of length: " + length);

                            // Nested Query to get event data
                            document.getReference().getParent().getParent().get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot queriedEvent = task1.getResult();
                                    Log.d(TAG, "Got event data" + queriedEvent.get("title"));

                                    // TODO: debug this with new test data, data might not convert right
                                    Event event = new Event(
                                            queriedEvent.get("title").toString(),
                                            queriedEvent.get("subtitle").toString(),
                                            queriedEvent.get("info").toString(),
                                            null, // TODO: obviously fix this
                                            (long) queriedEvent.get("dateAndTime")
                                    );
                                    events.add(event);
                                    tracker.getAndIncrement();
                                }
                                if (tracker.intValue() == length) {
                                    Log.d(TAG, "Got all events");
                                    emitter.onNext(events);
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "Failed to get user events " + task.getException());
                    }
                });
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

    // Example of real time updates
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

    // Add event attendee test data
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
}
