package com.example.bottomup2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);


    }
}