package com.example.qianfangbaiji.MemoryPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;

//根据数据库创建
public class memoryPage extends AppCompatActivity {
    Button btn_back, btn_left, btn_right;

    TextView fangge_id, fangge_name, fangge_infor, fangge_from, fangge_content, now_number, pre_number;
    int now, edge, max;
    int fangge_number, q_number, q_number_new;
    TextView submit;

    Button []star = new Button[4];


    private void init(){
        btn_back = findViewById(R.id.btn_back);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);

        submit = findViewById(R.id.memory_submit);

        star[0] = findViewById(R.id.star1);
        star[1]  = findViewById(R.id.star2);
        star[2]  = findViewById(R.id.star3);
        star[3]  = findViewById(R.id.star4);


        now_number = findViewById(R.id.now_number);
        pre_number = findViewById(R.id.pre_number);
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
        now = Global.now;
        edge = Global.edge;
        fangge_number = Global.fangge_info_array.get(now)[0];
        q_number = Global.fangge_info_array.get(now)[1];
        q_number_new = q_number;
        max = Global.number;

        setContentView(R.layout.memorypage);
        init();
        //左右切换按钮的显示与否
        if(now >= max-1){
            btn_right.setEnabled(false);
            btn_right.setVisibility(View.INVISIBLE);
            submit.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
        }
        if(now == edge){
            btn_left.setEnabled(false);
            btn_left.setVisibility(View.INVISIBLE);
        }
        // 返回上一页
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.LazyStore(now);
                Intent intent = new Intent(memoryPage.this,memoryStart.class);
                startActivity(intent);
            }
        });
        // 前往上一个条文
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.fangge_info_array.get(now)[1] = q_number_new;
                Global.now -= 1;
                Global.LazyStore(now);
                Intent intent = new Intent(memoryPage.this, memoryPage.class);
                startActivity(intent);
            }
        });
        // 前往下一个条文
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.fangge_info_array.get(now)[1] = q_number_new;
                Global.now += 1;
                Global.LazyStore(now);
                Intent intent = new Intent(memoryPage.this, memoryPage.class);
                startActivity(intent);
            }
        });

        //恢复上一次评级
        for(int i=0;i<4;i++) star[i].setAlpha(1.0f);
        if(q_number > 0){
            for(int i=0;i<4;i++) star[i].setAlpha(0.2f);
            star[q_number - 1].setAlpha(1.0f);
        }

        // 评级标志
        star[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) star[i].setAlpha(0.2f);
                star[0].setAlpha(1.0f);
                q_number_new = 1;
            }
        });
        star[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) star[i].setAlpha(0.2f);
                star[1].setAlpha(1.0f);
                q_number_new = 2;
            }
        });
        star[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) star[i].setAlpha(0.2f);
                star[2].setAlpha(1.0f);
                q_number_new = 3;
            }
        });
        star[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) star[i].setAlpha(0.2f);
                star[3].setAlpha(1.0f);
                q_number_new = 4;
            }
        });

        // 结束
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.stage = 2;
                Global.fangge_info_array.get(now)[1] = q_number_new;
                Global.now = edge;
                Global.LazyStore(now);
                Intent intent = new Intent(memoryPage.this, testPage.class);
                startActivity(intent);
            }
        });

        Cursor c = MySQLHelper.getInstance().sqlSelect(
                String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number));
        c.moveToFirst();
        Fangge fangge_item = new Fangge(c);
        c.close();

        // 展示
        fangge_id.setText(String.format("方歌: %d/%d", fangge_item.id, Global.total_number));
        fangge_name.setText(fangge_item.infor);
        fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
        fangge_content.setText(fangge_item.content);
        fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));
        //        展示
        // 获取全局持久变量
        int newDailyNumber;
        int preDailyNumber;
        switch(Global.stage){
//          复习上一轮
            case 0:
                newDailyNumber = Global.daily_new_number;
                preDailyNumber = Global.edge - Global.now;
                break;
//          新的学习
            case 1:
                preDailyNumber = Global.now - Global.edge;
                newDailyNumber = Global.number - Global.now;
                break;
//          第一轮复习
            case 2:
                preDailyNumber = Global.daily_new_number;
                newDailyNumber = 0;
                break;
//          第二轮复习
            case 3:
                preDailyNumber = Global.number - Global.now;
                newDailyNumber = 0;
                break;
            default:
                preDailyNumber = -1;
                newDailyNumber = -1;
                break;
        }
        now_number.setText(String.format("需新学 %d", newDailyNumber));
        pre_number.setText(String.format("需复习 %d", preDailyNumber));
    }
}