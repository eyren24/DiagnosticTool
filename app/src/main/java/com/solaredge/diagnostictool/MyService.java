package com.solaredge.diagnostictool;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends IntentService {

    protected static boolean lifeGuard = false;

    private static int countdown = 60;

    public static volatile boolean shouldStop = false;

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
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        startLifeGuard();
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Handler handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (TestGiroscopio.lastValue <= 10 && TestGiroscopio.lastValue >= 8){
                            if (countdown == 0) return;
                            countdown--;
                        }else{
                            countdown = 60;
                        }

                        if (countdown == 0){
                            /// generate message box
                            dbHandler.addNewCourse(TestGiroscopio.lastValue, String.valueOf(Calendar.getInstance().getTime()));
                        }
                    }
                });
            }
        }, 0, 1000);

        if(shouldStop) {
            stopSelf();
        }
    }
}
