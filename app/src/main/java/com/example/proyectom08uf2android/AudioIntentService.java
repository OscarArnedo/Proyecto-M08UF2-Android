package com.example.proyectom08uf2android;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;

public class AudioIntentService extends IntentService {

    private MediaPlayer mp;
    private MediaPlayer win;
    private MediaPlayer loss;
    private MediaPlayer ball;

    public AudioIntentService() {
        super("serveiAudio");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.musica_casino);
        ball = MediaPlayer.create(this, R.raw.ball);
        win = MediaPlayer.create(this, R.raw.win);
        loss = MediaPlayer.create(this, R.raw.loss);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            String operacio = intent.getStringExtra("operacio");
            switch (operacio) {
                case "inici":
                    mp.start();
                    break;
                case "pausa":
                    mp.pause();
                    break;
                case "salta":
                    mp.seekTo(10000);
                    break;
                case "ball":
                    ball.start();
                    break;
                case "win":
                    win.start();
                    break;
                case "loss":
                    loss.start();
                    break;
                default:
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
