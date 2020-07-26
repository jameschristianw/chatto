package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.potatodev.chatto.R;
import com.potatodev.chatto.preferences.SPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    LinearLayout llSearchResult, llProgressBar;
    TextView tvSearchNameResult;
    Button btnSearchUser, btnAddFriend;
    ImageView imgSearchPPResult;
    EditText edtUsername;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FirebaseFirestore firestore;
    Context context;

    // My data
    String myUsername, myName;
    long myFriendsCount;

    // Search data
    String resultUsername, resultName;
    long targetFriendsCount;

    String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        context = this;

        getSupportActionBar().setTitle("Add Friend");

        firestore = FirebaseFirestore.getInstance();

        initializeData();
        initializeViews();
    }

    public void initializeData(){
        // Get your firestore profile from SharedPreferences
        preferences = getSharedPreferences(SPreferences.getPreferenceFilename(), SPreferences.getPreferenceMode());
        editor = preferences.edit();
        myUsername = preferences.getString(SPreferences.getKeyUsername(), "");
        myFriendsCount = preferences.getLong(SPreferences.getKeyFriendsCount(), 0);
        myName = preferences.getString(SPreferences.getKeyFullname(), "");

        Log.d("AddFriendActivity", String.valueOf(myFriendsCount));
        Log.d("AddFriendActivity", myUsername != null ? myUsername : "Eh it's empty");
    }

    public void initializeViews(){
        llSearchResult = findViewById(R.id.llSearchResult);
        llProgressBar = findViewById(R.id.llProgressBar);
        tvSearchNameResult = findViewById(R.id.tvSearchNameResult);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnSearchUser = findViewById(R.id.btnSearch);
        imgSearchPPResult = findViewById(R.id.imgSearchPPResult);
        edtUsername = findViewById(R.id.edtUsername);

        llSearchResult.setVisibility(View.GONE);
        llProgressBar.setVisibility(View.GONE);

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llProgressBar.setVisibility(View.VISIBLE);
                resultUsername = edtUsername.getText().toString();

                firestore.collection("users").document(resultUsername)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        llProgressBar.setVisibility(View.GONE);
                        if (documentSnapshot.exists()) {
                            llSearchResult.setVisibility(View.VISIBLE);
                            resultName = (String) documentSnapshot.get("name");
                            targetFriendsCount = (long) documentSnapshot.get("friendsCount");

                            tvSearchNameResult.setText(resultName);
                        } else {
                            llSearchResult.setVisibility(View.GONE);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("Username not found", Toast.LENGTH_SHORT);
                                }
                            });
                        }
                    }
                });


            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating chatId
                chatId = myUsername.concat(resultUsername);

                // Adding target to your friend list
                Object newFriend;
                HashMap<String, String> newLine = new HashMap<>();
                newLine.put("chatid", chatId);
                newLine.put("name", resultName);
                newLine.put("id", resultUsername);
                updateContacts(true, myFriendsCount, newLine, myUsername);

                // Adding you to target friend list
                HashMap<String, String> newLineTarget = new HashMap<>();
                newLineTarget.put("chatid", chatId);
                newLineTarget.put("name", myName);
                newLineTarget.put("id", myUsername);
                updateContacts(false, targetFriendsCount, newLineTarget, resultUsername);

                // Finishing the activity
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void updateContacts(boolean isMyself, long friendsCount, HashMap<String, String> newLine, String username) {
        if (friendsCount == 0){
            List<HashMap<String, String>> newContact = new ArrayList<>();
            newContact.add(newLine);

            HashMap<String, List> contact = new HashMap<>();
            contact.put("contacts", newContact);

            firestore.collection("users").document(username).set(contact, SetOptions.merge());
        } else {
            firestore.collection("users").document(username).update("contacts", FieldValue.arrayUnion(newLine));
        }
        firestore.collection("users").document(username).update("friendsCount", FieldValue.increment(1));

        if (isMyself){
            editor.putLong(SPreferences.getKeyFriendsCount(), myFriendsCount + 1);
            editor.apply();
        }
    }

    public void showToast(String message, int duration){
        Toast.makeText(context, message, duration).show();
    }
}