package com.example.handlerapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Dylan121","onBind");
        MyBinder mBind=new MyBinder();
        Log.e("Dylan121",mBind.toString());
        return mBind;
    }

    @Override
    public void onCreate() {
        Log.e("Dylan121","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Dylan121","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyBinder extends Binder{
        public void systemOut(){
            System.out.println("该方法在MyService的内部类MyBinder中");
        }
    }
    @Override
    public void onDestroy() {
        Log.e("Dylan121","onDestroy");
        super.onDestroy();
    }
}
