package com.solaredge.diagnostictool;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

    private MediaPlayer mp;
    private EditText number;
    private TextView emcall;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_giroscopio);
        switchMaterial = findViewById(R.id.switchMaterial);

        mp = MediaPlayer.create(this, R.raw.alarm);
        number = findViewById(R.id.editTextPhone);
        emcall = findViewById(R.id.call);
        textView = findViewById(R.id.textView);

        number.setVisibility(View.INVISIBLE);
        emcall.setVisibility(View.INVISIBLE);
        if (!MyService.isServiceIsRunning()){
            startService(new Intent(TestGiroscopio.this, MyService.class));
        }

        if (MyService.isLifeGuard()){
            Handler handler = new Handler();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if(MyService.isLifeGuard()){
                                textView.setText(String.valueOf(MyService.getCountdown()));
                                if (MyService.getCountdown() == 0) {
                                    createMessage(getApplicationContext());
                                    MyService.stopLifeGuard();
                                    switchMaterial.setChecked(false);
                                }
                            }else {
                                textView.setText("LifeGuard");
                            }
                        }
                    });
                }
            }, 0, 1000);
            switchMaterial.setChecked(true);
        }else{
            switchMaterial.setChecked(false);
        }

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    number.setVisibility(View.VISIBLE);
                    emcall.setVisibility(View.VISIBLE);

                    MyService.startLifeGuard();
                    Handler handler = new Handler();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    if(MyService.isLifeGuard()){
                                        textView.setText(String.valueOf(MyService.getCountdown()));
                                        if (MyService.getCountdown() == 0) {
                                            createMessage(getApplicationContext());
                                            MyService.stopLifeGuard();
                                            switchMaterial.setChecked(false);
                                        }
                                    }else {
                                        textView.setText("LifeGuard");
                                    }
                                }
                            });
                        }
                    }, 0, 1000);
                } else {
                    MyService.stopLifeGuard();
                    textView.setText("LifeGuard");
                    number.setVisibility(View.INVISIBLE);
                    emcall.setVisibility(View.INVISIBLE);
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

    public void createMessage(Context context) {
        Thread check10sec = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(10000);
                    Log.e("YOO", "L'utente non ha risposto");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        check10sec.start();
        AlertDialog alertDialog = new AlertDialog.Builder(TestGiroscopio.this).create();
        alertDialog.setTitle("LifeGuard");
        alertDialog.setMessage("Are u ok?");
        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check10sec.interrupt();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check10sec.interrupt();
                dialog.dismiss();
            }
        });
        alertDialog.show();
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
    public void DANGER(){
        mp.start();
    }
}