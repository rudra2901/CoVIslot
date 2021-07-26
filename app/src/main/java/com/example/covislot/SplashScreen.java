package com.example.covislot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen

        setContentView(R.layout.splash_activity);

        sp =  getSharedPreferences("login",MODE_PRIVATE);

        int SPLASH_SCREEN_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent i;
            if(sp.getBoolean("logged", false)) {
                i = new Intent(SplashScreen.this, SignedInActivity.class);
            }
            else {
                i = new Intent(SplashScreen.this, RegistrationActivity.class);
            }
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
