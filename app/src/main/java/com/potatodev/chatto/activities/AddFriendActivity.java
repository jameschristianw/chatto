package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatodev.chatto.R;

public class AddFriendActivity extends AppCompatActivity {

    LinearLayout llSearchResult, llProgressBar;
    TextView tvSearchNameResult;
    Button btnSearchUser, btnAddFriend;
    ImageView imgSearchPPResult;
    EditText edtUsername;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        context = this;

        getSupportActionBar().setTitle("Add Friend");

        initializeViews();
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
                String username = edtUsername.getText().toString();

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore
                        .collection("users")
                        .document(username)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        llProgressBar.setVisibility(View.GONE);
                        if (documentSnapshot.exists()) {
                            llSearchResult.setVisibility(View.VISIBLE);
                            String nameResult = (String) documentSnapshot.get("name");

                            tvSearchNameResult.setText(nameResult);
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
                showToast("Feature not implemented yet", Toast.LENGTH_SHORT);

                // Adding friend to each user's list

                // Finishing the activity
                // finish();
            }
        });
    }

    public void showToast(String message, int duration){
        Toast.makeText(context, message, duration).show();
    }
}