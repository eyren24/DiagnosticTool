package com.solaredge.diagnostictool;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class TestGiroscopio extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;
    private SwitchMaterial switchMaterial;
    protected static double lastValue = 0;

    private EditText number;
    private TextView emcall;
    private TextView textView;
    private MediaPlayer mp;
    private Button addNumber;
    private boolean soundAlert = false;

    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_giroscopio);
        switchMaterial = findViewById(R.id.switchMaterial);

        number = findViewById(R.id.editTextPhone);
        emcall = findViewById(R.id.call);
        textView = findViewById(R.id.textView);
        addNumber = findViewById(R.id.Add);
        addNumber.setEnabled(false);

        ActivityCompat.requestPermissions(TestGiroscopio.this, new String[]{Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        addNumber.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               phone = number.getText().toString();
                number.setVisibility(View.INVISIBLE);
                emcall.setVisibility(View.INVISIBLE);
                addNumber.setVisibility(View.INVISIBLE);
            }
        });
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
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String n = String.valueOf(number.getText());
                if(n.length()!=10){
                    addNumber.setEnabled(false);
                    emcall.setText("number not valid");
                    emcall.setTextColor(Color.parseColor("#FF0000"));

                }else{
                    emcall.setText("number valid");
                    emcall.setTextColor(Color.parseColor("#32FF00"));
                    addNumber.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (phone == null || phone.isEmpty()){
                        switchMaterial.setChecked(false);
                        Toast.makeText(getApplicationContext(), "Insert a phone number before", Toast.LENGTH_LONG).show();
                        return;
                    }

                    MyService.startLifeGuard();
                    Handler handler = new Handler();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    if(MyService.isLifeGuard()){
                                        if(MyService.getCountdown()>30){
                                            textView.setTextColor(Color.parseColor("#32FF00"));
                                        }else{
                                            if(MyService.getCountdown()>10){
                                                textView.setTextColor(Color.parseColor("#E9FF00"));
                                            }else{
                                                textView.setTextColor(Color.parseColor("#FF0000"));
                                            }
                                        }
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

    public void startSoundAlert(){
        if(isSoundAlert()){
            return;
        }
        soundAlert = true;
        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.start();
    }

    public boolean isSoundAlert(){
        return soundAlert;
    }
    public void stopSoundAlert(){
        if (!isSoundAlert()) return;
        soundAlert = false;
        mp.release();
    }

    public void createMessage(Context context) {
        Thread check10sec = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(10000);
                    startSoundAlert();
                    emergencyCall(phone);
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
                emergencyCall(phone);
                startSoundAlert();
                check10sec.interrupt();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        stopSoundAlert();
        goToHome();
        finish();
        //super.onBackPressed();
    }

    public void emergencyCall(@NotNull String phone){
        String s = "tel:"+phone;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }

    private void goToHome() {
        stopSoundAlert();
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