package com.example.eventplanner.presenter;

import android.util.Log;

import com.example.eventplanner.ui.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

// TODO: This is a prototyping file, will split up functions as needed and use AsyncTask when necessary in later development
public class FirebaseHandler {

    FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final String TAG = "FireBaseHandler";



    public FirebaseHandler() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void addTestDataEvent() {
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
    }

    // TODO: make this Async
    public void checkAuthentication(String email, String password, LoginActivity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        activity.defaultLogin(false);
                    if(task.isSuccessful())
                        activity.defaultLogin(true);
                });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, LoginActivity activity) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success");
                FirebaseUser user = mAuth.getCurrentUser();
                //activity.checkUser(); // For debugging
                activity.defaultLogin(true);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                activity.defaultLogin(false);
            }
        });

        // [START_EXCLUDE]
        //hideProgressDialog();
        // [END_EXCLUDE]
    }

    public void signOutUser(){
        mAuth.signOut();
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
