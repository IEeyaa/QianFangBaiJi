package com.example.qianfangbaiji.DailyPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.CardPage.MemoryCardList;
import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.selectmode;
import com.example.qianfangbaiji.TestPage.testPage;

//主页，其中包含“每日一句”、“测试”、“学习”三个板块的接口
public class daily extends AppCompatActivity {
    int total_number = 206;
    int fangge_number = (int) (Math.random() * total_number);

//    数据库相关
    SQLiteDatabase db1;
    Cursor c;

    @SuppressLint({"ResourceType", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        获取xml链接
        setContentView(R.layout.daily);

//        组件动态绑定
        Button buttonToStudy, buttonToMemory;
        TextView fangge_from, fangge_content, fangge_infor;

        //按钮与文字绑定
        buttonToStudy = findViewById(R.id.studyButton);
        buttonToMemory = findViewById(R.id.memoryButton);


        fangge_from = findViewById(R.id.fangge_from);
        fangge_content = findViewById(R.id.fangge_content);
        fangge_infor = findViewById(R.id.fangge_infor);

        buttonToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(daily.this, selectmode.class);
                startActivity(intent);
            }
        });

        buttonToMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(daily.this, MemoryCardList.class);
                startActivity(intent);
            }
        });
        //      sql查询
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        c.moveToFirst();
        //      结果提取
        Fangge fangge_item = new Fangge(c);
        //        展示
        fangge_from.setText(String.format("方歌来源：%s·%s", fangge_item.dynasty, fangge_item.book));
        fangge_content.setText(fangge_item.content);
        fangge_infor.setText(String.format("——%s·%s",fangge_item.table_name,fangge_item.infor));
    }
}