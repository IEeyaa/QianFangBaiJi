package com.example.qianfangbaiji.TestPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class testPage extends AppCompatActivity {
    Button btn_back;

    TextView fangge_id, fangge_name, fangge_infor, fangge_from, fangge_content, now_number, pre_number;

    Button []answer = new Button[3];

    int now;
    SQLiteDatabase db1;

    private void init(){
        btn_back = findViewById(R.id.btn_back);


        answer[0] = findViewById(R.id.answer1);
        answer[1]  = findViewById(R.id.answer2);
        answer[2]  = findViewById(R.id.answer3);

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

        now = getIntent().getIntExtra("now", 0);
        SharedPreferences prefs = getSharedPreferences("test_prefs", MODE_PRIVATE);
        int fangge_number = prefs.getInt("array"+now, -1);
        final int max = prefs.getInt("number", -1);
        setContentView(R.layout.testpage);
        init();

        // 返回上一页
        btn_back.setOnClickListener(v -> {
        Intent intent = new Intent(testPage.this,testStart.class);
        startActivity(intent);
        });

        //  生成答案
        int question = (int) (Math.random() * Global.question_number);
        final int right_answer =  (int) (Math.random() * Global.answer_number);

        for(int i=0; i<Global.answer_number;i++){
            if(i != right_answer){
                answer[i].setOnClickListener(v -> {
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
                    v.setBackgroundColor(Color.rgb(199, 230, 203));
                    Intent intent;
                    if (now >= max - 1){
                        intent = new Intent(testPage.this, testReport.class);
                    }
                    else{
                        intent = new Intent(testPage.this, testPage.class);
                    }
                    intent.putExtra("now", now+1);
                    startActivity(intent);
                });
            }
        }
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        Cursor c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        c.moveToFirst();
        Fangge fangge_item = new Fangge(c);

        //        生成干扰项 4类问题
        //        fangge_name
        if (question == 0){
            String hint = "____?____";
            fangge_name.setText(hint);
            c = db1.rawQuery(String.format("SELECT * FROM %s WHERE infor != '%s' ORDER BY RANDOM()", "fangge", fangge_item.info), null);
            c.moveToFirst();
            for(int i=0;i<Global.answer_number;i++){
                if(i == right_answer){
                    answer[i].setText(fangge_item.info);
                }
                else{
                    answer[i].setText(c.getString(c.getColumnIndex("infor")));
                    c.moveToNext();
                }
            }
            fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
            fangge_content.setText(fangge_item.content);
            fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));
        }
        //        fangge_from
        else if (question == 1){
            String hint = "______?______";
            fangge_from.setText(hint);
            c = db1.rawQuery(String.format("SELECT * FROM %s WHERE book != '%s' ORDER BY RANDOM()", "fangge", fangge_item.book), null);
            c.moveToFirst();
            for(int i=0;i<Global.answer_number;i++){
                if(i == right_answer){
                    answer[i].setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
                }
                else{
                    answer[i].setText(String.format("%s·%s", c.getString(c.getColumnIndex("dynasty")),
                            c.getString(c.getColumnIndex("book"))));
                    c.moveToNext();
                }
            }
            fangge_name.setText(fangge_item.info);
            fangge_content.setText(fangge_item.content);
            fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));
        }
        //        fangge_content
        else if(question == 2){
            String hint = "__________________?__________________\n";
            int line_number = ((fangge_item.content).length()+1) / 17;
            int cut_line = (int) (Math.random() * line_number);
            String answer_word;
            if (cut_line == line_number - 1){
                answer_word = fangge_item.content.substring(cut_line*17, (cut_line+1)*17 - 1);
            }
            else{
                answer_word = fangge_item.content.substring(cut_line*17, (cut_line+1)*17);
            }
            String replace = fangge_item.content.replace(answer_word, hint);
            c = db1.rawQuery(String.format("SELECT * FROM %s WHERE id != %d ORDER BY RANDOM()", "fangge", fangge_number), null);
            c.moveToFirst();
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
            fangge_name.setText(fangge_item.info);
            fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
            fangge_content.setText(replace);
            fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));
        }
        else{
            String hint = "____?____";
            c = db1.rawQuery(String.format("SELECT * FROM %s WHERE table_name != '%s' ORDER BY RANDOM()", "fangge", fangge_item.table_name), null);
            c.moveToFirst();
            for(int i=0;i<Global.answer_number;i++){
                if(i == right_answer){
                    answer[i].setText(fangge_item.table_name);
                }
                else{
                    answer[i].setText(c.getString(c.getColumnIndex("table_name")));
                    c.moveToNext();
                }
            }
            fangge_name.setText(fangge_item.info);
            fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
            fangge_content.setText(fangge_item.content);
            fangge_infor.setText(String.format("治法：%s", hint));
        }

        //        展示
        now_number.setText(String.format("当前进度:%d/%d", now+1, max));
        pre_number.setText("");
        c.close();
    }
}