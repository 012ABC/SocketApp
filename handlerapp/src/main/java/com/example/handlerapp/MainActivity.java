package com.example.handlerapp;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String url="http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/07/10/141304.pdf";
    private static final int RC_CODE = 1;

    private Button mStart;

    private DownloadPdfUtils mDownloadPdfUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStart = findViewById(R.id.start);
        mStart.setOnClickListener(this);
        mDownloadPdfUtils=new DownloadPdfUtils(getApplicationContext());
       checkPermission();
    }

    @AfterPermissionGranted(RC_CODE)
    private void checkPermission(){
        String[]perms={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this,perms)){
            mDownloadPdfUtils.downloadUrl(url,"test");
        }else {
            EasyPermissions.requestPermissions(this,"eedf",RC_CODE,perms);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                break;
                default:
        }
    }
}
