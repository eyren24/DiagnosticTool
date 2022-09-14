package com.solaredge.diagnostictool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class TestPowerButton extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_power_button);
        IntentFilter filter = new IntentFilter( Intent.ACTION_SCREEN_ON );
        filter.addAction( Intent.ACTION_SCREEN_OFF );
        registerReceiver(new ScreenReceiver(), filter);
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

}