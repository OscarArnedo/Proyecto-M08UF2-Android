package com.example.proyectom08uf2android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    private boolean isPlaying = true;
    private Intent intentAudio;

    FirebaseAuth faAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase database
        faAuth = FirebaseAuth.getInstance();

        //Audio service
        intentAudio = new Intent(this, AudioIntentService.class);
        intentAudio.putExtra("operacio", "inici");
        startService(intentAudio);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Log.d(TAG, "Incoming call: Pause music");
                    intentAudio.putExtra("operacio", "pausa");
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Log.d(TAG, "Not in call: Play music");
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Log.d(TAG, "A call is dialing, active or on hold");
                    intentAudio.putExtra("operacio", "pausa");
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }


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

    public void signOut(View view) {
        faAuth.signOut();
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
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
                    intentAudio.putExtra("operacio", "inici");
                } else {
                    isPlaying = !isPlaying;
                    text = "PLAY MUSIC";
                    item.setTitle(text);
                    intentAudio.putExtra("operacio", "pausa");
                }
                startService(intentAudio);
                return true;
            case R.id.closeSession:
                faAuth.signOut();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        intentAudio.putExtra("operacio", "pausa");
    }

    @Override
    protected void onPause() {
        // call the superclass method first
        super.onPause();
        intentAudio.putExtra("operacio", "pausa");
    }

}

