package com.example.bottomup2020;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MK_profileActivity extends AppCompatActivity {
    EditText et_nickname;
    Button btn_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_k_profile);

        et_nickname=findViewById(R.id.et_nickname);
        btn_nickname=findViewById(R.id.btn_nickname);

        //확인 버튼 눌렀을 경우
        btn_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = et_nickname.getText().toString();
                // 파일 생성
                File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/nicknameSaver"); // 저장 경로
                // 폴더 생성
                if(!saveFile.exists()){ // 폴더 없을 경우
                    saveFile.mkdir(); // 폴더 생성
                }
                try {
                    long now = System.currentTimeMillis(); // 현재시간 받아오기
                    Date date = new Date(now); // Date 객체 생성
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = sdf.format(date);

                    BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/nickname.txt", true));
                    buf.append(nowTime + " "); // 날짜 쓰기
                    buf.append(str); // 파일 쓰기
                    buf.newLine(); // 개행
                    buf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}