package com.example.eventplanner.presenter.firebase;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.ui.LoginActivity;
import com.example.eventplanner.ui.SignupActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

// TODO: This is a prototyping file, will split up functions as needed and use RxJava when necessary in later development
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
        user.put("last", "Davison");
        user.put("born", 1998);

        Event mEvent = new Event("event1", "event1", "info");

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
}
