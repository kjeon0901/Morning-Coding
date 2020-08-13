package com.example.bottomup2020;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bottomup2020.List.ListViewAdapter;
import com.example.bottomup2020.List.ListViewItem;

import java.util.ArrayList;

public class FavouritesListActivity extends AppCompatActivity {
    private ListView listview;
    ArrayList<ListViewItem> list = new ArrayList<ListViewItem>();
    ListViewAdapter adapter;
    Button delBtn;

    DBHelper dbHelper = new DBHelper(this);
    public DBHelper dbHelper(){return this.dbHelper;}
    String favouriteProblem;
    String email = HomeActivity.showEmail();
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);
        ListViewItem item = new ListViewItem();

        listview = (ListView) findViewById(R.id.listview);
        delBtn = (Button) findViewById(R.id.delete);

        cursor=dbHelper().getAllData();
        while (cursor.moveToNext()) {
            if (email.equals(cursor.getString(2))) {
                break;
            }
        }
        adapter = new ListViewAdapter();

        if(cursor.getString(4)!=null) {
            email = cursor.getString(4);
            String[] favouriteNum = email.split("#");
            for (int i = 1; i < favouriteNum.length; i++) {
                String number[] = favouriteNum[i].split("  ");
                adapter.addItem(number[0], number[1] + "번");
            }
        }
//        listview.setAdapter(adapter);
//
//        item.setTitle("JAVA");
//        item.setContent("01번");
//
//        list.add(item);
      /*  adapter.addItem("JAVA", "01번");
        adapter.addItem("JAVA", "02번");
        adapter.addItem("JAVA", "03번");
        adapter.addItem("JAVA", "04번");

        adapter.addItem("C", "01번");
        adapter.addItem("C", "02번");
        adapter.addItem("C", "03번");
        adapter.addItem("C", "04번");

        adapter.addItem("PYTHON", "01번");
        adapter.addItem("PYTHON", "02번");
        adapter.addItem("PYTHON", "03번");
        adapter.addItem("PYTHON", "04번");
*/

        listview.setAdapter(adapter);

        // 아이템 클릭시 작동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ListViewItem item = (ListViewItem) listview.getAdapter().getItem(position);

                String title_text = item.getTitle();
                String content_text = item.getContent().replace("번","");

                Intent intent = new Intent(getApplicationContext(), FavouritesActivity.class);

                intent.putExtra("language_name_favourites", title_text);
                intent.putExtra("button_number_favourites", content_text);

                startActivity(intent);
            }
        });

        adapter.notifyDataSetChanged(); // 어댑터의 변경을 알림.
//
//        delBtn = (Button) findViewById(R.id.delete);
//        delBtn.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                int pos = Integer.parseInt(v.getTag().toString());
//                Context context = getParent();
//                Toast.makeText(context, "Item in position " + pos + " clicked", Toast.LENGTH_LONG).show();
////                final ListViewItem remove = listViewItemList.remove(pos);
////                listViewItemList.set(pos,remove);
//                // listViewItemList.get(0);
//                adapter.remove(pos);
                //ListViewAdapter adapter = new ListViewAdapter();
                // adapter.notifyDataSetChanged();
//                int count, checked ;
//                count = adapter.getCount() ;
//
//                if (count > 0) {
//                    // 현재 선택된 아이템의 position 획득.
//                    checked = listview.getCheckedItemPosition();
//
//                    if (checked > -1 && checked < count) {
//                        // 아이템 삭제
//                        list.remove(checked) ;
//
//                        // listview 선택 초기화.
//                        listview.clearChoices();
//
//                        // listview 갱신.
//                        adapter.notifyDataSetChanged();
//                    }
//                }
           }
//       }) ;
//    }

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
            setTitle("즐겨찾는 문제");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
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
//            case R.id.FavoritesMenu:
//                intent = new Intent(this, FavouritesListActivity.class);
//                startActivity(intent);
//                break;
        }
        return true;
    }

    // 메뉴가 화면에 보여질 때 마다 호출됨.
    // 메뉴를 눌렀을 때 이미 적용되어있어야 하는 정보들(ex) 로그인안하면 로그아웃 버튼 없게)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //menu.getItem(2).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }




}