package com.example.handlerapp.ui;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.handlerapp.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mBtn = findViewById(R.id.button);
        mBtn.setOnClickListener(this);
        mTextView = findViewById(R.id.txt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //要下载的文件地址
                String[] urls = {
                        "http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/07/10/141304.pdf",
                        "http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/07/03/150945.pdf",
                        "http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/06/26/115507.pdf",
                        "http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/05/29/114501.pdf",
                        "http://img02.mysteelcdn.com/wz/uploaded/glinfo/2018/04/17/144232.pdf"
                };

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,urls);
                DownloadTask downloadTask2 = new DownloadTask();
                downloadTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,urls);
                break;
                default:
        }
    }
    private class DownloadTask extends AsyncTask<String,Object,Long>{

        @Override
        protected void onPreExecute() {
            Log.i("Dylan121","DownloadTask -> onPreExecute,Thread name"+Thread.currentThread().getName());
            super.onPreExecute();
           // mBtn.setEnabled(false);
            mTextView.setText("开始下载......");
        }

        @Override
        protected Long doInBackground(String... strings) {
            Log.i("Dylan121","DownloadTask -> doInBackGround,Thread name:"+Thread.currentThread().getName());
            long totalByte=0;
            for (String url:strings) {
                Object[]result=downloadSingleFile(url);
                int byteCount=(int)result[0];
                totalByte+=byteCount;
                publishProgress(result);
                if (isCancelled()){
                    break;
                }
            }
            return totalByte;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            Log.i("Dylan121","DownloadTask -> onProgressUpdate,Thread name:"+Thread.currentThread().getName());
            super.onProgressUpdate(values);
            int byteCount=(int)values[0];
            String blogName=(String)values[1];
            String text=mTextView.getText().toString();
            text += "\n博客《" + blogName + "》下载完成，共" + byteCount + "字节";
            mTextView.setText(text);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            Log.i("Dylan121","DownloadTask ->onPostExecute,Thread name:"+Thread.currentThread().getName());
            super.onPostExecute(aLong);
            String text=mTextView.getText().toString();
            text+="\n全部下载完成了，总共下载了"+aLong+"个字节";
            mTextView.setText(text);
            mBtn.setEnabled(true);
        }

        @Override
        protected void onCancelled() {
            Log.i("Dylan121","DownloadTask -> onCancelled,Thread name:"
            +Thread.currentThread().getName());
            super.onCancelled();
            mTextView.setText("取消下载");
            mBtn.setEnabled(true);
        }

        private Object[]downloadSingleFile(String string){
            Object[]result=new Object[2];
            int byteCount=0;
            String blogName="";
            HttpURLConnection connection=null;
            try {
                URL url=new URL(string);
                connection= ((HttpURLConnection) url.openConnection());
                InputStream inputStream=connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                byte[]buf=new byte[1024];
                int length=-1;
                while ((length= inputStream.read(buf))!=-1){
                    byteArrayOutputStream.write(buf,0,length);
                    byteCount+=length;
                }
                String response=new String(byteArrayOutputStream.toByteArray(),"utf-8");
                int startIndex=response.indexOf("<title>");
                if (startIndex>0){
                    startIndex+=7;
                    int endIndex=response.indexOf("</title>");
                    if (endIndex>startIndex){
                        blogName=response.substring(startIndex,endIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }
            result[0]=byteCount;
            result[1]=blogName;
            return result;
        }
    }
}
