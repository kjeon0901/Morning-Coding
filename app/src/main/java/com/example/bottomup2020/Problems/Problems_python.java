package com.example.bottomup2020.Problems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottomup2020.FavouritesListActivity;
import com.example.bottomup2020.R;
import com.example.bottomup2020.SolutionActivity_c;
import com.example.bottomup2020.SolutionActivity_java;
import com.example.bottomup2020.SolutionActivity_python;

import java.io.IOException;
import java.io.InputStream;

public class Problems_python extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems_python);

        Intent intent = getIntent();

        TextView textView = (TextView) findViewById(R.id.problem_name);

        String language_name_python = intent.getExtras().getString("language_name_python");
        String button_number_python = intent.getExtras().getString("button_number_python");

        textView.setText(language_name_python + "  " + button_number_python + "번");

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        TextView problem_text = (TextView) findViewById(R.id.problem_text);
        TextView problem_solution = (TextView) findViewById(R.id.problem_solution);

        String s = readTxt();
        String[] array = s.split("#"); // 문제 구분
        // System.out.println(array[0]);


        int i = 0;
        while(i < array.length){
            String name_compare = "PYTHON  "+ button_number_python;
            if(array[i].equals(name_compare)){
                String[] str = array[i+1].split("\\|\\|");   // 선지 구분
                problem_text.setText(str[0]);
                button1.setText(str[1]);
                button2.setText(str[2]);
                button3.setText(str[3]);
                problem_solution.setText("답 : " + str[4]);
                if(str.length > 5){
                    problem_solution.append(str[5]);
                    problem_solution.setTextSize(15);
                }
                break;
            }

            i += 2;
        }
        problem_text.setMovementMethod(new ScrollingMovementMethod());
        problem_solution.setMovementMethod(new ScrollingMovementMethod());



    }




    // txt에서 String 추출
    private String readTxt(){
        // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
        // txt 파일을 InpuStream에 넣는다. (open 한다)
        String readData;

        try {

            InputStream fis = getResources().openRawResource(R.raw.python_mcproblems);

            byte[] data = new byte[fis.available()];

            while(fis.read(data)!=-1){;}

            readData = new String(data);

        } catch (IOException e) {

            readData = "failed read";

            e.printStackTrace();

        }

        return readData;

    }
//
//    int countClick_num = 0;
//
//    public void onClick(View view) {
//        ImageButton imageBtn = findViewById(R.id.star_on_btn);
//        countClick_num ++;
//        // 클릭 홀수면 별 없어진걸로!
//        if(countClick_num % 2 != 0){
//            imageBtn.setImageResource(R.drawable.star_off);
//            countClick_num = 1;
//        }else{
//            imageBtn.setImageResource(R.drawable.star_on);
//        }
//
//    }

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