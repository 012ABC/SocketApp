package com.example.handlerapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class PlayerService extends Service {

    private static final String TAG = "PlayerService";

    private static final long DELAY = 2000;

    private Handler sWork = new Handler();

    private Runnable task = new Runnable() {

        @Override
        public void run() {
            Log.d(TAG, DELAY / 1000 + "s after-----------");
            // 故意制造异常，使该进程挂掉
            Integer.parseInt("ok");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind------");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate------");
        sWork.postDelayed(task, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand------and startId = " + startId);
        Log.d(TAG, "onStartCommand------and intent = " + intent);
        // 实验中，可轮换这几个值测试
        return START_STICKY_COMPATIBILITY;// | START_STICKY | START_STICKY_COMPATIBILITY |
        // START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy------");
        super.onDestroy();
    }

}

