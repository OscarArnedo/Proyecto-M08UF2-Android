package com.example.proyectom08uf2android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        lv = (ListView) findViewById(R.id.list);
        userList = new ArrayList<User>();
        ranking = new ArrayList<String>();

        faAuth = FirebaseAuth.getInstance();
        drDatabase = FirebaseDatabase.getInstance().getReference();

        getUsers();
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