package com.example.eventplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.MainActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.presenter.FirebaseHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginScreen";
    private static final int REQUEST_SIGNUP = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseHandler firebaseHandler;
    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input)
    EditText passwordInput;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.signup_link)
    TextView signupText;
    @BindView(R.id.google_signIn_button)
    Button google_SignIn_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseHandler = new FirebaseHandler();
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(view -> authorize());
        google_SignIn_button.setOnClickListener(view -> pickGoogleAccount());
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //FirebaseAuth.getInstance().signOut();
        firebaseHandler = new FirebaseHandler();
    }

    public void authorize() {
        if(emailInput.getText().toString().trim().isEmpty())
            Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show();
        else if(passwordInput.getText().toString().trim().isEmpty())
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        else
            firebaseHandler.checkAuthentication(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim(), this); // calls defaultLogin() when completed
    }

    public void defaultLogin(boolean authorized){
        if(authorized) {
            checkUser();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Please make sure a valid email and password is entered", Toast.LENGTH_LONG).show();
    }

    public void googleLogin(boolean authorized){

        if(authorized) {
            checkUser();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Error connecting your Google Account", Toast.LENGTH_LONG).show();
    }

    private void pickGoogleAccount() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseHandler.firebaseAuthWithGoogle(account, this);
                mGoogleSignInClient.signOut();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    public void checkUser(){
        Toast.makeText(this, "Current User is " +  FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
    }
}
