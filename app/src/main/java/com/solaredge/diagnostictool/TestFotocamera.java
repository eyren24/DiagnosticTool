package com.solaredge.diagnostictool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class TestFotocamera extends AppCompatActivity {

    private CameraCaptureSession myCameraCaptureSession;
    private String myCameraID;
    private CameraManager myCameraManager;
    private CameraDevice myCameraDevice;
    private TextureView myTexureView;
    private CaptureRequest.Builder myCaptureRequestBuilder;
    private ExtendedFloatingActionButton extendedFab;
    private int click=0;
    private Button camera_button;
    private SwitchMaterial frontBackCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test_fotocamera);

        frontBackCam = findViewById(R.id.switchMaterial);

        camera_button = findViewById(R.id.open_camera);

        extendedFab = findViewById(R.id.extended_fab);

        myTexureView = findViewById(R.id.textureView);

        myCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        extendedFab.shrink();

        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendedFab.extend();
                click ++;
                if (click == 2){
                    goToHome();
                }
            }
        });
        frontBackCam.setChecked(false);
        if (frontBackCam.isChecked()){
            // Back camera
            openCamera();
        }else {
            openCameraFront();
        }

        frontBackCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    // Back camera
                    openCamera();
                }else {
                    // Front camre
                    openCameraFront();
                }
            }
        });

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPreview();
            }
        });
    }

    private CameraDevice.StateCallback myStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            myCameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            myCameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            myCameraDevice.close();
        }
    };

    private void openCamera() {
        try {
            myCameraID = myCameraManager.getCameraIdList()[0];
            ActivityCompat.requestPermissions(TestFotocamera.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myCameraManager.openCamera(myCameraID, myStateCallback, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void openCameraFront() {
        try {
            myCameraID = myCameraManager.getCameraIdList()[1];
            ActivityCompat.requestPermissions(TestFotocamera.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myCameraManager.openCamera(myCameraID, myStateCallback, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void cameraPreview(){
        SurfaceTexture mySurfaceTexure = myTexureView.getSurfaceTexture();
        Surface surface = new Surface(mySurfaceTexure);
        try {
            myCaptureRequestBuilder = myCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            myCaptureRequestBuilder.addTarget(surface);
            myCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    myCameraCaptureSession = session;
                    myCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try {
                        myCameraCaptureSession.setRepeatingRequest(myCaptureRequestBuilder.build(),null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        goToHome();
        finish();
        //super.onBackPressed();
    }

    private void goToHome() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

}

