package com.solaredge.diagnostictool;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.net.Inet4Address;
import java.util.Calendar;

public class MyService extends IntentService {

    protected static boolean lifeGuard = false;

    private static boolean hasStarted = false;

    public static volatile boolean shouldStop = false;

    private DBHandler dbHandler;

    public static boolean isHasStarted() {
        return hasStarted;
    }

    public static void setHasStarted(boolean hasStarted) {
        MyService.hasStarted = hasStarted;
    }

    public static boolean isLifeGuard() {
        return lifeGuard;
    }

    public static void setLifeGuard(boolean lifeGuard) {
        MyService.lifeGuard = lifeGuard;
    }

    public MyService() {
        super(MyService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String inc = intent.getStringExtra("value");
            dbHandler.addNewCourse(inc, String.valueOf(Calendar.getInstance().getTime()));
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        dbHandler = new DBHandler(getApplicationContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.solaredge.diagnostictool");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
        if(shouldStop) {
            unregisterReceiver(broadcastReceiver);
            stopSelf();
            return;
        }
    }
}
