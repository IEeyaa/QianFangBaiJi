package com.example.qianfangbaiji.DailyPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.CardPage.MemoryCardList;
import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.selectmode;

//主页，其中包含“每日一句”、“测试”、“学习”三个板块的接口
public class daily extends AppCompatActivity {
    int total_number = 206;
    int fangge_number = (int) (Math.random() * total_number);
    Button buttonToStudy, buttonToMemory, buttonToLogin;
    TextView fangge_from, fangge_content, fangge_info, fangge_name;
    // 数据库相关
    SQLiteDatabase database;
    Cursor cursor;

    @SuppressLint({"ResourceType", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily);

        // TextView
        fangge_name = findViewById(R.id.fangge_name);
        fangge_from = findViewById(R.id.fang_ge_source);
        fangge_content = findViewById(R.id.fang_ge_content);
        fangge_info = findViewById(R.id.fangge_infor);

        // 按钮与文字绑定
        buttonToStudy = findViewById(R.id.studyButton);
        buttonToMemory = findViewById(R.id.memoryButton);
        buttonToLogin= findViewById(R.id.loginButton);

        buttonToLogin.setOnClickListener(v -> {
            Global.inspiredCount();
            finish();
        });

        buttonToStudy.setOnClickListener(v -> {
            Intent intent=new Intent(daily.this, selectmode.class);
            startActivity(intent);
        });

        buttonToMemory.setOnClickListener(v -> {
            Intent intent=new Intent(daily.this, MemoryCardList.class);
            startActivity(intent);
        });

        // sql查询
        database = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        cursor.moveToFirst(); // 将光标移动到结果集的第一行
        // 结果提取
        Fangge fangge_item = new Fangge(cursor);
        // 展示
        fangge_name.setText(String.format("“%s”", fangge_item.info));
        fangge_content.setText(fangge_item.content);
        fangge_from.setText(String.format("——方歌来源：%s·%s", fangge_item.dynasty, fangge_item.book));
        fangge_info.setText(String.format("%s", fangge_item.table_name));
    }
}