package com.example.qianfangbaiji.TestPage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.MyAdapterForList;
import com.example.qianfangbaiji.StudyPage.readPage;
import com.example.qianfangbaiji.StudyPage.selectmode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//根据数据库创建
public class testReport extends AppCompatActivity {
    Button btn_back;
    TextView fangge_number;
    int max;
    List<Fangge> list = new ArrayList<>();
    SQLiteDatabase db1;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoryreport);

        SharedPreferences prefs = getSharedPreferences("test_prefs", MODE_PRIVATE);
        max = prefs.getInt("number", -1);
        int[] array = new int[max];
        for(int i=0; i<max; i++){
            array[i] = prefs.getInt("array"+i, -1);
        }

        btn_back = findViewById(R.id.button_back);

        fangge_number = findViewById(R.id.fangge_number);
        fangge_number.setText(""+array.length);

        // for get back
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(testReport.this, selectmode.class);
            startActivity(intent);
        });

        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);

        String[] aStrings = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            aStrings[i] = String.valueOf(array[i]);
        }
        String whereClause = "id IN (" + TextUtils.join(",", Collections.nCopies(aStrings.length, "?")) + ")";
        Cursor c = db1.rawQuery("SELECT * FROM fangge WHERE " + whereClause, aStrings);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list.add(new Fangge(c));
            c.moveToNext();
        }
        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForMemoryList myAdapter = new MyAdapterForMemoryList(list, this, "memory");
        listView.setAdapter(myAdapter);
    }
}

//MyAdapter模块，调用相应按钮对应的文本并将其删除
class MyAdapterForMemoryList extends MyAdapterForList {
    TextView q, day;

    MyAdapterForMemoryList(List<Fangge> list, AppCompatActivity myList, String from) {
        super(list, myList, from);
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.memoryitem, null);
        super.init(view);
        this.q = view.findViewById(R.id.q);
        this.day = view.findViewById(R.id.day);

        Fangge fangge_item = list.get(position);
        final int fangge_number;
        fangge_number = fangge_item.id;
        fangge_id.setText(""+fangge_number);
        fangge_infor.setText(fangge_item.info);
        fangge_content.setText(fangge_item.content.substring(0, 7) + "...");

        q.setText("");
        day.setText("");

        btn_delete.setOnClickListener(v -> {
            View parentView = (View) v.getParent().getParent();
            setAnimation(parentView, parent.getContext(), position);
        });

//        设置样式
        has_collect = (fangge_item.isCollect == 1);
        if(has_collect)btn_collect.setBackgroundResource(R.drawable.collec2);
        else btn_collect.setBackgroundResource(R.drawable.collec);
        btn_collect.setOnClickListener(v -> {
            String sql;
            if(has_collect) {
                v.setBackgroundResource(R.drawable.collec);
                sql = String.format("update fangge set iscollect = 0 where id = %d", fangge_number);
                MySQLHelper.getInstance().sqlOther(sql);
                Toast.makeText(myList, "方歌取消收藏成功", Toast.LENGTH_SHORT).show();
            }
            else{
                v.setBackgroundResource(R.drawable.collec2);
                sql = String.format("update fangge set iscollect = 1 where id = %d", fangge_number);
                MySQLHelper.getInstance().sqlOther(sql);
                Toast.makeText(myList, "方歌收藏成功", Toast.LENGTH_SHORT).show();
            }
            has_collect = !has_collect;
        });

        fangge_layout.setOnClickListener(v -> {
            Intent intent = new Intent(myList, readPage.class);
            intent.putExtra("fangge_number", fangge_number);
            intent.putExtra("from", "test");
            myList.startActivity(intent);
        });

        return view;
    }
    public void setAnimation(View view, final Context context, final int position) {
        ObjectAnimator oaY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        ObjectAnimator oaX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.playTogether(oaY, oaX);
        animSet.start();
        //设置动画执行的监听事件
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //这里是关键删除句
                int fangge_number = list.get(position).id;
                @SuppressLint("DefaultLocale")
                String sql = String.format("update fangge set iscut = 1 where id = %d", fangge_number);
                MySQLHelper.getInstance().sqlOther(sql);
                Toast.makeText(context, "方歌删除成功", Toast.LENGTH_SHORT).show();
                list.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}

