package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.User;
import com.example.eventplanner.ui.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.ObservableEmitter;

// TODO: This is a prototyping file, will split up functions as needed and use RxJava when necessary in later development
public class LoginPresenter {

    private static final String TAG = "LoginPresenter";
    FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public LoginPresenter() {
        firestoreDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    // TODO: make this Async
    public void checkAuthentication(String email, String password, ObservableEmitter<Boolean> emitter) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful())
                        emitter.onNext(false);
                    if (task.isSuccessful())
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
                Log.d(TAG, "signInWithCredential:success");

                // Check is google account user is already in user collection
                checkUserDatabase(activity);
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

    private void checkUserDatabase(LoginActivity activity){
        FirebaseUser user = mAuth.getCurrentUser();
        firestoreDB.collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            // User is already in database
                            Log.d(TAG, "Found user in users collection");
                            // Log user in
                            activity.googleLogin(true);
                        }
                        else{
                            // Add this google account to Users collection
                            Log.d(TAG, "No user found in users collection");
                            User newUser = new User(user.getDisplayName(), user.getEmail());
                            firestoreDB.collection("users")
                                    .document(user.getUid())
                                    .set(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            // Log user in
                                            activity.googleLogin(true);
                                        }
                                        else{
                                            Log.d(TAG, "Error adding new user");
                                        }
                                    });
                        }
                    }
                    else{
                        Log.d(TAG, "Error checking user");
                    }
                });
    }

    public void signOutUser() {
        mAuth.signOut();
    }
}
