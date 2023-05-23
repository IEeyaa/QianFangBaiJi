package com.example.qianfangbaiji.MemoryPage;

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

import java.util.ArrayList;
import java.util.List;

//getArray
//用户指定要学习的条文数目后，程序根据Anki算法求出用户要学习的条文序号，并将其存入到array中，传输到下一级
public class memoryStart extends AppCompatActivity {
    Button buttonBack, buttonStart, buttonLoad;
    EditText Edit;
    boolean flag = false;
    boolean arrayIsFull = false;
    int[] array;
    int[] q_array;
    int max;
    private Cursor c;
    SQLiteDatabase db1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorysetting);

        buttonBack =  findViewById(R.id.btn_back);
        buttonStart = findViewById(R.id.btn_start);
        buttonLoad = findViewById(R.id.btn_load);
        Edit = findViewById(R.id.Edit);

        // 返回上一页
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryStart.this, selectmode.class);
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
        // 注意，如果用户不输入数字，则默认为？
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {String text = Edit.getText().toString();
                if(text.trim().equals("")){
                    max = 0;
                }
                else max = Integer.valueOf(text);
                if(max <= 0  || max >= 100){
                    Toast.makeText(memoryStart.this, "请输入1-100范围内数字", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                else{
                    //开始对全部条文进行扫描
                    flag = true;
                    //重置array
                    array = new int[max];
                    q_array = new int[max];
                    db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
                    c = db1.rawQuery("SELECT id, ispassed FROM fangge", null);
                    int number = 0;
                    c.moveToFirst();
                    //完成列表，列表中为对应的数字，那么应该先给出一个数组来记录所有应当的条数，方式是遍历其中的值，收藏和删除的数据表可以不用了;
                    int nowCondition;
                    //表中所含的值：iscut iscollect	ispassed nowdate maxdate times EF
                    while (!c.isAfterLast()) {
                        //根据ispassed值来判断
                        nowCondition = c.getInt(c.getColumnIndex("ispassed"));
                        if (nowCondition  == 0 || nowCondition  == 3){
                            array[number] = c.getInt(c.getColumnIndex("id"));
                            q_array[number] = 0;
                            number++;
                            if(number == max){
                                arrayIsFull = true;
                                break;
                            }
                        }
                        c.moveToNext();
                    }
                    if (!arrayIsFull) {
                        max = number;
                    }
                }
                if(flag){
                    SharedPreferences.Editor editor = getSharedPreferences("memory_prefs", MODE_PRIVATE).edit();
//                    更新键值对
                    editor.putInt("number", array.length);
                    for(int i=0;i<array.length;i++){
                        editor.putInt("array"+ i, array[i]);
                        editor.putInt("q"+ i, 0);
                    }
                    editor.apply();
                    Intent intent=new Intent(memoryStart.this, memoryPage.class);//从SpendingActivity页面跳转至ExpenseProcesActivity页面
                    intent.putExtra("now", 0);
                    startActivity(intent);
                }
            }
        });
    }
}