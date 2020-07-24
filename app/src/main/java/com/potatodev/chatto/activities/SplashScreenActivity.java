package com.potatodev.chatto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean isAuth;
    private String password;
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
        password = authentication.getString(SPreferences.getKeyPass(), "");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("authPassword").document("7Y0Hc1V00Mwfp0Ygmzqe");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    final String currentPass = (String) snapshot.get("currentPassword");

                    // Delay on splash screen
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Page designation for each authentication result
                            if (isAuth || password.equals(currentPass)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("Welcome back!", Toast.LENGTH_LONG);
                                    }
                                });
                                startActivity(new Intent(SplashScreenActivity.this, StartActivity.class));
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("Your current password doesn't match. Please the password to the developer", Toast.LENGTH_LONG);
                                    }
                                });
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            }
                            finish();
                        }
                    }, 3000);
                }
            }
        });
    }

    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}