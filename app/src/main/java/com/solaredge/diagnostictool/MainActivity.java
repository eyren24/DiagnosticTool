package com.solaredge.diagnostictool;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setSelected(false);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera:
                goToCamera();
                finish();
                return true;
            case R.id.gyroscope:
                goToGiroscopio();
                finish();
                return true;
            case R.id.accelerometer:
                goToAccelerometro();
                finish();
                return true;
            case R.id.proximity:
                goToSensoreDiProssimita();
                finish();
                return true;
            case R.id.fingerprintSensor:
                goToFingerprintSensor();
                finish();
                return true;
            case R.id.brightness_sensor:
                goToSensoreDiLuminosita();
                finish();
                return true;
            case R.id.termometer:
                goToTermometro();
                finish();
                return true;
            case R.id.magnetometer:
                goToMagnetometer();
                finish();
                return true;
            case R.id.powerbutton:
                goToPowerButtonCheck();
                finish();
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        VideoView videoView = (VideoView) findViewById(R.id.videoView);  //casting to VideoView is not Strictly required above API level 26
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.bg); //set the path of the video that we need to use in our VideoView
        videoView.start();  //start() method of the VideoView class will start the video to play
    }

    private void goToCamera() {
        Intent switchActivityIntent = new Intent(this, TestFotocamera.class);
        startActivity(switchActivityIntent);
    }

    private void goToGiroscopio() {
        Intent switchActivityIntent = new Intent(this, TestGiroscopio.class);
        startActivity(switchActivityIntent);
    }

    private void goToAccelerometro() {
        Intent switchActivityIntent = new Intent(this, TestAccellerometro.class);
        startActivity(switchActivityIntent);
    }

    private void goToSensoreDiProssimita() {
        Intent switchActivityIntent = new Intent(this, TestSensoreDiProssimita.class);
        startActivity(switchActivityIntent);
    }

    private void goToFingerprintSensor() {
        Intent switchActivityIntent = new Intent(this, TestFingerPrint.class);
        startActivity(switchActivityIntent);
    }
    private void goToSensoreDiLuminosita() {
        Intent switchActivityIntent = new Intent(this, TestSensoreDiLuminosita.class);
        startActivity(switchActivityIntent);
    }
    private void goToTermometro() {
        Intent switchActivityIntent = new Intent(this, TestTermometro.class);
        startActivity(switchActivityIntent);
    }
    private void goToMagnetometer() {
        Intent switchActivityIntent = new Intent(this, TestMagnetometer.class);
        startActivity(switchActivityIntent);
    }
    private void goToPowerButtonCheck() {
        Intent switchActivityIntent = new Intent(this, TestPowerButton.class);
        startActivity(switchActivityIntent);
    }
}