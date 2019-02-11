package com.example.handlerapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.handlerapp.MyService;
import com.example.handlerapp.R;

public class SetResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent service;
    private MyConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_result);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                service=new Intent(this, MyService.class);
                startService(service);
                break;
            case R.id.btn1:
               stopService(service);
                break;
            case R.id.btn2:
                Intent service2=new Intent(this, MyService.class);
                connection=new MyConnection();
                bindService(service2,connection,BIND_AUTO_CREATE);
                break;
            case R.id.btn3:
                unbindService(connection);
                break;
            case R.id.btn4:
                Intent service4=new Intent(this, MyService.class);
                startService(service4);
                break;
                default:
        }
    }


    private class MyConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Dylan121","onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("Dylan121","onServiceDisconnected");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}