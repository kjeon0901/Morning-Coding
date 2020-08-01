package com.example.bottomup2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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
            String nickname=et_nickname.getText().toString();
            /**   try {

           //저장 경로
                File saveFile = new File("/storage/emulated/0" + "/nicknameSaver");
                String str="/storage/emulated/0" + "/nicknameSaver";
                if(!saveFile.exists()){ // 폴더 없을 경우
                    saveFile.mkdir(); // 폴더 생성
                }

                long now = System.currentTimeMillis(); // 현재시간 받아오기
                Date date = new Date(now); // Date 객체 생성
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = sdf.format(date);

                File file= new File("/storage/self/primary/nicknameSaver/nickname.txt");
                if(!file.exists()){ // 폴더 없을 경우
                    Log.e("zz","zz");
                }
                FileWriter fw= new FileWriter("/storage/self/primary/nicknameSaver/nickname.txt");

                fw.write(nickname);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
            //객체 생성
            Intent intent=new Intent(MK_profileActivity.this,HomeActivity.class);
            intent.putExtra("nickname",nickname);//데이터를 담는다.
            startActivity(intent); //액티비티 이름
        }
    });
  }
}