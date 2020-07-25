package com.potatodev.chatto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

import java.sql.Array;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tvWhyPassword;
    Button btnSubmit;
    EditText edtPassword, edtUsername, edtName;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
//        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//        TextView tvTitle = findViewById(titleId);
//        TypedValue value = new TypedValue();
//        Resources.Theme theme = context.getTheme();
//        theme.resolveAttribute(R.style.AppThemeNoActionBar, value, true);
//        int color = value.data;
//        tvTitle.setTextColor(color);


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
        edtUsername = findViewById(R.id.edtUsername);
        edtName = findViewById(R.id.edtName);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });

        tvWhyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(context);
                passwordDialog.setTitle("Why you may ask?");
                passwordDialog.setMessage("You might be wondering why this app need password, right? This app use Google Firebase to temporarily store the chat. So to avoid reaching the limit allowed, before needed to pay, and avoiding this app to be used for malicious intent, a password is needed. Password is changed every Monday.");
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

    // Checking password and move activity if needed
    public void checkPassword() {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.collection("authPassword").document("7Y0Hc1V00Mwfp0Ygmzqe");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()){
                    String input = edtPassword.getText().toString();
                    String username = edtUsername.getText().toString();
                    String name = edtName.getText().toString();
                    String pass = (String) snapshot.get("currentPassword");

                    if (input.equals(pass)) { // Correct password
                        String password = edtPassword.getText().toString();

                        // Add it to SharedPreferences
                        // Password resets every monday
                        SharedPreferences auth = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
                        SharedPreferences.Editor editor = auth.edit();
                        editor.putBoolean(SPreferences.getKeyIsAuth(), true);
                        editor.putString(SPreferences.getKeyPass(), password);
                        editor.putString(SPreferences.getKeyUsername(), username);
                        editor.putString(SPreferences.getKeyFullname(), name);
                        editor.apply();

                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("name", name);

                        firestore.collection("users").document(username).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast("Welcome!", Toast.LENGTH_LONG);
                                startActivity(new Intent(MainActivity.this, StartActivity.class));
                                finish();
                            }
                        });

                    } else { // Wrong password
                        showToast("Wrong password!", Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAboutApp:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;

            case R.id.menuExit:
                finish();
                return true;

            default:
                return true;
        }
    }

    // Just to show toast. A solution to shorten it, right?
    public void showToast(String message, int duration){
        Toast.makeText(this, message, duration).show();
    }
}