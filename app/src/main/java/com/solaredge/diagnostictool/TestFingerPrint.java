package com.solaredge.diagnostictool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.compose.ui.text.input.TextFieldValue;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class TestFingerPrint extends AppCompatActivity {

    protected BiometricPrompt biometricPrompt;
    protected Executor executor;
    protected BiometricPrompt.PromptInfo promptInfo;
    private ConstraintLayout constraintLayout;
    private TextView fingerprintalert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_finger_print);
        fingerprintalert = findViewById(R.id.fingerprintalert);

        //Init
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(TestFingerPrint.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                fingerprintalert.setText("Authentication error: " + errString + "\n error code: " + errorCode);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString + "\n error code: " + errorCode, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                fingerprintalert.setText("Authentication succeeded");
                Toast.makeText(getApplicationContext(), "Authentication succeeded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                fingerprintalert.setText("Authentication failed");
                Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_LONG).show();
            }

        });
        // Setup
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("login using fingerprint authentication")
                .setNegativeButtonText("User App Password")
                .setConfirmationRequired(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
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
}