package com.example.eventplanner.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHandler {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public FirebaseHandler() {
        FirebaseDatabase realtimeDB = FirebaseDatabase.getInstance();
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        HashMap user = new HashMap();
        user.put("first", "Kyle");
        user.put("last", "Davisong");
        user.put("born", 1998);

        Event mEvent = new Event("event1", 5, new LatLng(90, 5.5555));

        firestoreDB.collection("events")
                .add(mEvent)
                .addOnSuccessListener(documentReference -> {
                    // It uploaded
                })
                .addOnFailureListener(e -> {
                    // It did not upload
                });

        // Real Time Database Way (good for small amounts of data)
        /*var myEvent = Event("event3", 8,  LatLng(10997754.5,1.2222))

        myRef.setValue(myEvent)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var value = p0.value as HashMap<*, *>

                Log.d("NAME", value.get("name").toString())
                Log.d("POSITION", value.get("position").toString())
            }

        })*/


        //TODO: Authentication Handling
        // Authentication handling
        /*mAuth = FirebaseAuth.getInstance()

        mLoginButoon.setOnClickListener {

            mAuth!!.signInWithEmailAndPassword(
                    mEmailET.text.toString().trim(), mPassET.text.toString().trim()
            )
                    .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_LONG).show()
                }
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Sign In Unsuccessful", Toast.LENGTH_LONG).show()
                }
            }
        }

        mCreateButton.setOnClickListener {
            mAuth!!.createUserWithEmailAndPassword(
                    mEmailET.text.toString().trim(), mPassET.text.toString().trim()
            )
                    .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Creation Successful", Toast.LENGTH_LONG).show()
                }
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Creation Unsuccessful", Toast.LENGTH_LONG).show()
                }
            }
        }*/
    }

    class Event {
        public String name;
        public int members;
        public LatLng position;

        public Event(String n, int m, LatLng p) {
            name = n;
            members = m;
            position = new LatLng(p.latitude, p.longitude);
        }
    }
}
