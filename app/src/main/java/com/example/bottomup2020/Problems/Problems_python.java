package com.example.bottomup2020.Problems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottomup2020.FavouritesListActivity;
import com.example.bottomup2020.R;
import com.example.bottomup2020.SolutionActivity_c;
import com.example.bottomup2020.SolutionActivity_java;
import com.example.bottomup2020.SolutionActivity_python;

public class Problems_python extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems_python);

        Intent intent = getIntent();

        TextView textView = (TextView) findViewById(R.id.problem_name);

        String language_name_python = intent.getExtras().getString("language_name_python");
        String button_number_python = intent.getExtras().getString("button_number_python");

        textView.setText(language_name_python + "   " + button_number_python + "번");
    }

    int countClick_num = 0;

    public void onClick(View view) {
        ImageButton imageBtn = findViewById(R.id.star_on_btn);
        countClick_num ++;
        // 클릭 홀수면 별 없어진걸로!
        if(countClick_num % 2 != 0){
            imageBtn.setImageResource(R.drawable.star_off);
            countClick_num = 1;
        }else{
            imageBtn.setImageResource(R.drawable.star_on);
        }

    }

    @Override
    public void setContentView(int layoutResID){
        LinearLayout fullView = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_home_toolbar, null);
        FrameLayout activityContainer = (FrameLayout)fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        Toolbar toolbar=(Toolbar)findViewById(R.id.c_toolbar);
        //툴바 사용여부 결정(기본=사용)
        if(useToolbar()){
            setSupportActionBar(toolbar);

            Intent intent = getIntent();

            String language_name_python = intent.getExtras().getString("language_name_python");
            String button_number_python = intent.getExtras().getString("button_number_python");

            setTitle(language_name_python + "   " + button_number_python + "번");

        }else{
            toolbar.setVisibility(View.GONE);
        }
    }

    //툴바 사용할지 말지 정함
    protected boolean useToolbar(){
        return true;
    }

    // R.menu.home_menu라는 코드로 res/menu/home_menu.xml에 있는 코드 읽어옴
    // 메뉴버튼을 눌렀을 때 보여줄 menu에 대해 정의
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    // 메뉴의 항목을 클릭했을 때 호출되는 콜백 메서드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // id가 subMenu_C인 메뉴를 누르면 SolutionActivity_c로 이동함.
            case R.id.subMenu_C:
                Intent intent = new Intent(this, SolutionActivity_c.class);
                startActivity(intent);
                break;
            case R.id.subMenu_JAVA:
                intent = new Intent(this, SolutionActivity_java.class);
                startActivity(intent);
                break;
            case R.id.subMenu_PYTHON:
                intent = new Intent(this, SolutionActivity_python.class);
                startActivity(intent);
                break;
            case R.id.FavoritesMenu:
                intent = new Intent(this, FavouritesListActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    // 메뉴가 화면에 보여질 때 마다 호출됨.
    // 메뉴를 눌렀을 때 이미 적용되어있어야 하는 정보들(ex) 로그인안하면 로그아웃 버튼 없게)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

}