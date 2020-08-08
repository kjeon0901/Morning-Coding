package com.example.bottomup2020;


import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class SolutionActivity_c extends SolutionToolbarActivity {

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_c);

    }


    public void onClick(View view) {
        // 누른 버튼에서 텍스트 가져오기
        Button btn = (Button) view;
        String btn_text = btn.getText().toString();

        Intent intent = new Intent(getApplicationContext(), FavouritesActivity.class);

        intent.putExtra("button_number", btn_text);
        intent.putExtra("language_name", "C");

        startActivity(intent);
    }


}