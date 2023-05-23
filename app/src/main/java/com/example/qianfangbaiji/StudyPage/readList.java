package com.example.qianfangbaiji.StudyPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.R;

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
@SuppressLint("Registered")
public class readList extends AppCompatActivity {
    //创建一个List对象来存储数据
    List<Fangge> list = new ArrayList<>();
    Button btn_back, btn_search;
    EditText Edit;
    SQLiteDatabase db1;
    Cursor c;

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForList myAdapter = new MyAdapterForList(list, this, db1, "read");
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readlist);
        btn_back = findViewById(R.id.btn_back);
        btn_search = findViewById(R.id.btn_search);
        Edit = findViewById(R.id.Edit);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(readList.this, selectmode.class);
                startActivity(intent);
            }
        });


        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Edit.getText().toString();
                c = db1.rawQuery(String.format("SELECT * FROM fangge WHERE infor LIKE '%%%s%%' " +
                        "or content LIKE '%%%s%%'", str, str), null);
                c.moveToFirst();
                list.clear();
                while (!c.isAfterLast()) {
                    list.add(new Fangge(c));
                    c.moveToNext();
                }
                c.close();
                initView();
            }
        });

        c = db1.rawQuery("SELECT * FROM fangge WHERE iscut = 0", null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list.add(new Fangge(c));
            c.moveToNext();
        }
        c.close();
        initView();
    }
}

