package com.example.bottomup2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    String nickName,email,language,favouriteProblem,imagePath,solvedProblem;
    ImageView imageView5;
    TextView userName, solved, correct;
    Cursor cursor;
    Bitmap bitmap;
    Button java_btn , python_btn, c_btn;
    int solvedNum=0;
    int correctNum=0;
    boolean found=false;
    private DBHelper dbHelper=new DBHelper(this);
    private DBHelper dbHelper(){ return dbHelper; }
    Switch switchButton;
    String Shared= "file";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        java_btn = findViewById(R.id.java_button);
        python_btn = findViewById(R.id.python_button);
        c_btn = findViewById(R.id.c_button);

        SharedPreferences sharedPreferences = getSharedPreferences(Shared,0);
        String javaState= sharedPreferences.getString("javaState","");
        String pythonState= sharedPreferences.getString("pythonState","");
        String cState= sharedPreferences.getString("cState","");

        if(javaState.equals("selected")){
            java_btn.setBackgroundResource(R.drawable.btn_margin);
        }
        if(pythonState.equals("selected")){
            python_btn.setBackgroundResource(R.drawable.btn_margin);
        }
        if(cState.equals("selected")){
            c_btn.setBackgroundResource(R.drawable.btn_margin);
        }

        imageView5 = findViewById(R.id.imageView5);
        userName = findViewById(R.id.userName);
        solved=findViewById(R.id.solved);
        correct = findViewById(R.id.correct);

        //스위치 버튼
        switchButton = (Switch) findViewById(R.id.sb_use_listener);
        //switchButton.setChecked(true);
        CheckSwitchState();

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckSwitchState();
            }

        });

        Intent intent = getIntent();
        ArrayList<String> data = (ArrayList<String>) intent.getSerializableExtra("profile");
        nickName = data.get(0);
        email = data.get(1);
        imagePath=data.get(2);

        //문제들은 #붙여서 변수에 저장해주기
        favouriteProblem="#JAVA 01 ";
        solvedProblem = "#JAVA 01 #JAVA 02 #JAVA 03";
        language="JAVA PYTHON C";
        userName.setText(nickName);

        //카카오톡 프로필 사진 설정하기
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

        //#기준으로 푼 문제 문자열 잘라서 개수세기  
        String str =solvedProblem;
        String[] txtArr= str.split("#");
        solvedNum=txtArr.length-1;
        solved.setText(String.valueOf(solvedNum));

        //문제를 맞혔을 경우
        //correctNum++;
        //correct.setText(correctNum);

        //즐겨찾기 버튼 누르면


        //db에 데이터 있는지 검사
        cursor=dbHelper().getAllData();
        while (cursor.moveToNext()) {
            if (email.equals(cursor.getString(2))) {
                found = true;
                break;
            }
        }

        //db에 없으면 데이터 추가
        if (found == false) {
            cursor.moveToFirst();
            dbHelper().insertData(nickName, email, language, favouriteProblem,solvedProblem);
            cursor = dbHelper().getOneData(email);
        }
        //id  name email 선택한 언어  즐겨찾기문제 푼 문제
        id = cursor.getInt(0);
        String name = cursor.getString(1);
        String email = cursor.getString(2);
        String language = cursor.getString(3);
        String favouriteProblem = cursor.getString(4);
        String solvedProblem = cursor.getString(5);

        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);

    }

    private void CheckSwitchState() {

        //스위치버튼이 체크되었는지 검사하여 각 경우에 맞게..
        if(switchButton.isChecked()){
            Intent intent = new Intent(getApplicationContext(), ScreenService.class);
            startService(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), ScreenService.class);
            stopService(intent);
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
            case R.id.LogoutMenu:
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intentHome= new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intentHome);

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
    int btn_java = 0;
    int btn_python = 0;
    int btn_c = 0;

    public void onClick(View view){
        // 버튼 id가져오기
        switch (view.getId())
        {
            case R.id.java_button:
                ++btn_java;
                if(btn_java==1){ //버튼 취소
                    java_btn.setBackgroundResource(R.drawable.button_shape);
                    if(checkDuplicate('J')){
                        language= cursor.getString(3).replace("JAVA","");
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                else if(btn_java==2){ //다시 클릭
                    btn_java=0;
                    java_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('J')) {
                        language = cursor.getString(3)+" JAVA";
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;

            case R.id.python_button:
                ++btn_python;
                if(btn_python==1){ //버튼 취소
                    python_btn.setBackgroundResource(R.drawable.button_shape);
                    if(checkDuplicate('P')){
                        language= cursor.getString(3).replace("PYTHON","");
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }

                else if(btn_python==2){ //다시 클릭
                    btn_python=0;
                    python_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('P')) {
                        language = cursor.getString(3)+" PYTHON";
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;
            case R.id.c_button:
                ++btn_c;
                if(btn_c==1){ //버튼 취소
                    c_btn.setBackgroundResource(R.drawable.button_shape);
                    if(checkDuplicate('C')){
                        language= cursor.getString(3).replace("C","");
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                else if(btn_c==2){ //다시 클릭
                    btn_c=0;
                    c_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('C')) {
                        language = cursor.getString(3)+" C";
                        dbHelper().updateData(email, language, favouriteProblem, solvedProblem);
                        System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences= getSharedPreferences(Shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value;

        if(checkDuplicate('J')){
            value="selected";
           editor.putString("javaState",value);
        }
        else{
            value="unselected";
            editor.putString("javaState",value);
        }
        if(checkDuplicate('P')){
            value="selected";
            editor.putString("pythonState",value);
        }
        else{
            value="unselected";
            editor.putString("pythonState",value);
        }
        if(checkDuplicate('C')){
            value="selected";
            editor.putString("cState",value);
        }
        else{
            value="unselected";
            editor.putString("cState",value);
        }
        editor.commit(); //save 완료
    }

    protected boolean checkDuplicate(char s){
        boolean check = false;
        int languageLength;
        languageLength = language.length(); // language 문자열 처음부터 끝까지 확인

        for(int i=0; i<languageLength; i++){
            if(language.charAt(i)==s){
                check=true;
                return check;
            }
        }
        return check;
    }
}