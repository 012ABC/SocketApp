package com.example.abc.socketapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

public class DownloadImage implements Runnable{
    //要下载图片的url
    private String url;
    //Glide下载所需的Context，最好用ApplicationContext，
    //如果再用Activity作为Context了，可能会有Activity销毁了但子线程下载还没执行完这种情况出现。
    private Context context;
    //指定下载宽度，如果想下载原始宽带指定为0
    private int width;
    //指定下载高度，如果想下载原始高带指定为0
    private int height;
    //指定下载位置
    private File mFile;
    //下载完之后的回调
    private ImagedownLoadCallBack callBack;
    public interface ImagedownLoadCallBack{
        void onDownLoadSuccess(Bitmap bitmap);
        void onDownLoadFailed();
    }

    //用于回调到主线程的Handler，便于在回调回去的方法中执行UI操作
    private Handler mHandler;

    public DownloadImage(String url, Context context, int width, int height, File mFile, ImagedownLoadCallBack callBack) {
        this.url = url;
        this.context = context;
        this.width = width;
        this.height = height;
        this.mFile = mFile;
        this.callBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        try {
            if (width==0){
                width = Target.SIZE_ORIGINAL;
            }
            if (height==0){
                height = Target.SIZE_ORIGINAL;
            }
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit(width,height)
                    .get();
            if (bitmap != null){
                //上级文件夹不存在则创建
                if (!mFile.getParentFile().exists()){
                    mFile.getParentFile().mkdirs();
                }
                //文件不存在则创建
                if (!mFile.exists()){
                    mFile.createNewFile();
                }
                fos = new FileOutputStream(mFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null && mFile.exists()) {
                final Bitmap finalBitmap = bitmap;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDownLoadSuccess(finalBitmap);
                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDownLoadFailed();
                    }
                });
            }
        }
    }
}
