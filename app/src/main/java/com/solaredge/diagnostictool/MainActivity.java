package com.solaredge.diagnostictool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
            case R.id.accellerometro:
                goToAccellerometro();
                finish();
                return true;
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
    private void goToAccellerometro() {
        Intent switchActivityIntent = new Intent(this, TestAccelerometro.class);
        startActivity(switchActivityIntent);
    }
}