package com.solaredge.diagnostictool;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Timer;
import java.util.TimerTask;

public class TestGiroscopio extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;
    private SwitchMaterial switchMaterial;

    protected static double lastValue = 0;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_giroscopio);
        switchMaterial = findViewById(R.id.switchMaterial);
        switchMaterial.setChecked(false);
        textView = findViewById(R.id.textView);

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(TestGiroscopio.this, MyService.class));
                    Handler handler = new Handler();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    textView.setText(MyService.getCountdown());
                                    if(MyService.getCountdown() == 0){
                                        Toast.makeText(getApplicationContext(), "Uploading result on db", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }, 0, 1000);
                } else {
                    stopService(new Intent(TestGiroscopio.this, MyService.class));
                    Toast.makeText(getApplicationContext(), "Stopping service", Toast.LENGTH_LONG).show();
                    textView.setText(null);
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            Sensor acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (acceleroSensor != null) {
                sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                ((TextView) findViewById(R.id.accelerometer)).setText(String.valueOf("Sorry, sensor not available for this device."));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sensor service not detected.", Toast.LENGTH_LONG).show();
        }

        extendedFab = findViewById(R.id.extended_fab);
        extendedFab.shrink();
        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    public static double getLastValue() {
        return TestGiroscopio.lastValue;
    }

    @Override
    public void onBackPressed() {
        goToHome();
        finish();
        //super.onBackPressed();
    }

    private void goToHome() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double X = (double) Math.round(sensorEvent.values[0] * 10d) / 10d;
            double Y = (double) Math.round(sensorEvent.values[1] * 10d) / 10d;
            double Z = (double) Math.round(sensorEvent.values[2] * 10d) / 10d;
            ((TextView) findViewById(R.id.accelerometer)).setText(String.valueOf("X: " + X + ", Y: " + Y + ", Z: " + Z));
            ((TextView) findViewById(R.id.label)).setText(String.valueOf("if you see the value change, it works!"));
            lastValue = Z;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}