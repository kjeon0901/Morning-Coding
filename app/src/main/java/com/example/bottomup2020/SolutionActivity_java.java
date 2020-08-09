package com.example.bottomup2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bottomup2020.Problems.Problems_java;

public class SolutionActivity_java extends SolutionToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_java);
    }

    public void onClick(View view) {
        // 누른 버튼에서 텍스트 가져오기
        Button btn = (Button) view;
        String btn_text = btn.getText().toString();

        Intent intent = new Intent(getApplicationContext(), Problems_java.class);

        intent.putExtra("button_number_java", btn_text);
        intent.putExtra("language_name_java", "JAVA");

        startActivity(intent);
    }
}