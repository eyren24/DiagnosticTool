package com.solaredge.diagnostictool;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends IntentService {

    protected static boolean lifeGuard = false;

    private static boolean serviceIsRunning = false;

    private static int countdown = 60;

    public static volatile boolean shouldStop = false;

    public static boolean isLifeGuard() {
        return lifeGuard;
    }

    public MyService() {
        super(MyService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static int getCountdown() {
        return countdown;
    }

    public static void setCountdown(int countdown) {
        MyService.countdown = countdown;
    }

    public static void startLifeGuard(){
        lifeGuard = true;
    }
    public static void stopLifeGuard(){
        lifeGuard = false;
        countdown = 60;
    }

    public static boolean isServiceIsRunning(){
        return serviceIsRunning;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        serviceIsRunning = true;
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Handler handler = new Handler(getMainLooper());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (TestGiroscopio.lastValue <= 10 && TestGiroscopio.lastValue >= 8 && countdown != 0 && lifeGuard){
                            countdown--;
                        }else{
                            countdown = 60;
                        }
                        Log.e("Lifeguard", String.valueOf(lifeGuard));
                        Log.e("Countdown", String.valueOf(countdown));
                        if (countdown == 0 && lifeGuard){
                            /// generate message box
                            dbHandler.addNewCourse(TestGiroscopio.lastValue, String.valueOf(Calendar.getInstance().getTime()));
                        }
                    }
                });
            }
        }, 0, 1000);

        if(shouldStop) {
            serviceIsRunning = false;
            stopSelf();
        }
    }
}
