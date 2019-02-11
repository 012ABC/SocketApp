package com.example.handlerapp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.handlerapp.R;

import java.lang.ref.WeakReference;

public class AsynTaskActivity extends AppCompatActivity {

    private AsyncTask task;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asyn_task);
        task = new MyTask(this).execute();
    }

    public void doSomething() {
        Log.i("Dylan121", "异步任务完成,更新UI");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(false);
    }

    static class MyTask extends AsyncTask<String, Integer, String> {

        private WeakReference<Activity> weekActivity;

        public MyTask(Activity activity) {
            weekActivity = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 30; i++) {
                Log.i("Dylan121", "i=" + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Dylan121", "执行结束了");
            AsynTaskActivity mActivity;
            if ((mActivity = (AsynTaskActivity) weekActivity.get()) != null) {
                mActivity.doSomething();
            } else {
                Log.i("Dylan121", "Activity已经销毁");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("Dylan121", "执行了取消");
        }
    }
}
