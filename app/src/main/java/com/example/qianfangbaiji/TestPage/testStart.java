package com.example.qianfangbaiji.TestPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.selectmode;

public class testStart extends AppCompatActivity {
    Button buttonBack, buttonStart;
    EditText Edit;
    boolean flag = false;
    int[] array;
    int max;
    private Cursor c;
    SQLiteDatabase db1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testsetting);

        buttonBack =  findViewById(R.id.button_back);
        buttonStart = findViewById(R.id.btn_start);
        Edit = findViewById(R.id.search_box);

        // 返回上一页
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(testStart.this, selectmode.class);
            startActivity(intent);
        });
        // 注意，如果用户不输入数字，则默认为？
        buttonStart.setOnClickListener(v -> {
            String text = Edit.getText().toString();
            if(text.trim().equals("")){
                max = 0;
            }
            else max = Integer.valueOf(text);
            if(max <= 0  || max >= 100){
                Toast.makeText(testStart.this, "请输入1-100范围内数字", Toast.LENGTH_SHORT).show();
                flag = false;
            }
            else{
                //开始对全部条文进行扫描
                flag = true;
                //重置array
                array = new int[max];
                db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
                c = db1.rawQuery(String.format("SELECT id FROM fangge ORDER BY RANDOM() LIMIT %d", max), null);
                int number = 0;
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    array[number] = c.getInt(c.getColumnIndex("id"));
                    number++;
                    c.moveToNext();
                }
            }
            if(flag){
                SharedPreferences.Editor editor = getSharedPreferences("test_prefs", MODE_PRIVATE).edit();
//                    更新键值对
                editor.putInt("number", array.length);
                for(int i=0;i<array.length;i++){
                    editor.putInt("array"+ i, array[i]);
                }
                editor.apply();
                Intent intent=new Intent(testStart.this, testPage.class);//从SpendingActivity页面跳转至ExpenseProcesActivity页面
                intent.putExtra("now", 0);
                startActivity(intent);
            }
        });
    }
}