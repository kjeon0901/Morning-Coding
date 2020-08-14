package com.example.bottomup2020;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.bottomup2020.HomeActivity.email;

public class ListViewAdapter extends BaseAdapter {

    TextView titleTextView;
    TextView contentTextView;
    Cursor cursor;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        titleTextView = (TextView) convertView.findViewById(R.id.title);
        contentTextView = (TextView) convertView.findViewById(R.id.content);
        final Button delbtn = (Button) convertView.findViewById(R.id.delete);

        final ListViewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        contentTextView.setText(listViewItem.getContent());

        delbtn.setTag(position);
        final DBHelper dbHelper = new DBHelper(context);
        cursor=dbHelper.getAllData();
        while (cursor.moveToNext()) {
            if (email.equals(cursor.getString(2))) {
                break;
            }
        }
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(v.getTag().toString());
                String problemName= listViewItemList.get(pos).getTitle()+"  "+listViewItemList.get(pos).getContent();
                Toast.makeText(context, problemName + "이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                listViewItemList.remove(pos);
                notifyDataSetChanged();
                problemName = problemName.replace("번","");
                String favouriteProblem= cursor.getString(4);
                String favouriteArr[]= favouriteProblem.split("#");
                for(int i=0;i<favouriteArr.length;i++){
                    if(favouriteArr[i].equals(problemName)){
                        problemName="#"+problemName;
                        favouriteProblem= cursor.getString(4).replace(problemName,"");
                        dbHelper.updateFavourite(email,favouriteProblem);
                        break;
                    }
                }
            }
        });

        return convertView;
    }


    // 지정한 위치에 있는 데이터 리턴
    @Override
    public ListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String title, String content) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setContent(content);

        listViewItemList.add(item);
    }

    public void remove(int pos) {
        String t = listViewItemList.get(pos).getTitle();
        String c = listViewItemList.get(pos).getContent();

        ListViewItem item = new ListViewItem();
        item.setTitle("송이");
        item.setContent("바보");

        listViewItemList.add(item);

    }

}
class subClass extends AppCompatActivity {
    DBHelper dbHelper = new DBHelper(this);
    public DBHelper dbHelper(){
        return this.dbHelper;
    };
}
//
//    public void removeItem(String title, String content){
//        ListViewItem item = new ListViewItem(title, content);
//
//        item.setTitle(title);
//        item.setContent(content);
//
//        listViewItemList.remove(item);
//    }

