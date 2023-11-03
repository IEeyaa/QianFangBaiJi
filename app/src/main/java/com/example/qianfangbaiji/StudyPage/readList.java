package com.example.qianfangbaiji.StudyPage;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
@SuppressLint("Registered")
public class readList extends AppCompatActivity {
    //创建一个List对象来存储数据
    List<Fangge> list = new ArrayList<>();
    Button backButton, searchButton;
    EditText searchBox;
    Cursor c;

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForList myAdapter = new MyAdapterForList(list, this,"read");
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readlist);

        // Buttons
        backButton = findViewById(R.id.button_back);
        searchButton = findViewById(R.id.search_button);

        // EditText
        searchBox = findViewById(R.id.search_box);

        backButton.setOnClickListener(v -> finish());

        searchButton.setOnClickListener(v -> {
            String str = searchBox.getText().toString();
            c = MySQLHelper.getInstance().sqlSelect(String.format("SELECT * FROM fangge WHERE infor LIKE '%%%s%%' " +
                    "or content LIKE '%%%s%%'", str, str));
            c.moveToFirst();
            list.clear();
            while (!c.isAfterLast()) {
                list.add(new Fangge(c));
                c.moveToNext();
            }
            c.close();
            initView();
        });

        // 这里的初始化List<Fangge>可以抽象出一个函数func(Cursor) -> List<Fangge>
        c = MySQLHelper.getInstance().sqlSelect("SELECT * FROM fangge WHERE iscut = 0");
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list.add(new Fangge(c));
            c.moveToNext();
        }
        c.close();
        initView();
    }
}

