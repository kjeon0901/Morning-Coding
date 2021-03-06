
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
import android.widget.LinearLayout;
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


public class LockScreenActivity extends AppCompatActivity  {

    TextView Time;
    TextView Date;
    TextView textView,problem_text;
    RadioButton one,two,three;
    Button answer_btn;
    String answer,email;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/M/d");    // 날짜 포맷
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Cursor cursor;
    private DBHelper dbHelper=new DBHelper(this);
    DBHelper dbHelper(){ return this.dbHelper; }
    String shared = "file";
    boolean javaState,pythonState,cState;
    //sdf = new SimpleDateFormat("yyyy/MM/dd");
    //String date = sdf.format(new Date(System.currentTimeMillis()));

    Calendar cal = new GregorianCalendar();
    //String timeGre = String.format("%d:%d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
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

        showSelectLanguage(); // 3개의 언어가 각각 선택되어 있는지 체크
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
                        answer_btn.setEnabled(false);
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
                        answer_btn.setEnabled(false);
                    }
                }
                else if(two.isChecked()){
                    if(answer.equals("2번")){
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                        answer_btn.setEnabled(false);
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
                        answer_btn.setEnabled(false);
                    }
                }
                else{
                    if(answer.equals("3번")){
                        Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_LONG).show();
                        answer_btn.setEnabled(false);
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
                        answer_btn.setEnabled(false);
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
                    String favouriteProblem="#"+textView.getText().toString()+" ";
                    favouriteProblem= favouriteProblem.replace("번","");
                    dbHelper().updateFavourite(email, favouriteProblem);
                }else {
                    String favouriteProblem = cursor.getString(4) + "#" + textView.getText().toString() + " ";
                    favouriteProblem = favouriteProblem.replace("번","");
                    dbHelper().updateFavourite(email, favouriteProblem);
                }
            }
        });

        // 밀어서 잠금해제!!
        LinearLayout view = (LinearLayout) findViewById(R.id.swipe_layout);
        view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // Whatever
//                Intent mainActivity = new Intent(Intent.ACTION_MAIN);
//                mainActivity.addCategory(Intent.CATEGORY_HOME);
//                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(mainActivity);
                finish();

                // finish();하면 잠금화면 전 화면으로 돌아가고 주석처리한 코드 하면 home으로 돌아감.
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
        if(checkDuplicate('P')){
            pythonState=true;
        }
        else{
            pythonState=false;
        }
        if(checkDuplicate('C')){
            cState=true;
        }
        else{
            cState=false;
        }
    }
    private void textSet() { //txt 나누기

        String txt = readRandomTxt(javaState,pythonState,cState);
        String[] array = txt.split("#"); // 문제 구분
        //System.out.println(array[0]);

        if(array[0].charAt(0)=='C'){//C면 ##으로 구분
            array=txt.split("##");
        }

        int i =(int)(Math.random()*3);//문제번호는 0~3
        i=i*2;//문제번호는 0,2,4,6
        while (i < array.length) {
            textView.setText(array[i] + "번");
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
    private String readRandomTxt(boolean javaState,boolean pythonState, boolean cState) {
        // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
        // txt 파일을 InpuStream에 넣는다. (open 한다)
        //반복문안에서 랜덤 난수를 생성하여 선택된 언어가 우리가 선택한 언어인경우에만 txt를 읽는다.
        String readData;
        while(true) {
            int num = (int) (Math.random() * 3);  // 0~2 사이의 난수 발생
            try {
                if (num == 0) { //num이 0이면 java txt 가져옴
                    InputStream fis = getResources().openRawResource(R.raw.java_mcproblems);
                    byte[] data = new byte[fis.available()];
                    while(fis.read(data)!=-1){;}
                    if(javaState==false){
                        continue;
                    }
                    readData = new String(data);
                    break;
                } else if (num == 1) { //num이 1이면 python txt 가져옴
                    InputStream fis = getResources().openRawResource(R.raw.python_mcproblems);
                    byte[] data = new byte[fis.available()];
                    while(fis.read(data)!=-1){;}
                    if (pythonState == false) {
                        continue;
                    }
                    readData = new String(data);
                    break;
                } else { //num이 2이면 c txt 가져옴
                    InputStream fis = getResources().openRawResource(R.raw.c_mcproblems);
                    byte[] data = new byte[fis.available()];
                    while(fis.read(data)!=-1){;}
                    if(cState==false){
                        continue;
                    }
                    readData = new String(data);
                    break;
                }
            } catch (IOException e) {
                readData = "failed read";
                e.printStackTrace();
            }
        }
        return readData;
    }

    private void timeset() {
        Time = findViewById(R.id.Time);
        Date = findViewById(R.id.Date);

        Date dd = new Date();
        String date = mFormat.format(dd);

        long now = System.currentTimeMillis();
        Date tt = new Date(now);
        String time = sdf.format(tt);

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