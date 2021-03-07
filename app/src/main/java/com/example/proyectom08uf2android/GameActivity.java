package com.example.proyectom08uf2android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String TAG = "GameActivity";
    private boolean isPlaying = true;
    private Intent intentAudio;

    Button button;
    Button button2;
    Button button3;
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView iv_roulette;
    ImageView pointer;
    Spinner spinner;
    TextView tvWelcomeUser;
    CheckBox cbVictoria;

    private User user;

    private FirebaseAuth faAuth;
    private DatabaseReference drDatabase;
    private String id;

    Random r;
    int degree = 0, degree_old = 0;

    Animation animFadein;
    Animation animBounce;
    Animation animRotate;

    private static final float FACTOR = 4.86f;

    private String apuesta;
    private int dineroApostado;
    private boolean resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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


        button = (Button) findViewById(R.id.button2);
        button2 = (Button) findViewById(R.id.btMinus);
        button3 = (Button) findViewById(R.id.btPlus);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        iv_roulette = (ImageView) findViewById(R.id.roulette);
        pointer = (ImageView) findViewById(R.id.pointer);
        spinner = (Spinner) findViewById(R.id.spOptions);
        tvWelcomeUser = (TextView) findViewById(R.id.tvWelcomeUser);
        cbVictoria = (CheckBox) findViewById(R.id.checkBox);

        faAuth = FirebaseAuth.getInstance();
        drDatabase = FirebaseDatabase.getInstance().getReference();

        id = faAuth.getCurrentUser().getUid();
        getUserInfo();

        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        Path path = new Path();
        path.moveTo(150, 0);
        path.lineTo(180, 100);
        path.lineTo(150, 80);
        path.lineTo(120, 100);
        path.close();
        canvas.drawPath(path, paint);
        pointer.setImageBitmap(bitmap);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);

        iv_roulette.startAnimation(animBounce);
        pointer.startAnimation(animBounce);

        button.startAnimation(animRotate);
        button2.startAnimation(animRotate);
        button3.startAnimation(animRotate);

        textView.startAnimation(animFadein);
        textView2.startAnimation(animFadein);
        textView3.startAnimation(animFadein);

        intentAudio = new Intent(this, AudioIntentService.class);

        textView.setText("CLICK \"SPIN\" TO PLAY");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        r = new Random();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degree_old = degree % 360;
                degree = r.nextInt(3600) + 720;
                RotateAnimation rotate = new RotateAnimation(degree_old, degree,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(3600);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        intentAudio.putExtra("operacio", "ball");
                        startService(intentAudio);
                        spinner.setEnabled(false);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.setText(currentNumber(360 - (degree % 360)));
                        if(cbVictoria.isChecked()){
                            resultado = true;
                        } else {
                            resultado = apuesta.equals(textView.getText().toString());
                        }
                        dineroApostado = Integer.parseInt(textView2.getText().toString());
                        if (resultado) {
                            intentAudio.putExtra("operacio", "win");
                            startService(intentAudio);
                            user.setMoney((int) (user.getMoney() + (dineroApostado * 1.3)));
                        } else {
                            intentAudio.putExtra("operacio", "loss");
                            startService(intentAudio);
                            user.setMoney(user.getMoney() - dineroApostado);
                        }
                        textView2.setText("0");

                        spinner.setEnabled(true);
                        button2.setEnabled(true);
                        button3.setEnabled(true);

                        actualizarDinero();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv_roulette.startAnimation(rotate);
            }
        });

        button3.setOnClickListener(this::betPlus);
        button2.setOnClickListener(this::betMinus);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apuesta = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getUserInfo() {
        drDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                int money = Integer.parseInt(dataSnapshot.child("money").getValue().toString());

                user = new User(name, money);
                tvWelcomeUser.setText("Welcome "+name);
                actualizarDinero();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserInfo() {
        drDatabase.child("Users").child(id).child("money").setValue(user.getMoney());
    }


    private void betMinus(View view) {
        int newApuesta = Integer.parseInt(textView2.getText().toString()) - 10;
        if (newApuesta < 0) {
            textView2.setText("0");
        } else {
            textView2.setText(String.valueOf(newApuesta));
        }
    }

    private void betPlus(View view) {
        int newApuesta = Integer.parseInt(textView2.getText().toString()) + 10;
        if (newApuesta <= user.getMoney()) {
            textView2.setText(String.valueOf(newApuesta));
        }
    }

    private void actualizarDinero() {
        textView3.setText(String.valueOf(user.getMoney()));
        setUserInfo();
    }

    private String currentNumber(int degrees) {
        String text = "";

        if (degrees >= (FACTOR * 1) && degrees < (FACTOR * 3)) {
            text = "32 RED";
        }
        if (degrees >= (FACTOR * 3) && degrees < (FACTOR * 5)) {
            text = "15 BLACK";
        }
        if (degrees >= (FACTOR * 5) && degrees < (FACTOR * 7)) {
            text = "19 RED";
        }
        if (degrees >= (FACTOR * 7) && degrees < (FACTOR * 9)) {
            text = "4 BLACK";
        }
        if (degrees >= (FACTOR * 9) && degrees < (FACTOR * 11)) {
            text = "21 RED";
        }
        if (degrees >= (FACTOR * 11) && degrees < (FACTOR * 13)) {
            text = "2 BLACK";
        }
        if (degrees >= (FACTOR * 13) && degrees < (FACTOR * 15)) {
            text = "25 RED";
        }
        if (degrees >= (FACTOR * 15) && degrees < (FACTOR * 17)) {
            text = "17 BLACK";
        }
        if (degrees >= (FACTOR * 17) && degrees < (FACTOR * 19)) {
            text = "34 RED";
        }
        if (degrees >= (FACTOR * 19) && degrees < (FACTOR * 21)) {
            text = "6 BLACK";
        }
        if (degrees >= (FACTOR * 21) && degrees < (FACTOR * 23)) {
            text = "27 RED";
        }
        if (degrees >= (FACTOR * 23) && degrees < (FACTOR * 25)) {
            text = "13 BLACK";
        }
        if (degrees >= (FACTOR * 25) && degrees < (FACTOR * 27)) {
            text = "36 RED";
        }
        if (degrees >= (FACTOR * 27) && degrees < (FACTOR * 29)) {
            text = "11 BLACK";
        }
        if (degrees >= (FACTOR * 29) && degrees < (FACTOR * 31)) {
            text = "30 RED";
        }
        if (degrees >= (FACTOR * 31) && degrees < (FACTOR * 33)) {
            text = "8 BLACK";
        }
        if (degrees >= (FACTOR * 33) && degrees < (FACTOR * 35)) {
            text = "23 RED";
        }
        if (degrees >= (FACTOR * 35) && degrees < (FACTOR * 37)) {
            text = "10 BLACK";
        }
        if (degrees >= (FACTOR * 37) && degrees < (FACTOR * 39)) {
            text = "5 RED";
        }
        if (degrees >= (FACTOR * 39) && degrees < (FACTOR * 41)) {
            text = "24 BLACK";
        }
        if (degrees >= (FACTOR * 41) && degrees < (FACTOR * 43)) {
            text = "16 RED";
        }
        if (degrees >= (FACTOR * 43) && degrees < (FACTOR * 45)) {
            text = "33 BLACK";
        }
        if (degrees >= (FACTOR * 45) && degrees < (FACTOR * 47)) {
            text = "1 RED";
        }
        if (degrees >= (FACTOR * 47) && degrees < (FACTOR * 49)) {
            text = "20 BLACK";
        }
        if (degrees >= (FACTOR * 49) && degrees < (FACTOR * 51)) {
            text = "14 RED";
        }
        if (degrees >= (FACTOR * 51) && degrees < (FACTOR * 53)) {
            text = "31 BLACK";
        }
        if (degrees >= (FACTOR * 53) && degrees < (FACTOR * 55)) {
            text = "9 RED";
        }
        if (degrees >= (FACTOR * 55) && degrees < (FACTOR * 57)) {
            text = "22 BLACK";
        }
        if (degrees >= (FACTOR * 57) && degrees < (FACTOR * 59)) {
            text = "18 RED";
        }
        if (degrees >= (FACTOR * 59) && degrees < (FACTOR * 61)) {
            text = "29 BLACK";
        }
        if (degrees >= (FACTOR * 61) && degrees < (FACTOR * 63)) {
            text = "7 RED";
        }
        if (degrees >= (FACTOR * 63) && degrees < (FACTOR * 65)) {
            text = "28 BLACK";
        }
        if (degrees >= (FACTOR * 65) && degrees < (FACTOR * 67)) {
            text = "12 RED";
        }
        if (degrees >= (FACTOR * 67) && degrees < (FACTOR * 69)) {
            text = "35 BLACK";
        }
        if (degrees >= (FACTOR * 69) && degrees < (FACTOR * 71)) {
            text = "3 RED";
        }
        if (degrees >= (FACTOR * 71) && degrees < (FACTOR * 73)) {
            text = "26 BLACK";
        }
        if ((degrees >= (FACTOR * 73) && degrees < 360) || (degrees >= 0 && degrees < (FACTOR * 1))) {
            text = "0";
        }

        return text;
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
                intentAudio.putExtra("operacio", "pausa");
                faAuth.signOut();
                startActivity(new Intent(GameActivity.this, RegisterActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}