package com.sda5.walletdroid.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.sda5.walletdroid.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {

        EditText email = findViewById(R.id.txt_login_email);
        EditText password = findViewById(R.id.txt_login_password);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, ServiceActivity.class));

                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToSignUpPage(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void resetPasswordViaEmail(View view) {
        String email = ((EditText) findViewById(R.id.txt_login_email)).getText().toString();
        if (!email.equals("")) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password reset email has sent your mail. Check your email!", Toast.LENGTH_SHORT).show();

                            } else {
                                // ...
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();

        }
    }
}
