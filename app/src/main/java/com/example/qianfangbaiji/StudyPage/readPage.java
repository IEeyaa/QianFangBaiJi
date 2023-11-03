package com.example.qianfangbaiji.StudyPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

//根据数据库创建
@SuppressLint("Registered")
public class readPage extends AppCompatActivity {
    Button backButton, searchButton, starButton, leftButton, rightButton;
    EditText searchBox;
    TextView fangGeID, fangGeName, fangGeSource, fangGeContent, fangGeInfo;
    SQLiteDatabase database;
    boolean starred = true;
    int total_number = Global.total_number, fangge_number;



    //初始化
    private void init(){
        setContentView(R.layout.readpage);

        // Buttons
        backButton = findViewById(R.id.button_back);
        leftButton = findViewById(R.id.btn_left);
        rightButton = findViewById(R.id.btn_right);
        starButton = findViewById(R.id.btn_collect);
        searchButton = findViewById(R.id.search_button);

        // EditText
        searchBox = findViewById(R.id.search_box);

        // TextView
        fangGeID = findViewById(R.id.fangge_id);
        fangGeName = findViewById(R.id.fangge_name);
        fangGeInfo = findViewById(R.id.fangge_infor);
        fangGeSource = findViewById(R.id.fang_ge_source);
        fangGeContent = findViewById(R.id.fang_ge_content);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fangge_number = getIntent().getIntExtra("fangge_number", 1);

        // 初始化
        init();
        // 查找与检索
        database = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        cursor.moveToFirst();
        Fangge fangge_item = new Fangge(cursor);
        cursor.close();
        // 展示
        fangGeID.setText(String.format("方歌: %d/%d", fangge_item.id, total_number));
        fangGeName.setText(fangge_item.info);
        fangGeSource.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
        fangGeContent.setText(fangge_item.content);
        fangGeInfo.setText(String.format("治法：%s",fangge_item.table_name));

        // 设置图标样式
        starred = fangge_item.isCollect == 1;
        if(starred) starButton.setBackgroundResource(R.drawable.collec2);
        else starButton.setBackgroundResource(R.drawable.collec);

        if(fangge_number == total_number){
            rightButton.setEnabled(false);
            rightButton.setVisibility(View.INVISIBLE);
        }
        if(fangge_number == 1){
            leftButton.setEnabled(false);
            leftButton.setVisibility(View.INVISIBLE);
        }

        backButton.setOnClickListener(v -> finish());

        leftButton.setOnClickListener(v -> {
            Intent intent = new Intent(readPage.this, readPage.class);
            intent.putExtra("fangge_number", fangge_number - 1);//参数：name、value
            startActivity(intent);
            finish();
        });

        rightButton.setOnClickListener(v -> {
            Intent intent = new Intent(readPage.this, readPage.class);
            intent.putExtra("fangge_number", fangge_number + 1); //参数：name、value
            startActivity(intent);
            finish();
        });

        searchButton.setOnClickListener(v -> {
            String text = searchBox.getText().toString();
            if(text.trim().equals("")){
                Toast.makeText(readPage.this, "请输入范围内数字", Toast.LENGTH_SHORT).show();
            } else {
                int new_number = Integer.parseInt(text);
                if(new_number <= total_number && new_number >= 1){
                    Intent intent = new Intent(readPage.this, readPage.class);
                    intent.putExtra("fangge_number", new_number);//参数：name、value
                    startActivity(intent);
                }
                else Toast.makeText(readPage.this, "请输入范围内数字", Toast.LENGTH_SHORT).show();
            }
        });

        // 收藏
        starButton.setOnClickListener(v -> {
            if(starred) {
                v.setBackgroundResource(R.drawable.collec);
                String sql = String.format("update fangge set iscollect = 0 where id = %d", fangge_number);
                database.execSQL(sql);
                Toast.makeText(readPage.this, "条文取消收藏成功", Toast.LENGTH_SHORT).show();
            } else {
                v.setBackgroundResource(R.drawable.collec2);
                String sql = String.format("update fangge set iscollect = 1 where id = %d", fangge_number);
                database.execSQL(sql);
                Toast.makeText(readPage.this, "条文收藏成功", Toast.LENGTH_SHORT).show();
            }
            starred = !starred;
        });
    }
}