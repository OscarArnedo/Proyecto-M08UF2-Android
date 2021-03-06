package com.example.proyectom08uf2android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPwd;
    private Button btSignUp;

    User user = null;

    FirebaseAuth faAuth;
    DatabaseReference drDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        faAuth = FirebaseAuth.getInstance();
        drDataBase = FirebaseDatabase.getInstance().getReference();

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPwd = (EditText) findViewById(R.id.etPwd);

        btSignUp = (Button) findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = new User(etName.getText().toString(), etPwd.getText().toString(), etEmail.getText().toString());

                if(!user.getName().isEmpty() && !user.getEmail().isEmpty() && !user.getPasswd().isEmpty()) {
                    if(user.getPasswd().length() >= 6) {
                        registerUser();
                    } else {
                        Toast.makeText(LoginActivity.this, "The password must have at least 6 characters", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "You must complete all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void registerUser(){
        faAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPasswd()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", user.getName());
                    map.put("email", user.getEmail());
                    map.put("pwd", user.getPasswd());;
                    map.put("money", user.getMoney());


                    String id = faAuth.getCurrentUser().getUid();

                    drDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "The data couldn't be created correctly", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Couldn't register this user", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}