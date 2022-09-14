package com.solaredge.diagnostictool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
                TestPowerButton.changeLabel("Power button pressed");
                TestPowerButton.clearScreen();
        }
    }


}
