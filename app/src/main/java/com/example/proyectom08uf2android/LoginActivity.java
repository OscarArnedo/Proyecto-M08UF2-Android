package com.example.proyectom08uf2android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private EditText etEmail;
    private EditText etPwd;

    User user = null;

    FirebaseAuth faAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        faAuth = FirebaseAuth.getInstance();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPwd = (EditText) findViewById(R.id.etPwd);

    }

    public void logIn(View view) {
        user = new User(etPwd.getText().toString(), etEmail.getText().toString());

        if (!user.getEmail().isEmpty() && !user.getPasswd().isEmpty()) {
            logInUser();
        } else {
            Toast.makeText(LoginActivity.this, "You must complete all the fields", Toast.LENGTH_LONG).show();
        }
    }

    public void logInUser() {
        faAuth.signInWithEmailAndPassword(user.getEmail(), user.getPasswd()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Can't log in. Check user and pwd", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goBack(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}