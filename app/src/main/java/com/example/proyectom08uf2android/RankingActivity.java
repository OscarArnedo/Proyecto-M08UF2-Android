package com.example.proyectom08uf2android;

import androidx.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private String TAG = "RankingActivity";

    private ListView lv;
    private ArrayAdapter<String> adapter;

    private List<User> userList;
    private List<String> ranking;

    private FirebaseAuth faAuth;
    private DatabaseReference drDatabase;

    private Intent intentAudio;
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.list);
        userList = new ArrayList<User>();
        ranking = new ArrayList<String>();

        faAuth = FirebaseAuth.getInstance();
        drDatabase = FirebaseDatabase.getInstance().getReference();

        getUsers();

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
                startActivity(new Intent(RankingActivity.this, RegisterActivity.class));
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

    private void getUsers(){
        drDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    int money = Integer.parseInt(ds.child("money").getValue().toString());

                    userList.add(new User(name, money));
                }
                doRanking();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doRanking(){
        Collections.sort(userList);
        for(int i = 0; i < userList.size(); i++){
            if (i <= 5) {
                ranking.add(i+1+". "+userList.get(i).toStringRanking());
            }
        }
        adapter = new ArrayAdapter<String>(RankingActivity.this, android.R.layout.simple_list_item_1, ranking);
        lv.setAdapter(adapter);
    }
}