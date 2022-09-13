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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        NavigationView mNavigationView = findViewById(R.id.navigationView);
        mNavigationView.setSelected(false);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){
        finish();
        //super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.camera:
                goToCamera();
                finish();
                return true;
            case R.id.giroscopio:
                goToGiroscopio();
                finish();
                return true;
            case R.id.accelerometro:
                goToAccelerometro();
            default:
                return false;
        }
    }

    @Override
    protected void onResume(){
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
}