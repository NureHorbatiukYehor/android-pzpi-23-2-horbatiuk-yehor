package com.example.test1;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lab1", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lab1", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lab1", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lab1", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lab1", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lab1", "onDestroy");
    }
}
