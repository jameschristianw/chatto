package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean isAuth;
    public static SharedPreferences authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*
            Getting authentication
            Currently using SharedPreference
            Consider using Firebase
        */
        authentication = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
        isAuth = authentication.getBoolean(SPreferences.getKeyIsAuth(), false);

        // Delay on splash screen
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Page designation for each authentication result
                if (!isAuth){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, StartActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}