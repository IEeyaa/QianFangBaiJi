package com.example.qianfangbaiji.MemoryPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.selectmode;


//getArray
//用户指定要学习的条文数目后，程序根据Anki算法求出用户要学习的条文序号，并将其存入到array中，传输到下一级
public class memoryStart extends AppCompatActivity {
    Button buttonBack, buttonStart, buttonLoad;
    TextView totalWordNumber, preWordNumber, progressInfo, textHint, countDown, buttonSelect;
    ProgressBar progressBar;
    int newDailyNumber;
    int preDailyNumber;
    int nowOverNumber;
    int totalNumber;
    private Cursor c;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorysetting);

        // 全局变量加载
        Global.initMemory();

        buttonBack =  findViewById(R.id.btn_back);
        buttonStart = findViewById(R.id.btn_start);
        buttonLoad = findViewById(R.id.btn_load);
        buttonSelect = findViewById(R.id.btn_select);

        // 用户需要新学的，固定数量
        textHint = findViewById(R.id.textHint);
        countDown = findViewById(R.id.countDown);
        totalWordNumber = findViewById(R.id.totalWordNumber);
        preWordNumber = findViewById(R.id.preWordNumber);
        progressInfo = findViewById(R.id.progressInfo);
        progressBar = findViewById(R.id.progressBar);

        // 获取全局持久变量
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
        nowOverNumber = Global.already_over_number;
        totalNumber = Global.total_number;
        if(Global.stage == 4){
            textHint.setText("今日任务已完成");
            totalWordNumber.setText("0");
            preWordNumber.setText("0");
            buttonStart.setText("再来一轮");
        }
        else{
            totalWordNumber.setText(String.valueOf(newDailyNumber));
            preWordNumber.setText(String.valueOf(preDailyNumber));
        }
        progressBar.setProgress(nowOverNumber);
        progressInfo.setText(String.format("%d/%d", nowOverNumber, totalNumber));
        countDown.setText(String.format("剩余%d天", (totalNumber - nowOverNumber) / Global.daily_new_number));

        // 返回上一页
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryStart.this, selectmode.class);
                startActivity(intent);
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryStart.this, memorySelect.class);
                startActivity(intent);
            }
        });
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryStart.this, memoryLoad.class);
                startActivity(intent);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(Global.stage == 4){
                    Global.RestartInfo();
                }
                switch(Global.stage){
                    case 0:
                        int question = (int) (Math.random() * 2);
                        if(question == 0) intent=new Intent(memoryStart.this, testPage.class);
                        else intent=new Intent(memoryStart.this, testPage2B.class);
                        break;
                    case 2:
                        intent=new Intent(memoryStart.this, testPage.class);
                        break;
                    case 3:
                        intent=new Intent(memoryStart.this, testPage2B.class);
                        break;
                    default:
                        intent=new Intent(memoryStart.this, memoryPage.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}