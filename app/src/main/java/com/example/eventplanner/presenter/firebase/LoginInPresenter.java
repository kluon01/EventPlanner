package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.ui.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import io.reactivex.ObservableEmitter;

// TODO: This is a prototyping file, will split up functions as needed and use RxJava when necessary in later development
public class LoginInPresenter {

    FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final String TAG = "FireBaseHandler";

    public LoginInPresenter() {
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
    public void checkAuthentication(String email, String password, ObservableEmitter<Boolean> emitter) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        emitter.onNext(false);
                    if(task.isSuccessful())
                        emitter.onNext(true);
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
                activity.googleLogin(true);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                activity.googleLogin(false);
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
