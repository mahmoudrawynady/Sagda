package com.example.mahmoudrawy.eladan.Views.Views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mahmoudrawy.eladan.R;

/*
This is the Splash Screen to the App
 */
public class SplashActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////////
    private static int M_SECONDS = 2000; // 2Milli Seconds.
    private static int SECONDS = 1; // one Second.

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_splash);
        ///////////////////////////////////////////////////////////////////////////////////////
        //Delay for Splash Screen for 3 seconds.
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_slide_stay);
                overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_slide_down);
                finish();

            }
        }, SECONDS * M_SECONDS);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

}


