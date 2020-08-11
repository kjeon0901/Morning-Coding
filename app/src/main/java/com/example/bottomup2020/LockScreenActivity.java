package com.example.bottomup2020;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
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

        textset();

    }

    private void textset() {
        Intent intent = getIntent();

        TextView textView = (TextView) findViewById(R.id.lock_problem_name);

        String language_name_lock = intent.getExtras().getString("language_name_java");
        String button_number_lock = intent.getExtras().getString("button_number_java");

        textView.setText(language_name_lock + "  " + button_number_lock + "번");


        Button button1 = (Button) findViewById(R.id.lock_radiobutton1);
        Button button2 = (Button) findViewById(R.id.lock_radiobutton2);
        Button button3 = (Button) findViewById(R.id.lock_radiobutton3);
        TextView problem_text = (TextView) findViewById(R.id.lock_problem_text);

        String s = readTxt();
        String[] array = s.split("#"); // 문제 구분
        // System.out.println(array[0]);


        int i = 0;
        while (i < array.length) {
            String name_compare = "JAVA  " + button_number_lock;
            if (array[i].equals(name_compare)) {
                String[] str = array[i + 1].split("\\|\\|");   // 선지 구분
                problem_text.setText(str[0]);
                button1.setText(str[1]);
                button2.setText(str[2]);
                button3.setText(str[3]);
                break;
            }

            i += 2;
        }
        problem_text.setMovementMethod(new ScrollingMovementMethod());
    }

    private void timeset() {
        Time = (TextView) findViewById(R.id.Time);
        Date = (TextView) findViewById(R.id.Date);

        Time.setText(time);
        Date.setText(date);


    }

    // txt에서 String 추출
    private String readTxt() {
        // getResources().openRawResource()로 raw 폴더의 원본 파일을 가져온다.
        // txt 파일을 InpuStream에 넣는다. (open 한다)
        String readData;

        try {

            InputStream fis = getResources().openRawResource(R.raw.java_mcproblems);

            byte[] data = new byte[fis.available()];

            while (fis.read(data) != -1) {
                ;
            }

            readData = new String(data);

        } catch (IOException e) {

            readData = "failed read";

            e.printStackTrace();

        }

        return readData;

    }
}