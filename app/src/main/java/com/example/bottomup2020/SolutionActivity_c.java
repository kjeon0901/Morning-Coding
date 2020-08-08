package com.example.bottomup2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toolbar;

public class SolutionActivity_c extends SolutionToolbarActivity {

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_c);

    }


    public void onClick(View view) {
        Button btn;
        // 버튼 id가져오기
        switch (view.getId())
        {
            case R.id.c_button1:
                // 버튼 c_button1 눌렀을 때 처리
                btn = findViewById(R.id.c_button1);
                Intent intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                break;
            case R.id.c_button2: case R.id.c_button3: case R.id.c_button4:
            case R.id.c_button5: case R.id.c_button6: case R.id.c_button7:
            case R.id.c_button8: case R.id.c_button9: case R.id.c_button10:
            case R.id.c_button11: case R.id.c_button12: case R.id.c_button13:
            case R.id.c_button14: case R.id.c_button15: case R.id.c_button16:
            case R.id.c_button17: case R.id.c_button18: case R.id.c_button19:
            case R.id.c_button20: case R.id.c_button21: case R.id.c_button22:
            case R.id.c_button23: case R.id.c_button24: case R.id.c_button25:
            case R.id.c_button26: case R.id.c_button27: case R.id.c_button28:
            case R.id.c_button29: case R.id.c_button30: case R.id.c_button31:
            case R.id.c_button32: case R.id.c_button33: case R.id.c_button34:
            case R.id.c_button35:
                intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }


}