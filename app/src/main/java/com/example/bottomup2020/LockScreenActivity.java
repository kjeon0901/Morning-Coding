package com.example.bottomup2020;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
    Button button1,button2,button3;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String time = sdf.format(new Date(System.currentTimeMillis()));
    //sdf = new SimpleDateFormat("yyyy/MM/dd");
    //String date = sdf.format(new Date(System.currentTimeMillis()));

    Calendar cal = new GregorianCalendar();
    //String timeGre = String.format("%d:%d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE));
    String date = String.format("%d/%d/%d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    TextView tViewLock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        textView = (TextView) findViewById(R.id.lock_problem_name);
        button1 = (Button) findViewById(R.id.lock_radiobutton1);
        button2 = (Button) findViewById(R.id.lock_radiobutton2);
        button3 = (Button) findViewById(R.id.lock_radiobutton3);
        problem_text = (TextView) findViewById(R.id.lock_problem_text);

        timeset();
        tViewLock = (TextView) findViewById(R.id.lock_problem_text);
        tViewLock.setMovementMethod(new ScrollingMovementMethod());

        final ImageButton imageBtn = findViewById(R.id.lock_star_off_btn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            // 즐겨찾기 별 누르고 이벤트
            @Override
            public void onClick(View v) {
                imageBtn.setImageResource(R.drawable.star_on);
            }
        });

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.requestDismissKeyguard(this, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
         | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        textSet();

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

    private void timeset() {
        Time = (TextView) findViewById(R.id.Time);
        Date = (TextView) findViewById(R.id.Date);

        Time.setText(time);
        Date.setText(date);
    }
}