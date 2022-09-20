package com.solaredge.diagnostictool;

import static java.lang.Thread.sleep;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Timer;
import java.util.TimerTask;

public class TestGiroscopio extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;
    private SwitchMaterial switchMaterial;
    private Handler handler;
    private TimerTask timertask;
    private int alert = 60;
    protected double lastValue = 0;
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_giroscopio);

        switchMaterial = findViewById(R.id.switchMaterial);

        Timer timer = new Timer();
        textView = findViewById(R.id.textView);
        // create handler
        handler = new Handler();
        // create timeTask
        timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.e("TEST", String.valueOf(lastValue));
                        if (lastValue <= 10 && lastValue >= 8){
                            Log.e("TEST", "ALERT ++");
                            if (alert == 0) return;
                            alert --;
                        }else{
                            alert = 60;
                        }
                        if (alert == 0){
                            switchMaterial.setChecked(false);
                            Log.e("TEST", "ALERT == 3");
                            Intent intent = new Intent("com.solaredge.diagnostictool");
                            intent.putExtra("value", String.valueOf(lastValue));
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            alert = 0;
                        }
                    }
                });
            }
        };

        timer.schedule(timertask, 0, 1000);

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

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    startService(new Intent(TestGiroscopio.this, MyService.class));
                    Log.e("TEST", "STARTING SERVICE");
                }else {
                    stopService(new Intent(TestGiroscopio.this, MyService.class));
                    Log.e("TEST", "STOP SERVICE");
                }
            }
        });
    }

    public Dialog messageBox(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("YO OK?");
            builder.setMessage("Are u alive grandFather?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just
                // want it dismissed when clicked
            }
        });
        builder.setNegativeButton("No, Call emergency!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static void yolo(String text){
        textView.setText(text);
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