package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sda5.walletdroid.R;
import com.sda5.walletdroid.models.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText etEmail;
    EditText etDisplayName;
    EditText etPassword;
    FirebaseFirestore database;
    String userId;
    String email;
    String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.txt_signUp_email);
        etDisplayName = findViewById(R.id.txt_signUp_displayName);
        etPassword = findViewById(R.id.txt_signUp_password);
    }

    public void signUp(View view) {

        //Check if user fills all fields
        if (etEmail.getText().toString().trim().isEmpty() ||
                etPassword.getText().toString().trim().isEmpty() ||
                etDisplayName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            final String email = etEmail.getText().toString();
            final String displayName = etDisplayName.getText().toString();
            final String password = etPassword.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                updateUserDisplayName(user, displayName, password);

                            } else {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void postSignUpLogin(final String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserDisplayName(final FirebaseUser user, final String displayName, final String password) {
        userId = user.getUid();
        email = user.getEmail();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Account userAccount = new Account(true, displayName, email, idToken);
                            userAccount.setUserID(mAuth.getCurrentUser().getUid());
                            database.collection("Accounts").document().set(userAccount);

                            postSignUpLogin(user.getEmail(), password);
                            startActivity(new Intent(SignUpActivity.this, ServiceActivity.class));
                        }
                    }
                });
    }
}
