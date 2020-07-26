package com.potatodev.chatto.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatodev.chatto.R;
import com.potatodev.chatto.adapter.FriendListAdapter;
import com.potatodev.chatto.preferences.SPreferences;

import java.util.List;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    ImageView imgProfPic;
    TextView tvStartName, tvEmptyList;
    RecyclerView rvFriendList;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String displayName;
    long friendsCount;
    Context context;

    private static final int REQUEST_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        context = this;

        initializeData();
        initializeViews();
    }

    public void initializeData(){
        preferences = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection("users")
                .document(preferences.getString(SPreferences.getKeyUsername(), ""))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                friendsCount = (long) documentSnapshot.get("friendsCount");
                displayName = (String) documentSnapshot.get("name");
                tvStartName.setText(displayName);
                setDataToSharedPreferences();

                List<Map<String, String >> friends = (List<Map<String, String>>) documentSnapshot.get("contacts");
                if (friends == null || friends.isEmpty()) {
                    Log.d("StartActivity", "You have no friends");
                    tvEmptyList.setVisibility(View.VISIBLE);
                    rvFriendList.setVisibility(View.GONE);
                } else {
                    tvEmptyList.setVisibility(View.GONE);
                    rvFriendList.setVisibility(View.VISIBLE);

                    rvFriendList.setLayoutManager(new LinearLayoutManager(context));
                    FriendListAdapter adapter = new FriendListAdapter(friends);
                    rvFriendList.setAdapter(adapter);
                    Log.d("StartActivity", "You have a friend list");
                }
            }
        });
    }

    // Function to initialize views in the activity and its logic if available
    public void initializeViews(){
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setElevation(0);

        imgProfPic = findViewById(R.id.imgProfPic);
        imgProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("This feature is not implemented yet", Toast.LENGTH_SHORT);
            }
        });

        tvStartName = findViewById(R.id.tvStartName);
        tvStartName.setText(preferences.getString(SPreferences.getKeyFullname(), "N/A"));

        tvEmptyList = findViewById(R.id.tvEmptyList);

        rvFriendList = findViewById(R.id.rvFriendList);
        rvFriendList.setVisibility(View.GONE);
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
                editor.putString(SPreferences.getKeyUsername(), "");
                editor.putString(SPreferences.getKeyFullname(), "");
                editor.putLong(SPreferences.getKeyFriendsCount(), 0);
                editor.apply();

                startActivity(new Intent(StartActivity.this, SplashScreenActivity.class));
                finish();

                return true;

            case R.id.menuAddFriend:
                startActivityForResult(new Intent(StartActivity.this, AddFriendActivity.class), REQUEST_ADD);
                return true;

            case R.id.menuExit:
                finish();
                return true;

            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            initializeData();
        }
    }

    public void setDataToSharedPreferences() {
        editor = preferences.edit();
        editor.putLong(SPreferences.getKeyFriendsCount(), friendsCount);
        editor.putString(SPreferences.getKeyFullname(), displayName);
        editor.apply();
    }

    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}