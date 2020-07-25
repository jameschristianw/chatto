package com.potatodev.chatto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

public class StartActivity extends AppCompatActivity {

    Button btnLogout;
    ImageView imgMenu, imgProfPic;
    TextView tvStartName, tvEmptyList;
    RecyclerView rvFriendList;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeViews();
    }

    // Function to initialize views in the activity and its logic if available
    public void initializeViews(){
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setElevation(0);

        imgMenu = findViewById(R.id.imgMenu);
        imgProfPic = findViewById(R.id.imgProfPic);

        tvStartName = findViewById(R.id.tvStartName);
        preferences = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
        tvStartName.setText(preferences.getString(SPreferences.getKeyFullname(), "N/A"));

        tvEmptyList = findViewById(R.id.tvEmptyList);

        rvFriendList = findViewById(R.id.rvFriendList);
        rvFriendList.setVisibility(View.GONE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAboutApp:
                startActivity(new Intent(StartActivity.this, AboutActivity.class));
                return true;

            case R.id.menuLogout:
                showToast("Logging out", Toast.LENGTH_LONG);
                SharedPreferences auth = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
                SharedPreferences.Editor editor = auth.edit();
                editor.putBoolean(SPreferences.getKeyIsAuth(), false);
                editor.putString(SPreferences.getKeyPass(), "");
                editor.apply();

                startActivity(new Intent(StartActivity.this, SplashScreenActivity.class));
                finish();

                return true;

            case R.id.menuExit:
                finish();
                return true;

            default:
                return true;
        }
    }

    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}