package com.example.proyectom08uf2android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {
        Intent intent = new Intent(getApplicationContext(),
                GameActivity.class);
        startActivity(intent);
    }

    public void ranking(View view) {
    }

    public void exit(View view) {
    }
}