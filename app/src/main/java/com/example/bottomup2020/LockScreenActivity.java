package com.example.bottomup2020;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        Time = (TextView)findViewById(R.id.Time);
        Date = (TextView)findViewById(R.id.Date);

        Time.setText(time);
        Date.setText(date);

        final ImageButton imageBtn = findViewById(R.id.star_off_btn);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            // 즐겨찾기 별 누르고 이벤트
            @Override
            public void onClick(View v) {
                imageBtn.setImageResource(R.drawable.star_on);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}