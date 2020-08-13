package com.example.bottomup2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    String name,language,favouriteProblem,imagePath,solvedProblem,correctProblem;
    ImageView imageView5;
    TextView userName, solved, correct , correctPercent;
    Cursor cursor;
    Bitmap bitmap;
    Button java_btn , python_btn, c_btn;
    Switch lock;
    int solvedNum=0;
    int correctNum=0;
    boolean found=false;
    public static String email;
    private DBHelper dbHelper=new DBHelper(this);
    DBHelper dbHelper(){ return dbHelper; }

    String Shared= "file";
    int id;
    int btn_java = 1;
    int btn_python = 1;
    int btn_c = 1;
    TextView tViewLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        java_btn = findViewById(R.id.java_button);
        python_btn = findViewById(R.id.python_button);
        c_btn = findViewById(R.id.c_button);
        imageView5 = findViewById(R.id.imageView5);
        userName = findViewById(R.id.userName);
        solved=findViewById(R.id.solved);
        correct = findViewById(R.id.correct);
        correctPercent = findViewById(R.id.corrrectPercent);

        lock=findViewById(R.id.sb_use_listener);
        tViewLog = (TextView) findViewById(R.id.home_problem_text);
        tViewLog.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        ArrayList<String> data = (ArrayList<String>) intent.getSerializableExtra("profile");
        name = data.get(0);
        email = data.get(1);
        imagePath=data.get(2);
        //===================DB=====================
        // db에 데이터 있는지 검사
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
            favouriteProblem = null;
            solvedProblem = null;
            correctProblem=null;
            language="JAVA PYTHON C";
            dbHelper().insertData(name, email, language, favouriteProblem,solvedProblem,correctProblem);
            cursor = dbHelper().getOneData(email);
        }
        //==============================================
        userName.setText(name);

        dbHelper().addColumn();
        
        if(checkDuplicate('J')){
            java_btn.setBackgroundResource(R.drawable.btn_margin);
            btn_java=0;
        }
        if(checkDuplicate('P')){
            python_btn.setBackgroundResource(R.drawable.btn_margin);
            btn_python=0;
        }
        if(checkDuplicate('C')){
            c_btn.setBackgroundResource(R.drawable.btn_margin);
            btn_c=0;
        }

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
        //dbHelper().updateSolved(email,null);
        //dbHelper().updateCorrect(email,null);
        //푼문제가 0
        if(cursor.getString(5)==null){
            solved.setText("0");
            correct.setText("0");
        }
        else {
            //푼 문제 존재
            String solvedStr = cursor.getString(5);
            String[] txtArr = solvedStr.split("#");
            solvedNum = txtArr.length - 1;
            solved.setText(String.valueOf(solvedNum));
            if(cursor.getString(6)==null){ //맞은 문제 0
                correct.setText("0");
            }
            else{ //맞은 문제 존재
                String correctStr = cursor.getString(6);
                String[] txtArr2 = correctStr.split("#");
                correctNum = txtArr2.length-1;
                correct.setText(String.valueOf(correctNum));
            }
        }
        int percent= (int) (((double)correctNum/(double)solvedNum)*100);
        correctPercent.setText(String.valueOf(percent));

        //문제를 맞혔을 경우
        //correctNum++;
        //correct.setText(correctNum);
        //즐겨찾기 버튼 누르면
        //id  name email 선택한 언어  즐겨찾기문제 푼 문제
        id = cursor.getInt(0);
        name = cursor.getString(1);
        email = cursor.getString(2);
        language = cursor.getString(3);
        favouriteProblem = cursor.getString(4);
        solvedProblem = cursor.getString(5);
        correctProblem = cursor.getString(6);
        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem+ " | "+ correctProblem);

        textSet(); // 랜덤 오늘의 모닝코딩 출력
        lock.setChecked(true); //잠금화면은 항상 ON 으로
        Intent intentLock = new Intent(getApplicationContext(), ScreenService.class);
        startService(intentLock);

        //잠금화면 스위치
       lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){ //스위치 끔
                    Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                    stopService(intent);
                }
            }
        });
    }

    public static String showEmail(){
        return email;
    }

    private void textSet() { //txt 나누기
        TextView textView =  findViewById(R.id.home_problem_name);
        Button button1 = findViewById(R.id.home_radiobutton1);
        Button button2 = findViewById(R.id.home_radiobutton2);
        Button button3 =  findViewById(R.id.home_radiobutton3);
        TextView problem_text = findViewById(R.id.home_problem_text);

        String txt = readRandomTxt();
        String[] array = txt.split("#"); // 문제 구분
        //System.out.println(array[0]);

        if(array[0].charAt(0)=='C'){//C면 ##으로 구분
            array=txt.split("##");
        }

        int i =(int)(Math.random()*3);//문제번호는 0~3
        i=i*2;//문제번호는 0,2,4,6
        while (i < array.length) {
                textView.setText(array[i]);
                String[] str = array[i + 1].split("\\|\\|");   // 선지 구분
                problem_text.setText(str[0]);
                button1.setText(str[1]);
                button2.setText(str[2]);
                button3.setText(str[3]);
                break;
        }
        problem_text.setMovementMethod(new ScrollingMovementMethod());
    }

    // txt에서 String 추출
    private String readRandomTxt() {
        // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
        // txt 파일을 InpuStream에 넣는다. (open 한다)
        String readData;
        int num= (int)(Math.random()*3);  // 0~2 사이의 난수 발생
        try {
            if(num==0) { //num이 0이면 java txt 가져옴
                InputStream fis = getResources().openRawResource(R.raw.java_mcproblems);
                byte[] data = new byte[fis.available()];
                while (fis.read(data) != -1) {
                    ;
                }
                readData = new String(data);
            }
            else if(num==1){ //num이 1이면 python txt 가져옴
                InputStream fis = getResources().openRawResource(R.raw.python_mcproblems);
                byte[] data = new byte[fis.available()];
                while (fis.read(data) != -1) {
                    ;
                }
                readData = new String(data);
            }
            else{ //num이 2이면 c txt 가져옴
                InputStream fis = getResources().openRawResource(R.raw.c_mcproblems);
                byte[] data = new byte[fis.available()];
                while (fis.read(data) != -1) {
                    ;
                }
                readData = new String(data);
            }
        } catch (IOException e) {
            readData = "failed read";
            e.printStackTrace();
        }
        return readData;
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
            setTitle("Morning Coding");
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
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                else if(btn_java==2){ //다시 클릭
                    btn_java=0;
                    java_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('J')) {
                        language = cursor.getString(3)+ " JAVA";
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;
            case R.id.python_button:
                ++btn_python;
                if(btn_python==1){ //버튼 취소
                    python_btn.setBackgroundResource(R.drawable.button_shape);
                    if(checkDuplicate('P')){
                        language= cursor.getString(3).replace("PYTHON","");
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                else if(btn_python==2){ //다시 클릭
                    btn_python=0;
                    python_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('P')) {
                        language = cursor.getString(3)+" PYTHON";
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;
            case R.id.c_button:
                ++btn_c;
                if(btn_c==1){ //버튼 취소
                    c_btn.setBackgroundResource(R.drawable.button_shape);
                    if(checkDuplicate('C')){
                        language= cursor.getString(3).replace("C","");
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                else if(btn_c==2){ //다시 클릭
                    btn_c=0;
                    c_btn.setBackgroundResource(R.drawable.btn_margin);
                    if(!checkDuplicate('C')) {
                        language = cursor.getString(3)+" C";
                        dbHelper().updateLanguage(email, language);
                        System.out.println(id + " | " + name + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem);
                    }
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //스위치 저장
        SharedPreferences sharedPreferences = getSharedPreferences(Shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("checked",true);
        editor.commit();
    }
    protected boolean checkDuplicate(char s){
        boolean check = false;
        int languageLength;
        languageLength = cursor.getString(3).length(); // language 문자열 처음부터 끝까지 확인
        for(int i=0; i<languageLength; i++){
            if(cursor.getString(3).charAt(i)==s){
                check=true;
                return check;
            }
        }
        return check;
    }
}