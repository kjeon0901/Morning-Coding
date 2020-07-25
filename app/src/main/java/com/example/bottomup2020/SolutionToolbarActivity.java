package com.example.bottomup2020;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SolutionToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_solution);
    }

    @Override
    public void setContentView(int layoutResID){
        LinearLayout fullView = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_toolbar_solution, null);
        FrameLayout activityContainer = (FrameLayout)fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        Toolbar toolbar=(Toolbar)findViewById(R.id.c_toolbar);
        //툴바 사용여부 결정(기본=사용)
        if(useToolbar()){
            switch (layoutResID){
                case R.layout.activity_solution_c:
                    toolbar.setSubtitle("C");
                    break;
                case R.layout.activity_solution_java:
                    toolbar.setSubtitle("JAVA");
                    break;
                case R.layout.activity_solution_python:
                    toolbar.setSubtitle("PYTHON");
                    break;
                default:
                    break;
            }
            setSupportActionBar(toolbar);
            setTitle("언어별 풀이보기");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        }else{
            toolbar.setVisibility(View.GONE);
        }
    }

    //툴바 사용할지 말지 정함
    protected boolean useToolbar(){
        return true;
    }

    //메뉴 등록하기
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.solution_menu, menu);
        return true;
    }

    //앱바 메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            // id가 subMenu_C인 메뉴를 누르면 SolutionActivity_c로 이동함.
            case R.id.SolutionCMenu:
                Intent intent = new Intent(this, SolutionActivity_c.class);
                startActivity(intent);
                return true;
            case R.id.SolutionJavaMenu:
                intent = new Intent(this, SolutionActivity_java.class);
                startActivity(intent);
                return true;
            case R.id.SolutionPythonMenu:
                intent = new Intent(this, SolutionActivity_python.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }


}
