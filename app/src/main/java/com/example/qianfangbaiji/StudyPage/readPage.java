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

import com.example.qianfangbaiji.MemoryPage.memoryReport;
import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.TestPage.testReport;

//根据数据库创建
@SuppressLint("Registered")
public class readPage extends AppCompatActivity {
    Button btn_back, btn_search, btn_collect, btn_left, btn_right;
    EditText Edit;
    int total_number = Global.total_number;
    int fangge_number;
    String from;
    SQLiteDatabase db1;
    boolean has_collect = true;

    TextView fangge_id, fangge_name, fangge_infor, fangge_from, fangge_content;


    //初始化
    private void init(){
        setContentView(R.layout.readpage);
        btn_back = findViewById(R.id.btn_back);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_collect = findViewById(R.id.btn_collect);
        btn_search = findViewById(R.id.btn_search);

        Edit = findViewById(R.id.Edit);

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
        fangge_number = getIntent().getIntExtra("fangge_number", 1);
        from = getIntent().getStringExtra("from");

        // 初始化
        init();
        // 查找与检索
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        Cursor cursor = db1.rawQuery(String.format("SELECT * FROM %s WHERE id = %d", "fangge", fangge_number), null);
        cursor.moveToFirst();
        Fangge fangge_item = new Fangge(cursor);
        cursor.close();
        //        展示
        fangge_id.setText(String.format("方歌: %d/%d", fangge_item.id, total_number));
        fangge_name.setText(fangge_item.info);
        fangge_from.setText(String.format("%s·%s", fangge_item.dynasty, fangge_item.book));
        fangge_content.setText(fangge_item.content);
        fangge_infor.setText(String.format("治法：%s",fangge_item.table_name));

        //        设置图标样式
        has_collect = fangge_item.isCollect == 1;
        if(has_collect)btn_collect.setBackgroundResource(R.drawable.collec2);
        else btn_collect.setBackgroundResource(R.drawable.collec);

        if(fangge_number == total_number){
            btn_right.setEnabled(false);
            btn_right.setVisibility(View.INVISIBLE);
        }
        if(fangge_number == 1){
            btn_left.setEnabled(false);
            btn_left.setVisibility(View.INVISIBLE);
        }

        btn_back.setOnClickListener(v -> {
            Intent intent;
            switch (from) {
                case "delete":
                    intent = new Intent(readPage.this, deleteList.class);
                    break;
                case "collect":
                    intent = new Intent(readPage.this, collectList.class);
                    break;
                case "memory":
                    intent = new Intent(readPage.this, memoryReport.class);
                    break;
                case "test":
                    intent = new Intent(readPage.this, testReport.class);
                    break;
                default:
                    intent = new Intent(readPage.this, readList.class);
                    break;
            }
            startActivity(intent);
        });

        btn_left.setOnClickListener(v -> {
            Intent intent = new Intent(readPage.this, readPage.class);
            intent.putExtra("fangge_number", fangge_number - 1);//参数：name、value
            intent.putExtra("from", from);
            startActivity(intent);
        });
        btn_right.setOnClickListener(v -> {
            Intent intent = new Intent(readPage.this, readPage.class);
            intent.putExtra("fangge_number", fangge_number + 1);//参数：name、value
            intent.putExtra("from", from);
            startActivity(intent);
        });
        btn_search.setOnClickListener(v -> {
            int new_number;
            String text = Edit.getText().toString();
            if(text.trim().equals("")){
                Toast.makeText(readPage.this, "请输入范围内数字", Toast.LENGTH_SHORT).show();
            }
            else{
                new_number = Integer.parseInt(text);
                if(new_number <= total_number && new_number >= 1){
                    Intent intent = new Intent(readPage.this, readPage.class);
                    intent.putExtra("from", from);
                    intent.putExtra("fangge_number", new_number);//参数：name、value
                    startActivity(intent);
                }
                else Toast.makeText(readPage.this, "请输入范围内数字", Toast.LENGTH_SHORT).show();
            }
        });

        //  收藏
        btn_collect.setOnClickListener(v -> {
            String sql;
            if(has_collect) {
                v.setBackgroundResource(R.drawable.collec);
                sql = String.format("update fangge set iscollect = 0 where id = %d", fangge_number);
                db1.execSQL(sql);
                Toast.makeText(readPage.this, "条文取消收藏成功", Toast.LENGTH_SHORT).show();
            }
            else{
                v.setBackgroundResource(R.drawable.collec2);
                sql = String.format("update fangge set iscollect = 1 where id = %d", fangge_number);
                db1.execSQL(sql);
                Toast.makeText(readPage.this, "条文收藏成功", Toast.LENGTH_SHORT).show();
            }
            has_collect = !has_collect;
        });
    }
}