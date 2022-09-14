package com.solaredge.diagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class TestTermometro extends AppCompatActivity {

    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;
    private SensorManager mgr;
    private Sensor temp;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test_termometro);
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        text = (TextView) findViewById(R.id.temperatura);


        extendedFab = findViewById(R.id.extended_fab);
        extendedFab.shrink();
        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendedFab.extend();
                click++;
                if (click == 2) {
                    goToHome();
                }
            }
        });
        text.setText((int) temp.getPower());
    }


    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            text.setText((int) event.values[0] + "°C");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    };


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