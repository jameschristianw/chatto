package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

public class StartActivity extends AppCompatActivity {

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeViews();
    }

    // Function to initialize views in the activity and its logic if available
    public void initializeViews(){
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences auth = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
                SharedPreferences.Editor editor = auth.edit();
                editor.putBoolean(SPreferences.getKeyIsAuth(), false);
                editor.putString(SPreferences.getKeyPass(), "");
                editor.apply();

                startActivity(new Intent(StartActivity.this, SplashScreenActivity.class));
                finish();
            }
        });
    }
}