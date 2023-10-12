package com.example.qianfangbaiji.MemoryPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;


public class memorySelect extends AppCompatActivity{
    Button buttonStart;
    TextView totalWordNumber, preWordNumber, progressInfo, countDown, buttonReStart;
    NumberPicker numberPicker, timePicker;
    ProgressBar progressBar;
    int nowOverNumber;
    int totalNumber;

    int temp_dailyNumber;
    int temp_day;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoryselect);

        buttonStart = findViewById(R.id.btn_start);
        buttonReStart = findViewById(R.id.btn_restart);

        // 用户需要新学的，固定数量
        nowOverNumber = Global.already_over_number;
        totalNumber = Global.total_number;


        countDown = findViewById(R.id.countDown);
        totalWordNumber = findViewById(R.id.totalWordNumber);
        preWordNumber = findViewById(R.id.preWordNumber);
        progressInfo = findViewById(R.id.progressInfo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(nowOverNumber);

        numberPicker = findViewById(R.id.numberPicker);
        timePicker = findViewById(R.id.timePicker);

        progressInfo.setText(String.format("%d/%d", nowOverNumber, totalNumber));
        countDown.setText(String.format("剩余%d天", (totalNumber - nowOverNumber) / Global.daily_new_number));

        numberPicker.setMaxValue((totalNumber - nowOverNumber) / 5);
        numberPicker.setMinValue(1);
        numberPicker.setValue(Global.daily_new_number / 5);
        temp_dailyNumber = Global.daily_new_number;
        NumberPicker.Formatter formatterNumber = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 5;
                return temp + "个";
            }
        };

        NumberPicker.Formatter formatterTime = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "天";
            }
        };
        numberPicker.setFormatter(formatterNumber);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                temp_dailyNumber = newVal * 5;
                temp_day = (int) Math.ceil((totalNumber - nowOverNumber) / temp_dailyNumber);
                timePicker.setValue(temp_day);
            }
        });

        timePicker.setMaxValue((totalNumber - nowOverNumber) / 5);
        timePicker.setMinValue(1);
        timePicker.setValue((totalNumber - nowOverNumber) / Global.daily_new_number);
        timePicker.setFormatter(formatterTime);
        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                temp_day = newVal;
                temp_dailyNumber = (totalNumber - nowOverNumber) / temp_day / 5 * 5;
                numberPicker.setValue(temp_dailyNumber / 5);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Global.daily_new_number = temp_dailyNumber;
                Global.RestartInfo();
                intent=new Intent(memorySelect.this, memoryStart.class);
                startActivity(intent);
            }
        });

        buttonReStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.ReStart();
                Global.daily_new_number = temp_dailyNumber;
                Global.RestartInfo();
                Intent intent;
                intent=new Intent(memorySelect.this, memorySelect.class);
                startActivity(intent);
            }
        });
    }
}