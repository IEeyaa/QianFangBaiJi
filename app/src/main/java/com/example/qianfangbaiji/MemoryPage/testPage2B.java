package com.example.qianfangbaiji.MemoryPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

//根据数据库创建
public class testPage2B extends AppCompatActivity {
    Button btn_back;

    TextView fangge_id, fangge_name, fangge_infor, fangge_from, fangge_content, now_number, pre_number;

    Button []answer = new Button[3];

    int now, edge, max;
    int fangge_number, q_number, test_q_add = 1;
    SQLiteDatabase db1;

    private void init(){
        btn_back = findViewById(R.id.button_back);


        answer[0] = findViewById(R.id.answer1);
        answer[1]  = findViewById(R.id.answer2);
        answer[2]  = findViewById(R.id.answer3);

        now_number = findViewById(R.id.now_number);
        pre_number = findViewById(R.id.pre_number);
        fangge_id = findViewById(R.id.fangge_id);
        fangge_name = findViewById(R.id.fangge_name);
        fangge_infor = findViewById(R.id.fangge_infor);
        fangge_from = findViewById(R.id.fang_ge_source);
        fangge_content = findViewById(R.id.fang_ge_content);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 当前序列号
        // 当前序列号
        now = Global.now;
        edge = Global.edge;
        fangge_number = Global.fangge_info_array.get(now)[0];
        q_number = Global.fangge_info_array.get(now)[1];
        max = Global.number;

        setContentView(R.layout.testpage2b);
        init();

        // 返回上一页
        btn_back.setOnClickListener(v -> {
        Intent intent = new Intent(testPage2B.this, memoryStart.class);
        startActivity(intent);
        });

        final int right_answer =  (int) (Math.random() * Global.answer_number);

        for(int i=0; i<Global.answer_number;i++){
            if(i != right_answer){
                answer[i].setOnClickListener(v -> {
                    if(test_q_add > -1){
                        test_q_add -= 1;
                    }
                    float originalX = v.getX();
                    float originalY = v.getY();
                    Animation shake = new TranslateAnimation(0, 10, 0, 0);
                    shake.setDuration(500);
                    shake.setInterpolator(new CycleInterpolator(5));
                    v.startAnimation(shake);
                    v.setX(originalX);
                    v.setY(originalY);
                });
            }
            else{
                answer[i].setOnClickListener(v -> {
                    if(now < edge){
                        q_number = 2 + test_q_add * 2;
                    }
                    else{
                        q_number += test_q_add;
                    }
                    v.setBackgroundColor(Color.rgb(199, 230, 203));
                    Intent intent;
                    Global.now++;
                    if(Global.stage == 0){
                        if (now == edge - 1){
                            Global.stage = 1;
                            Global.fangge_info_array.get(now)[1] = q_number;
                            Global.LazyStore(now);
                            intent = new Intent(testPage2B.this, memoryPage.class);
                        }
                        else{
                            Global.fangge_info_array.get(now)[1] = q_number;
                            Global.LazyStore(now);
                            intent = new Intent(testPage2B.this, testPage2B.class);
                        }
                    }
                    else{
                        if (now == max - 1){
                            Global.now = 0;
                            Global.stage = 4;
                            Global.fangge_info_array.get(now)[1] = q_number;
                            Global.LazyStore(now);
                            intent = new Intent(testPage2B.this, memoryReport.class);
                        }
                        else{
                            Global.fangge_info_array.get(now)[1] = q_number;
                            Global.LazyStore(now);
                            intent = new Intent(testPage2B.this, testPage2B.class);
                        }
                    }
                    startActivity(intent);
                });
            }
        }
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        Cursor c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        c.moveToFirst();
        Fangge fangge_item = new Fangge(c);
        int line_number = ((fangge_item.content).length()+1) / 17;
        int cut_line = (int) (Math.random() * line_number);
        String answer_word;
        if (cut_line == line_number - 1){
            answer_word = fangge_item.content.substring(cut_line*17, (cut_line+1)*17 - 1);
        }
        else{
            answer_word = fangge_item.content.substring(cut_line*17, (cut_line+1)*17);
        }
        c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id != %d ORDER BY RANDOM()", "fangge", fangge_number), null);
        c.moveToFirst();
//        生成干扰项
        for(int i=0;i<Global.answer_number;i++){
            if(i == right_answer){
                answer[i].setText(answer_word);
            }
            else{
                int temp_line_number = (c.getString(c.getColumnIndex("content")).length() + 1) / 17;
                int temp_cut_line = (int) (Math.random() * temp_line_number);
                answer[i].setText(c.getString(c.getColumnIndex("content")).substring(temp_cut_line*17, (temp_cut_line+1)*17-1));
                c.moveToNext();
            }
        }
        //        展示
        fangge_name.setText(fangge_item.info);
        fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
        c.close();
        // 获取全局持久变量
        int newDailyNumber;
        int preDailyNumber;
        switch(Global.stage){
            case 0:
                newDailyNumber = Global.daily_new_number;
                preDailyNumber = Global.edge - Global.now;
                break;
            case 1:
                preDailyNumber = Global.now - Global.edge;
                newDailyNumber = Global.number - Global.now;
                break;
            case 2:
                preDailyNumber = Global.daily_new_number;
                newDailyNumber = 0;
                break;
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