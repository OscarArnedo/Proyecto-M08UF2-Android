package com.example.proyectom08uf2android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private boolean isPlaying = true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, AudioIntentService.class);
        intent.putExtra("operacio", "inici");
        startService(intent);

    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Función para añadir el menú definido en menu_principal.xml al action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu_principal, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.playStopMusic:
                String text;

                if (!isPlaying) {
                    isPlaying = !isPlaying;
                    text = "PAUSE MUSIC";
                    item.setTitle(text);
                    intent.putExtra("operacio", "inici");
                } else {
                    isPlaying = !isPlaying;
                    text = "PLAY MUSIC";
                    item.setTitle(text);
                    intent.putExtra("operacio", "pausa");
                }
                startService(intent);
                return true;
            /*case R.id.:
                exportar();
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

