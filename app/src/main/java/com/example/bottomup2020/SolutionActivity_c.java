package com.example.bottomup2020;


import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;


public class SolutionActivity_c extends SolutionToolbarActivity {

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_c);

    }


    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), FavouritesActivity.class);
        startActivity(intent);
    }


}