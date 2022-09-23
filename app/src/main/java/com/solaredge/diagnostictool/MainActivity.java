package com.solaredge.diagnostictool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setSelected(false);
        mNavigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "You are alreay at home!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.history:
                        gotoHistory();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void updateNavigationBarState(int actionId){
        Menu menu = bottomNavigationView.getMenu();

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
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
                break;
            case R.id.gyroscope:
                goToGiroscopio();
                finish();
                break;
            case R.id.accelerometer:
                goToAccelerometro();
                finish();
                break;
            case R.id.proximity:
                goToSensoreDiProssimita();
                finish();
                break;
            case R.id.fingerprintSensor:
                goToFingerprintSensor();
                finish();
                break;
            case R.id.brightness_sensor:
                goToSensoreDiLuminosita();
                finish();
                break;
            case R.id.termometer:
                goToTermometro();
                finish();
                break;
            case R.id.magnetometer:
                goToMagnetometer();
                finish();
                break;
            case R.id.powerbutton:
                goToPowerButtonCheck();
                finish();
                break;
            default:
                return false;
        }
        updateNavigationBarState(item.getItemId());
        return true;
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
        finish();
    }


    private void gotoHistory() {
        Intent switchActivityIntent = new Intent(this, History.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToGiroscopio() {
        Intent switchActivityIntent = new Intent(this, TestGiroscopio.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToAccelerometro() {
        Intent switchActivityIntent = new Intent(this, TestAccellerometro.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToSensoreDiProssimita() {
        Intent switchActivityIntent = new Intent(this, TestSensoreDiProssimita.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToFingerprintSensor() {
        Intent switchActivityIntent = new Intent(this, TestFingerPrint.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToSensoreDiLuminosita() {
        Intent switchActivityIntent = new Intent(this, TestSensoreDiLuminosita.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToTermometro() {
        Intent switchActivityIntent = new Intent(this, TestTermometro.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToMagnetometer() {
        Intent switchActivityIntent = new Intent(this, TestMagnetometer.class);
        startActivity(switchActivityIntent);
        finish();
    }

    private void goToPowerButtonCheck() {
        Intent switchActivityIntent = new Intent(this, TestPowerButton.class);
        startActivity(switchActivityIntent);
        finish();
    }
}