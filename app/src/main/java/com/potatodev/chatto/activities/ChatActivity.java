package com.potatodev.chatto.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.potatodev.chatto.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}