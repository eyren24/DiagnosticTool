package com.solaredge.diagnostictool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class History extends AppCompatActivity {

    private TextView sqlInfo;
    private int click = 0;
    private ExtendedFloatingActionButton extendedFab;
    private ListView listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sqlInfo = findViewById(R.id.sqlInfo);
        listItem = findViewById(R.id.list_item);

        sqlInfo.setText("Connecting to database . . .");
        DBHandler dbHandler = new DBHandler(getApplicationContext());

        sqlInfo.setText("Getting data . . .");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbHandler.getAllAlert());

        sqlInfo.setText("Display data . . .");
        listItem.setAdapter(arrayAdapter);

        extendedFab = findViewById(R.id.extended_fab);
        extendedFab.shrink();
        extendedFab.setOnClickListener(view -> {
            extendedFab.extend();
            click++;
            if (click == 1) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        goToHome();
                    }
                }, 500);

            }
        });
    }


    private void goToHome() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onBackPressed() {
        goToHome();
        finish();
        //super.onBackPressed();
    }
}