package com.example.debtsettler_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class zgodovina extends AppCompatActivity {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zgodovina);

        String token = SharedPrefManager.getInstance(this).tokenValue();
    }
}