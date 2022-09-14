package com.solaredge.diagnostictool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
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

public class TestSensoreDiLuminosita extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView lightLevel , title, desc;
    private View someView;

    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test_sensore_di_luminosita);
        lightLevel = (TextView) findViewById(R.id.lumus);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

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
        if (sensor == null){
            ((TextView) findViewById(R.id.lumus)).setText("Sorry, sensor not available for this device.");
            return;
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            title = findViewById(R.id.lumusdetector);
            desc = findViewById(R.id.descrizione);
            someView = findViewById(R.id.screen);
            View root = someView.getRootView();
            int value = (int) event.values[0];
            if(value>=10000){
                lightLevel.setTextSize(50);
            }
            if(value>3500){
                root.setBackgroundColor(Color.parseColor("#ffffff"));
                title.setTextColor(Color.parseColor("#000000"));
                lightLevel.setTextColor(Color.parseColor("#000000"));
                desc.setTextColor(Color.parseColor("#000000"));
            }else{
                root.setBackgroundColor(Color.parseColor("#000000"));
                title.setTextColor(Color.parseColor("#ffffff"));
                lightLevel.setTextColor(Color.parseColor("#ffffff"));
                desc.setTextColor(Color.parseColor("#ffffff"));
            }
            lightLevel.setText(value + " lx");

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