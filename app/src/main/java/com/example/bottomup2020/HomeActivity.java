package com.example.bottomup2020;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    String nickName,email,language,number,imagePath,solvedProblem;
    ImageView imageView5;
    TextView userName;
    Cursor cursor;
    Bitmap bitmap;

    private MainActivity mainActivity=new MainActivity() ;
    private DBHelper dbHelper=new DBHelper(this);

    private MainActivity main() { return mainActivity; }
    private DBHelper dbHelper(){ return dbHelper; }

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView5 = findViewById(R.id.imageView5);
        userName = findViewById(R.id.userName);

        Intent intent = getIntent();
        ArrayList<String> data = (ArrayList<String>) intent.getSerializableExtra("profile");
        nickName = data.get(0);
        email = data.get(1);
        imagePath=data.get(2);

        language="C";
        number="JAVA 01";
        solvedProblem = "JAVA 02";
        userName.setText(nickName);

        if(imagePath!=null) {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imagePath);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();

            try {
                mThread.join(); // 메인 스레드는 별도의 작업 완료전까지 대기
                imageView5.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean found = false;

        //db에 데이터 있는지 검사
        cursor = dbHelper().getAllData();
        while (cursor.moveToNext()) {
            if (email.equals(cursor.getString(2))) {
                found = true;
                break;
            }
        }

        //db에 없으면 데이터 추가
        if (found == false) {
            cursor.moveToFirst();
            dbHelper().insertData(nickName, email, language, number,solvedProblem);
            cursor = dbHelper().getOneData(email);
        }

        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String email = cursor.getString(2);
        String language = cursor.getString(3);
        String num = cursor.getString(4);
        String solvedProblem = cursor.getString(5);


        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + num+" | "+solvedProblem);

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
            setTitle("MorningCoding");
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

    int btn_click_num = 0;

    public void onClick(View view) {
        btn_click_num++;
        Button btn;
        // 버튼 id가져오기
        switch (view.getId())
        {
            case R.id.java_button:
                // 버튼 JAVA 눌렀을 때 처리
                btn = findViewById(R.id.java_button);
                if(btn_click_num %2 != 0) { // 클릭횟수가 홀수면
                    btn.setBackgroundResource(R.drawable.btn_margin);
                    btn_click_num = 1;
                }else {
                    btn.setBackgroundResource(R.drawable.button_shape);
                    btn_click_num = 2;
                }
                break;
            case R.id.python_button:
                btn = findViewById(R.id.python_button);
                if(btn_click_num %2 != 0) { // 클릭횟수가 홀수면
                    btn.setBackgroundResource(R.drawable.btn_margin);
                    btn_click_num = 1;
                } else {
                    btn.setBackgroundResource(R.drawable.button_shape);
                    btn_click_num = 2;
                }
                break;
            case R.id.c_button:
                btn = findViewById(R.id.c_button);
                if(btn_click_num %2 != 0) { // 클릭횟수가 홀수면
                    btn.setBackgroundResource(R.drawable.btn_margin);
                    btn_click_num = 1;
                } else {
                    btn.setBackgroundResource(R.drawable.button_shape);
                    btn_click_num = 2;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}