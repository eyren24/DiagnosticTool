package com.solaredge.diagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class TestPowerButton extends AppCompatActivity {

    private static TextView powerlabel;

    private ExtendedFloatingActionButton extendedFab;
    private int click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_power_button);

        extendedFab = findViewById(R.id.extended_fab);
        extendedFab.shrink();

        powerlabel = findViewById(R.id.powerlabel);
        IntentFilter filter = new IntentFilter( Intent.ACTION_SCREEN_ON );
        filter.addAction( Intent.ACTION_SCREEN_OFF );
        registerReceiver(new ScreenReceiver(), filter);

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

    public static void unlockScreen () {
        if (current == null) return;

        Window window = current.getWindow();
        window.addFlags( WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD );
        window.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED );
        window.addFlags( WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON   );
    }

    public static void clearScreen () {
        if (current == null) return;

        Window window = current.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD );
        window.clearFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED );
        window.clearFlags( WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON   );
    }
    private static TestPowerButton current;
    @Override
    protected void onResume () {
        current = this;
        super.onResume();
    }
    public static void changeLabel(String text){
        powerlabel.setText(text);
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