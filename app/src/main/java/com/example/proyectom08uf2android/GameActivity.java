package com.example.proyectom08uf2android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ImageView iv_roulette;
    Spinner spinner;

    Random r;
    int degree = 0, degree_old = 0;

    private static final float FACTOR = 4.86f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        button = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        iv_roulette = (ImageView) findViewById(R.id.roulette);
        spinner = (Spinner) findViewById(R.id.spOptions);

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

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.setText(currentNumber(360 - (degree % 360)));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv_roulette.startAnimation(rotate);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String currentNumber(int degrees){
        String text = "";

        if(degrees >= (FACTOR * 1) && degrees < (FACTOR * 3)){
            text = "32 RED";
        }
        if(degrees >= (FACTOR * 3) && degrees < (FACTOR * 5)){
            text = "15 BLACK";
        }
        if(degrees >= (FACTOR * 5) && degrees < (FACTOR * 7)){
            text = "19 RED";
        }
        if(degrees >= (FACTOR * 7) && degrees < (FACTOR * 9)){
            text = "4 BLACK";
        }
        if(degrees >= (FACTOR * 9) && degrees < (FACTOR * 11)){
            text = "21 RED";
        }
        if(degrees >= (FACTOR * 11) && degrees < (FACTOR * 13)){
            text = "2 BLACK";
        }
        if(degrees >= (FACTOR * 13) && degrees < (FACTOR * 15)){
            text = "25 RED";
        }
        if(degrees >= (FACTOR * 15) && degrees < (FACTOR * 17)){
            text = "17 BLACK";
        }
        if(degrees >= (FACTOR * 17) && degrees < (FACTOR * 19)){
            text = "34 RED";
        }
        if(degrees >= (FACTOR * 19) && degrees < (FACTOR * 21)){
            text = "6 BLACK";
        }
        if(degrees >= (FACTOR * 21) && degrees < (FACTOR * 23)){
            text = "27 RED";
        }
        if(degrees >= (FACTOR * 23) && degrees < (FACTOR * 25)){
            text = "13 BLACK";
        }
        if(degrees >= (FACTOR * 25) && degrees < (FACTOR * 27)){
            text = "36 RED";
        }
        if(degrees >= (FACTOR * 27) && degrees < (FACTOR * 29)){
            text = "11 BLACK";
        }
        if(degrees >= (FACTOR * 29) && degrees < (FACTOR * 31)){
            text = "30 RED";
        }
        if(degrees >= (FACTOR * 31) && degrees < (FACTOR * 33)){
            text = "8 BLACK";
        }
        if(degrees >= (FACTOR * 33) && degrees < (FACTOR * 35)){
            text = "23 RED";
        }
        if(degrees >= (FACTOR * 35) && degrees < (FACTOR * 37)){
            text = "10 BLACK";
        }
        if(degrees >= (FACTOR * 37) && degrees < (FACTOR * 39)){
            text = "5 RED";
        }
        if(degrees >= (FACTOR * 39) && degrees < (FACTOR * 41)){
            text = "24 BLACK";
        }
        if(degrees >= (FACTOR * 41) && degrees < (FACTOR * 43)){
            text = "16 RED";
        }
        if(degrees >= (FACTOR * 43) && degrees < (FACTOR * 45)){
            text = "33 BLACK";
        }
        if(degrees >= (FACTOR * 45) && degrees < (FACTOR * 47)){
            text = "1 RED";
        }
        if(degrees >= (FACTOR * 47) && degrees < (FACTOR * 49)){
            text = "20 BLACK";
        }
        if(degrees >= (FACTOR * 49) && degrees < (FACTOR * 51)){
            text = "14 RED";
        }
        if(degrees >= (FACTOR * 51) && degrees < (FACTOR * 53)){
            text = "31 BLACK";
        }
        if(degrees >= (FACTOR * 53) && degrees < (FACTOR * 55)){
            text = "9 RED";
        }
        if(degrees >= (FACTOR * 55) && degrees < (FACTOR * 57)){
            text = "22 BLACK";
        }
        if(degrees >= (FACTOR * 57) && degrees < (FACTOR * 59)){
            text = "18 RED";
        }
        if(degrees >= (FACTOR * 59) && degrees < (FACTOR * 61)){
            text = "29 BLACK";
        }
        if(degrees >= (FACTOR * 61) && degrees < (FACTOR * 63)){
            text = "7 RED";
        }
        if(degrees >= (FACTOR * 63) && degrees < (FACTOR * 65)){
            text = "28 BLACK";
        }
        if(degrees >= (FACTOR * 65) && degrees < (FACTOR * 67)){
            text = "12 RED";
        }
        if(degrees >= (FACTOR * 67) && degrees < (FACTOR * 69)){
            text = "35 BLACK";
        }
        if(degrees >= (FACTOR * 69) && degrees < (FACTOR * 71)){
            text = "3 RED";
        }
        if(degrees >= (FACTOR * 71) && degrees < (FACTOR * 73)){
            text = "26 BLACK";
        }
        if((degrees >= (FACTOR * 73) && degrees < 360) || (degrees >= 0 && degrees < (FACTOR * 1))){
            text = "0";
        }

        return text;
    }
}