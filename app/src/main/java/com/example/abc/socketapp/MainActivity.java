package com.example.abc.socketapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.abc.socketapp.bean.Constants;
import com.example.abc.socketapp.utils.DownloadImage;
import com.example.abc.socketapp.utils.GlideDiskCacheUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage = findViewById(R.id.image);
        findViewById(R.id.btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                circleImg();
                break;
            default:
        }
    }

    /**
     * 圆形图片
     */
    private void circleImg(){
        RequestOptions options=new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .load(Constants.imageUrl)
                .apply(options)
                .into(mImage);
    }
    /**
     * 8.5Listener
     */
    private void workListener(){
        Glide.with(this)
                .load(Constants.imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }).into(mImage);
    }

    private void doWork(){
        File mFile=new File(Environment.getExternalStorageDirectory()
                +File.separator+"Glide","glideDownload.png");
        DownloadImage downloadImage=new DownloadImage(Constants.imageUrl, getApplicationContext()
                , 600, 600, mFile, new DownloadImage.ImagedownLoadCallBack() {
            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownLoadFailed() {

            }
        });
        new Thread(downloadImage).start();
    }
    private void downloadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = getApplicationContext();
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(Constants.imageUrl)
                            .submit();
                    final File imageFile = target.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Dylan121",imageFile.getAbsolutePath());
                            Glide.with(MainActivity.this).load(imageFile.getAbsoluteFile()).into(mImage);
                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
