package com.example.eventplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.MainActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.presenter.FirebaseHandler;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginScreen";
    private static final int REQUEST_SIGNUP = 0;
    FirebaseHandler firebaseHandler;

    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input)
    EditText passwordInput;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.signup_link)
    TextView signupText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseHandler = new FirebaseHandler();

        loginButton.setOnClickListener(view -> authorize());

        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseHandler = new FirebaseHandler();
    }

    public void authorize() {
        if(emailInput.getText().toString().trim().isEmpty())
            Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show();
        else if(passwordInput.getText().toString().trim().isEmpty())
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        else
            firebaseHandler.checkAuthentication(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim(), this); // calls login() when completed
    }

    public void login(boolean authorized){
        if(authorized) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Please make sure a valid email and password is entered", Toast.LENGTH_LONG).show();
    }

}
