package com.example.bottomup2020;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText number;
    TextView result;
    Button insert;
    Button select;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        email=findViewById(R.id.email);
        number=findViewById(R.id.number);
        insert=findViewById(R.id.insert);
        select=findViewById(R.id.select);
        result = (TextView) findViewById(R.id.result);
        dbHelper=new DBHelper(this);

        //데이터 삽입
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String userName=name.getText().toString();
                 String userEmail=email.getText().toString();
                 String userNum=number.getText().toString();

                if(dbHelper.insertData(userName,userEmail,userNum)){
                    Toast.makeText(getApplicationContext(),"insert 성공",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"insert 실패",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //데이터 조회하기
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = dbHelper.getAllData();
                while(cursor.moveToNext()){
                    int no = cursor.getInt(0);
                    String name= cursor.getString(1);
                    String email = cursor.getString(2);
                    String num= cursor.getString(3);
                    result.setText(no+" "+name+" "+email+" "+num);
                }
            }
        });
    }
}

class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MorningCoding";
    public static final String TABLE_NAME= "userProfile";
    public static final String COL_1="ID";
    public static final String COL_2="name";
    public static final String COL_3="email";
    public static final String COL_4="number";

    public DBHelper(@Nullable Context context){
        super(context,DATABASE_NAME,null,2);

    }

    //실행할 때 테이블 최초 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT , email TEXT, number TEXT)");
    }

    //버전 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //테이블에 정보 넣기
    public boolean insertData(String name, String email, String number){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,email);
        contentValues.put(COL_4,number);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    //데이터 조회
    public Cursor getAllData(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }

    //데이터 수정
    public boolean updateData(String id,String name,String email,String number){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,email);
        contentValues.put(COL_4,number);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{id});
        return true;
    }
    //테이블 삭제
    public int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[] {id});
    }


}