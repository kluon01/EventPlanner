package com.example.eventplanner.presenter.firebase;

import android.util.Log;

import com.example.eventplanner.model.User;
import com.example.eventplanner.ui.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.ObservableEmitter;

public class SignUpPresenter {
    private FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final String TAG = "SIGNUP";

    public SignUpPresenter(){
        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
    }

    public void createUser(String name, String email, String password, SignupActivity signupActivity){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        mUser = mAuth.getCurrentUser();
                        signupActivity.signInNewUser(name, email);
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        //updateUI(null);
                    }
                });
    }

    public void addNewUser(String name, String email, ObservableEmitter<Boolean> emitter){
        User newUser = new User(name, email);

        String userID = mUser.getUid();

        if(userID != null) {
            firestoreDB.collection("users")
                    .document(userID)
                    .set(newUser)
                    .addOnSuccessListener(documentReference -> emitter.onNext(true))
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding new user to database", e);
                    });
        }
        else{
            Log.d(TAG, "User id was null could not add user");
        }
    }
}
