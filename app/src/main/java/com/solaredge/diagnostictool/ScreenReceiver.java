package com.solaredge.diagnostictool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Intent.ACTION_SCREEN_OFF:
                TestPowerButton.unlockScreen();
                break;

            case Intent.ACTION_SCREEN_ON:
                // and do whatever you need to do here
                Toast.makeText(context.getApplicationContext(), "dewfwerg", Toast.LENGTH_LONG).show();
                TestPowerButton.clearScreen();
        }
    }


}
