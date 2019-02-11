package com.example.handlerapp;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED;
import static android.app.DownloadManager.COLUMN_REASON;
import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_PAUSED;
import static android.app.DownloadManager.STATUS_PENDING;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

public class DownloadPdfUtils {

    //下载器
    private DownloadManager downloadManager;

    //上下文
    private Context mContext;

    //下载的ID
    private long downloadId;

    private boolean mReceiverTag = false;

    public DownloadPdfUtils(Context context) {
        if (context == null) {
            return;
        }
        this.mContext = context;

    }

    public void downloadUrl(String url, String name) {

        //创建下载任务
        Request request = new Request(Uri.parse(url));

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(Request.VISIBILITY_HIDDEN);
        request.setVisibleInDownloadsUi(true);

        //设置下载路径
        final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String pdfPath = sdcard + "/mysteel/pdf";
        if (!(new File(pdfPath)).exists()) {
            (new File(pdfPath)).mkdirs();
        }
        File file = new File(pdfPath, name);
        request.setDestinationUri(Uri.fromFile(file));
        downloadManager = ((DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE));
        removePdfFile();
        downloadId = downloadManager.enqueue(request);
        checkStatu();
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mReceiverTag = true;
    }

    private void checkStatu() {
        Query query = new Query();

        //通过下载的ID查找
        Cursor cursor = downloadManager.query(query);

        while (cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(COLUMN_REASON));
            switch (status) {
                case STATUS_PAUSED:
                    Log.i("Dylan121", "STATUS_PAUSED1");
                    break;
                case STATUS_PENDING:
                    Log.i("Dylan121", "STATUS_PENDING1");
                    break;
                case STATUS_RUNNING:
                    Log.i("Dylan121", "STATUS_RUNNING1");
                    break;
                case STATUS_SUCCESSFUL:
                    Log.i("Dylan121", "STATUS_SUCCESSFUL1");
                    break;
                case STATUS_FAILED:
                    Log.i("Dylan121", "STATUS_FAILED1");
                    break;
                default:
                    break;
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    private BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            downloadManager.remove(downloadId);
        }
    };

    /**
     * 下载状态
     */
    private void checkStatus() {
        Query query = new Query();

        //通过下载的ID查找
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case STATUS_PAUSED:
                    Log.i("Dylan121", "STATUS_PAUSED");
                    break;
                case STATUS_PENDING:
                    Log.i("Dylan121", "STATUS_PENDING");
                    break;
                case STATUS_RUNNING:
                    Log.i("Dylan121", "STATUS_RUNNING");
                    break;
                case STATUS_SUCCESSFUL:
                    Log.i("Dylan121", "STATUS_SUCCESSFUL");
                    break;
                case STATUS_FAILED:
                    Log.i("Dylan121", "STATUS_FAILED");
                    break;
                default:
                    break;
            }
        } else {

        }
        cursor.close();
        mContext.unregisterReceiver(receiver);
        mReceiverTag = false;
    }

    /**
     * 返回文件下载路径
     *
     * @return
     */
    public String queryUrl() {
        String url = "";
        if (downloadId != -1) {
            Query query = new Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                }
            }
            assert cursor != null;
            cursor.close();
        }
        return url;
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        mContext.registerReceiver(receiver1, new IntentFilter(ACTION_NOTIFICATION_CLICKED));
    }

    /**
     * 取消广播注册
     */
    public void unRegister() {
        if (mReceiverTag) {
            mContext.unregisterReceiver(receiver);
        }
        mContext.unregisterReceiver(receiver1);
    }

    /**
     * 删除PDF文件
     */
    private void removePdfFile() {
        Query query = new Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getColumnIndex(DownloadManager.COLUMN_ID) != -1) {
                    downloadManager.remove(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
                }
            }
        }
        assert cursor != null;
        cursor.close();
    }
}
