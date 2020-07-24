package com.potatodev.chatto.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

public class MainActivity extends AppCompatActivity {

    TextView tvWhyPassword;
    Button btnSubmit;
    EditText edtPassword;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initializeAlertDialog();
        initializeViews();
    }

    public void initializeAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage("This app is portfolio only and shouldn't be use for day to day basis.");
        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    // Function to initialize views in the activity and its logic if available
    public void initializeViews(){
        tvWhyPassword = findViewById(R.id.tvWhyPassword);
        edtPassword = findViewById(R.id.edtPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edtPassword.getText().toString();
                if (password.equals("a")) {
                    SharedPreferences auth = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
                    SharedPreferences.Editor editor = auth.edit();
                    editor.putBoolean(SPreferences.getKeyIsAuth(), true);
                    editor.putString(SPreferences.getKeyPass(), password);
                    editor.apply();

                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                    finish();
                } else {
                    showToast("Wrong password!", Toast.LENGTH_LONG);
                }
            }
        });

        tvWhyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(context);
                passwordDialog.setTitle("Why you may ask?");
                passwordDialog.setMessage("You might be wondering why this app need password, right? This app use Google Firebase to temporarily store the chat. So to avoid reaching the limit allowed, before needed to pay, and avoiding this app to be used for malicious intent, a password is needed.");
                passwordDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                passwordDialog.show();
            }
        });
    }

    // Just to show toast. A solution to shorten it, right?
    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}