package com.solaredge.diagnostictool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.Inet4Address;

public class MyService extends Service {
    private static boolean isAlive = false;

    public static boolean isAlive() {
        return isAlive;
    }

    public static void setAlive(boolean alive){
        isAlive = alive;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
