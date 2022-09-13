package com.solaredge.diagnostictool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TestAccelerometro extends AppCompatActivity  implements SensorEventListener {

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_accellerometro);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null){
            Sensor acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (acceleroSensor!=null){
                sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }else{
            Toast.makeText(getApplicationContext(), "Sensor service not detected.", Toast.LENGTH_LONG).show();
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            ((TextView) findViewById(R.id.accelerometer)).setText("X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
            ((TextView) findViewById(R.id.label)).setText("if you see the value change, it works!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}