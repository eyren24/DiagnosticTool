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
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView temperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test_termometro);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        temperature = (TextView) findViewById(R.id.temperatura);

        if (sensor == null) {
            temperature.setText(NOT_SUPPORTED_MESSAGE);
            temperature.setTextSize(30);
        }else{
            temperature.setText((int) sensor.getPower());
        }

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
    }


    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            temperature.setText((int) event.values[0] + "°C");
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