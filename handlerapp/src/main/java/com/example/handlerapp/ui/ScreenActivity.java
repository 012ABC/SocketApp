package com.example.handlerapp.ui;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.handlerapp.R;
import com.example.handlerapp.utils.ScreenShotListenManager;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ScreenActivity extends AppCompatActivity  {

    private static final int RC_CODE = 1;
    private ImageView mImage;
    private ScreenShotListenManager screenShotListenManager;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        mImage = findViewById(R.id.image);
        mText = findViewById(R.id.text);
        mImage.setImageResource(R.drawable.a);
        checkPers();
    }

    @AfterPermissionGranted(RC_CODE)
    private void checkPers() {
        String[] pers=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this,pers)){
            Toast.makeText(this, "开通了", Toast.LENGTH_SHORT).show();
        }else {
            EasyPermissions.requestPermissions(this, "开启权限",
                    RC_CODE, pers);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenShotListenManager = ScreenShotListenManager.newInstance(this);
        screenShotListenManager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    @Override
                    public void onShot(String imagePath) {
                        Log.i("Dylan121",imagePath);
                        Glide.with(ScreenActivity.this).load(imagePath).into(mImage);
                        Toast.makeText(ScreenActivity.this, "糟老头子坏得很", Toast.LENGTH_SHORT).show();
                        mText.setVisibility(View.VISIBLE);

                    }
                }
        );
        screenShotListenManager.startListen();
    }
}
