package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText edtFullname;
    private Button btnSubmit;

    private String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getSupportActionBar().setTitle("Account Creation");

        edtFullname = findViewById(R.id.edtFullname);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bundle = getIntent();
                final String username = bundle.getStringExtra(SPreferences.getKeyUsername());
                final String password = bundle.getStringExtra(SPreferences.getKeyPass());
                fullname = edtFullname.getText().toString();

                final Map<String, Object> newUser = new HashMap<>();
                newUser.put("name", fullname);

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("users").document(username).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Add it to SharedPreferences
                        // Password resets every monday
                        setSharedPref(username, fullname, password);

                        showToast("Welcome!", Toast.LENGTH_LONG);
                        startActivity(new Intent(CreateAccountActivity.this, StartActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    public void setSharedPref(String username, String fullname, String password){
        SharedPreferences auth = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
        SharedPreferences.Editor editor = auth.edit();
        editor.putBoolean(SPreferences.getKeyIsAuth(), true);
        editor.putString(SPreferences.getKeyPass(), password);
        editor.putString(SPreferences.getKeyUsername(), username);
        editor.putString(SPreferences.getKeyFullname(), fullname);
        editor.apply();
    }

    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}