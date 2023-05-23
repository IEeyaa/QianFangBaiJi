package com.example.qianfangbaiji.MemoryPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

//根据数据库创建
public class memoryPage extends AppCompatActivity {
    Button btn_back, btn_left, btn_right;

    TextView fangge_id, fangge_name, fangge_infor, fangge_from, fangge_content, now_number;

    TextView submit, clearStar;

    Button []star = new Button[5];

    int now;
    private Cursor c;
    SQLiteDatabase db1;

    private void init(){
        btn_back = findViewById(R.id.btn_back);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);

        submit = findViewById(R.id.memory_submit);

        clearStar =findViewById(R.id.btn_starClear);
        star[0] = findViewById(R.id.star1);
        star[1]  = findViewById(R.id.star2);
        star[2]  = findViewById(R.id.star3);
        star[3]  = findViewById(R.id.star4);
        star[4]  = findViewById(R.id.star5);


        now_number = findViewById(R.id.now_number);
        fangge_id = findViewById(R.id.fangge_id);
        fangge_name = findViewById(R.id.fangge_name);
        fangge_infor = findViewById(R.id.fangge_infor);
        fangge_from = findViewById(R.id.fangge_from);
        fangge_content = findViewById(R.id.fangge_content);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        now = getIntent().getIntExtra("now", 0);

        SharedPreferences prefs = getSharedPreferences("memory_prefs", MODE_PRIVATE);

        int fangge_number = prefs.getInt("array"+now, -1);
        final int q_number = prefs.getInt("q" + now, -1);
        final int[] q_number_new = {q_number};
        final int max = prefs.getInt("number", -1);

        setContentView(R.layout.memorypage);
        init();
        //左右切换按钮的显示与否
        if(now >= max-1){
            btn_right.setEnabled(false);
            btn_right.setVisibility(View.INVISIBLE);
            submit.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
        }
        if(now == 0){
            btn_left.setEnabled(false);
            btn_left.setVisibility(View.INVISIBLE);
        }
        // 返回上一页
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(memoryPage.this,memoryStart.class);
            startActivity(intent);
            }
        });
        // 前往上一个条文
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("memory_prefs", MODE_PRIVATE).edit();
//                    更新键值对
                editor.putInt("q"+ now, q_number_new[0]);
                editor.apply();
                Intent intent = new Intent(memoryPage.this, memoryPage.class);
                intent.putExtra("now", now-1);
                startActivity(intent);
            }
        });
        // 前往下一个条文
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("memory_prefs", MODE_PRIVATE).edit();
//                    更新键值对
                editor.putInt("q"+ now, q_number_new[0]);
                editor.apply();
                Intent intent = new Intent(memoryPage.this, memoryPage.class);
                intent.putExtra("now", now+1);
                startActivity(intent);
            }
        });

        //恢复上一次评级
        for(int i=0;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
        for(int i = 0; i<q_number; i++) star[i].setBackgroundResource(R.drawable.collec2);

        // 评级相关
        clearStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
                q_number_new[0] = 0;
            }
        });

        // 评级标志
        star[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star[0].setBackgroundResource(R.drawable.collec2);
                for(int i=1;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
                q_number_new[0] = 1;
            }
        });
        star[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<2;i++) star[i].setBackgroundResource(R.drawable.collec2);
                for(int i=2;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
                q_number_new[0] = 2;
            }
        });
        star[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<3;i++) star[i].setBackgroundResource(R.drawable.collec2);
                for(int i=3;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
                q_number_new[0] = 3;
            }
        });
        star[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) star[i].setBackgroundResource(R.drawable.collec2);
                for(int i=4;i<5;i++) star[i].setBackgroundResource(R.drawable.collec);
                q_number_new[0] = 4;
            }
        });
        star[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<5;i++) star[i].setBackgroundResource(R.drawable.collec2);
                q_number_new[0] = 5;
            }
        });


        // 结束
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryPage.this, memoryReport.class);
                startActivity(intent);
            }
        });


        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        c.moveToFirst();
        Fangge fangge_item = new Fangge(c);
        c.close();

        //        展示
        fangge_id.setText(String.format("方歌: %d/%d", fangge_item.id, Global.total_number));
        fangge_name.setText(fangge_item.infor);
        fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
        fangge_content.setText(fangge_item.content);
        fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));
        now_number.setText(String.format("当前进度:%d/%d", now+1, max));
    }
}