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

    // Camera Var
    protected CameraDevice cameraDevice;
    protected CameraManager cameraManager;
    private CameraCaptureSession cameraCaptureSession;
    private String cameraID;
    private TextureView textureView;
    private CaptureRequest.Builder captureRequestBuilder;

    private ExtendedFloatingActionButton extendedFab;

    // Double click to go home
    private int click=0;

    private Button camera_button;

    private SwitchMaterial frontBackCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_test_fotocamera);

        // Get texure view from UI
        textureView = findViewById(R.id.textureView);
        // Get camera manager
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        // Start camera
        openCamera();

        // Get camera switch from UI
        frontBackCam = findViewById(R.id.switchMaterial);

        // Get camera button from UI
        camera_button = findViewById(R.id.open_camera);

        // Button home animation
        extendedFab = findViewById(R.id.extended_fab);
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
        // Switch button cam
        frontBackCam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    // Back camera

                }else {
                    // Front camre

                }
            }
        });

        // Show Camera
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPreview();
                camera_button.setVisibility(View.GONE);
            }
        });
    }


    // State Callback
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    // Start camera
    private void openCamera(){
        ActivityCompat.requestPermissions(TestFotocamera.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraID = cameraManager.getCameraIdList()[0];
            cameraManager.openCamera(cameraID, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Start the preview
    private void cameraPreview(){
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        Surface surface = new Surface(surfaceTexture);
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
            @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try{
                        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            }, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Back android button
    @Override
    public void onBackPressed(){
        goToHome();
        finish();
    }

    // Home func
    private void goToHome() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

}

