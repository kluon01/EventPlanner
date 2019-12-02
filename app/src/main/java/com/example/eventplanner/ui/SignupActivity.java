package com.example.eventplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.presenter.firebase.SignUpPresenter;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
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

    private SignUpPresenter signUpPresenter;
    private CompositeDisposable mycompositeDisposable = new CompositeDisposable();
    private DisposableObserver<Boolean> mydisposableObserver;
    private String TAG = "SignupActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        signUpPresenter = new SignUpPresenter();

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
            signUpPresenter.createUser(nameText, emailText, passwordText, this);
        }
    }

    public void signInNewUser(String name, String email) {
        Observable<Boolean> observable;
        observable = Observable.create(emitter -> {
            try {
                signUpPresenter.addNewUser(name, email, emitter);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        mycompositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getObserver()));
    }

    public DisposableObserver<Boolean> getObserver() {
        return mydisposableObserver = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean result) {
                // TODO : run check user for a quick test (for debugging)
                //checkUser();
                // take user back to login screen
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "Error adding user to database", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mycompositeDisposable.clear();
    }

    public void checkUser() {
        Toast.makeText(this, "Current User is " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
    }
}
