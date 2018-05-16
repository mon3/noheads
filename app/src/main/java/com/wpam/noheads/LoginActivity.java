package com.wpam.noheads;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wpam.noheads.Model.User;


import com.wpam.noheads.wifi.UserListActivity;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.wpam.noheads.Util.getCurrentUserId;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = "StartActivity";
    private boolean logginIn;

    EditText inputEmail;
    EditText inputName;
    Button loginButton;
    EditText inputPassword;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = findViewById(R.id.input_email);
        inputName = findViewById(R.id.input_name);
        inputPassword = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress);
    }

//    public void startSingleMode(View view) {
//        startActivity(new Intent(this, MainActivity.class)
//                .putExtra("type", "single"));
//    }

    public void startMultilayer(View view) {
        if (!arePlayServicesOk()) {
            return;
        }
        if (isAnonymous()) {
            inputEmail.setVisibility(VISIBLE);
            inputName.setVisibility(VISIBLE);
            loginButton.setVisibility(VISIBLE);
            inputPassword.setVisibility(VISIBLE);
        } else {
//            startActivity(new Intent(this, MainActivity.class)
//                    .putExtra("type", "com.wpam.noheads.wifi"));

            startActivity(new Intent(this, UserListActivity.class));
        }
    }

    public void loginWithEmail(View view) {
        String email =inputEmail.getText().toString();
        String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();
        if (logginIn) {
            return;
        }
        if (isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter correct email", LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(name)) {
            Toast.makeText(this, "Enter correct name", LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password should have at least 6 characters", LENGTH_SHORT).show();
            return;
        }

        logginIn = true;
        showProgressDialog();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "loginWithEmail: ");
                            String uid = auth.getCurrentUser().getUid();

                            User user = new User(name);
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                    .setValue(user);

                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            savePushToken(refreshedToken, uid);

//                        startActivity(new Intent(this, MainActivity.class)
//                                .putExtra("type", "com.wpam.noheads.wifi"));

                            startActivity(new Intent(LoginActivity.this, UserListActivity.class));

                        } else {
                            Log.d(LOG_TAG, "loginWithEmail: unsuccessful");
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task1 -> {
                                        if (!isAnonymous()) {
                                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                                            savePushToken(refreshedToken, getCurrentUserId());

//                                        startActivity(new Intent(this, MainActivity.class)
//                                                .putExtra("type", "com.wpam.noheads.wifi"));

                                            startActivity(new Intent(LoginActivity.this, UserListActivity.class));
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
//                                       ToDO: update UI -> set to NULL (refresh!)

                                        }
                                    });
                        }
                        hideProgressDialog();
                    }
                });

    }


    private void savePushToken(String refreshedToken, String uid) {
    }

    private void showProgressDialog() {
        progressBar.setVisibility(VISIBLE);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(GONE);
    }

    private boolean isAnonymous() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null || currentUser.isAnonymous();
    }

    private boolean arePlayServicesOk() {
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int resultCode = googleAPI.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode, 5000).show();
            }
            return false;
        }

        return true;
    }
}
