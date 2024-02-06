package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfort.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


//        //Status Bar Color
//        getWindow().setStatusBarColor(ContextCompat.getColor(SplashActivity.this,R.color.color_white));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);

    }
}