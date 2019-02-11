

package com.example.handlerapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.handlerapp.R;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Dyaln121","First onStart");
    }

    @Override
    protected void onPause() {
        setResult(RESULT_OK);
        super.onPause();
        Log.i("Dylan121","First onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Dylan121","First onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Dylan121","First onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Dylan121","First onDestroy");
    }
}
