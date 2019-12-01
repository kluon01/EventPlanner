package com.example.eventplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.MainActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.presenter.FirebaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText emailInput;
    @BindView(R.id.input_password)
    EditText passwordInput;
    @BindView(R.id.signup_button)
    Button signupButton;
    @BindView(R.id.link_login)
    TextView loginLink;
    @BindView(R.id.input_name)
    EditText nameInput;

    private FirebaseHandler firebaseHandler;
    private String TAG = "SignupActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        firebaseHandler = new FirebaseHandler();

        signupButton.setOnClickListener(view -> signup());

        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

    }

    public void signup() {
        String emailText = emailInput.getText().toString();
        String passwordText = passwordInput.getText().toString();
        String nameText = nameInput.getText().toString();

        if (!emailText.trim().isEmpty() || !passwordText.trim().isEmpty() || !nameText.trim().isEmpty()) {
            //TODO: Send user verification email, check password strength, check for profanity
            firebaseHandler.createUser(emailText, passwordText, this);
        }
    }

    public void signInNewUser(String name, String email) {
        // TODO: Finish makes user database stuff (use rxjava), then login user
        Observable<String> observable;
        // First add new user to user database, for future use
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        firestoreDB.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {

                        }
                )
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Could not create user info", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error writing document", e);
                });
    }
}
