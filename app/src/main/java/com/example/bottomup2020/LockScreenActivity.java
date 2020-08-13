package com.example.bottomup2020;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class LockScreenActivity extends AppCompatActivity {

    TextView Time;
    TextView Date;
    TextView textView,problem_text;
    RadioButton one,two,three;
    Button answer_btn;
    String answer,email;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String time = sdf.format(new Date(System.currentTimeMillis()));
    Cursor cursor;
    private DBHelper dbHelper=new DBHelper(this);
    DBHelper dbHelper(){ return this.dbHelper; }
    String shared = "file";
    boolean javaState,pythonState,cState;
    //sdf = new SimpleDateFormat("yyyy/MM/dd");
    //String date = sdf.format(new Date(System.currentTimeMillis()));

    Calendar cal = new GregorianCalendar();
    //String timeGre = String.format("%d:%d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
    String date = String.format("%d/%d/%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    TextView tViewLock;
    public static int correctNum=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        SharedPreferences sharedPreferences = getSharedPreferences(shared,0);
        correctNum =sharedPreferences.getInt("solvedNum",0);
        textView = findViewById(R.id.lock_problem_name);
        one =  findViewById(R.id.one);
        two =  findViewById(R.id.two);
        three =  findViewById(R.id.three);
        problem_text =  findViewById(R.id.lock_problem_text);
        answer_btn=findViewById(R.id.answer);

        timeset();
        tViewLock =  findViewById(R.id.lock_problem_text);
        tViewLock.setMovementMethod(new ScrollingMovementMethod());

       email = HomeActivity.showEmail();
        cursor=dbHelper().getAllData();
        while (cursor.moveToNext()) {
            if (email.equals(cursor.getString(2))) {
                break;
            }
        }

        final ImageButton imageBtn = findViewById(R.id.lock_star_off_btn);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.requestDismissKeyguard(this, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
         | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        showSelectLanguage();

        textSet(); //문제 띄우기
        answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = cursor.getString(0);
                String nickName = cursor.getString(1);
                String email= cursor.getString(2);
                String language = cursor.getString(3);
                String favouriteProblem=cursor.getString(4);
                String solvedProblem=cursor.getString(5);
                String correctProblem=cursor.getString(6);

                if(cursor.getString(5)==null){
                    solvedProblem = " #" +textView.getText().toString();
                    dbHelper().updateSolved(email,solvedProblem);
                }
                else {
                    solvedProblem = cursor.getString(5) + " #" + textView.getText().toString();
                    dbHelper().updateSolved(email,solvedProblem);
                }
                System.out.println(id + " | " + nickName + " | " + email + " | "+ language+ " | " + favouriteProblem +" | "+solvedProblem );
                System.out.println(correctNum);
                if(one.isChecked()){
                    if(answer.equals("1번")){
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                        if(cursor.getString(6)==null){
                            correctProblem = " #" +textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                        else {
                            correctProblem= cursor.getString(6) + " #" + textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"틀렸습니다. 정답은 "+answer ,Toast.LENGTH_LONG).show();
                    }
                }
                else if(two.isChecked()){
                    if(answer.equals("2번")){
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                        if(cursor.getString(6)==null){
                            correctProblem = " #" +textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                        else {
                            correctProblem= cursor.getString(6) + " #" + textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"틀렸습니다. 정답은 "+answer ,Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(answer.equals("3번")){
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                        if(cursor.getString(6)==null){
                            correctProblem = " #" +textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                        else {
                            correctProblem= cursor.getString(6) + " #" + textView.getText().toString();
                            dbHelper().updateCorrect(email,correctProblem);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"틀렸습니다. 정답은 "+answer ,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            // 즐겨찾기 별 누르고 이벤트
            @Override
            public void onClick(View v) {
                imageBtn.setImageResource(R.drawable.star_on);
                if(cursor.getString(4)==null){
                    dbHelper().updateFavourite(email, "#"+textView.getText().toString()+" ");
                }else {
                    String favouriteProblem = cursor.getString(4) + "#" + textView.getText().toString() + " ";
                    dbHelper().updateFavourite(email, favouriteProblem);
                }
            }
        });
    }

    private void showSelectLanguage(){
        if(checkDuplicate('J')){
            javaState=true;
        }
        else{
            javaState=false;
        }
        if(checkDuplicate('C')){
            pythonState=true;
        }
        else{
            pythonState=false;
        }
        if(checkDuplicate('P')){
            cState=true;
        }
        else{
            cState=false;
        }
    }
    private void textSet() { //txt 나누기

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
            one.setText(str[1]);
            two.setText(str[2]);
            three.setText(str[3]);
            answer=str[4];
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

    private void timeset() {
        Time = findViewById(R.id.Time);
        Date = findViewById(R.id.Date);

        Time.setText(time);
        Date.setText(date);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences=getSharedPreferences(shared,0);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //에디터 연결
        int num=correctNum;
        editor.putInt("solvedNum",num);
        editor.commit();
    }

}