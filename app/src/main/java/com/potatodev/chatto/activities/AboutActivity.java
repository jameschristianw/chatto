package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.potatodev.chatto.R;

public class AboutActivity extends AppCompatActivity {

    TextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAppVersion = findViewById(R.id.tvAppVersion);

        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "App version " + info.versionName;
            tvAppVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}