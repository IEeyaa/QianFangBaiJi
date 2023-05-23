package com.example.qianfangbaiji.MemoryPage;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.StudyPage.MyAdapterForList;
import com.example.qianfangbaiji.StudyPage.readPage;
import com.example.qianfangbaiji.StudyPage.selectmode;

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
public class memoryReport extends AppCompatActivity {
    Button btn_back;
    int max;
    TextView fangge_number;
    List<Fangge> list = new ArrayList<>();
    SQLiteDatabase db1;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoryreport);

        SharedPreferences prefs = getSharedPreferences("memory_prefs", MODE_PRIVATE);
        max = prefs.getInt("number", -1);
        int[] array = new int[max];
        int[] q_array = new int[max];
        for(int i=0; i<max; i++){
            array[i] = prefs.getInt("array"+i, -1);
            q_array[i] = prefs.getInt("array"+i, -1);
        }

        btn_back = findViewById(R.id.btn_back);

        fangge_number = findViewById(R.id.fangge_number);
        fangge_number.setText(""+max);

        // for get back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memoryReport.this, selectmode.class);
                startActivity(intent);
            }
        });

        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        Cursor c = db1.rawQuery("SELECT * FROM fangge ", null);
        c.moveToFirst();

        int i=0;
        int ispassed,nowdate,maxdate,times,q, t;
        double EF;
        String sql;
        while (!c.isAfterLast()) {
            // save into q_array
            //这一部分的条文，如果通过了ispassed设为2，反之设为1，同时更新EF，maxdate值，nowdate值恢复为0
            if (i < max && c.getInt(c.getColumnIndex("id")) == array[i]) {
                q = q_array[i];
                maxdate = c.getInt(c.getColumnIndex("maxdate"));
                times = c.getInt(c.getColumnIndex("times"));
                EF = c.getInt(c.getColumnIndex("EF"));
                if(q>=4) ispassed = 2;
                else{
                    ispassed = 1;
                    times++;
                    if(times == 1) maxdate = 1;
                    else if (times == 2) maxdate = 6;
                    else{
                        maxdate = Double.valueOf(maxdate * EF).intValue();
                        EF = EF+(0.1-(5-q)*(0.08+(5-q)*0.02));
                    }
                }
                list.add(new Fangge(c, q_array[i], maxdate));
                sql = "update fangge set nowdate = 0, ispassed = " + ispassed + ", maxdate = " + maxdate+ ", times = " + times + ", EF = " + EF + " where id = " + array[i];
                db1.execSQL(sql);
                i++;
            }
            //对于其它条文，如果已经遇到了并且这次没有学而且没有通过（即1类条文），加一天时间
            else if(c.getInt(c.getColumnIndex("ispassed")) == 1){
                t = c.getInt(c.getColumnIndex("id"));
                nowdate = c.getInt(c.getColumnIndex("nowdate"));
                ispassed = c.getInt(c.getColumnIndex("ispassed"));
                maxdate = c.getInt(c.getColumnIndex("maxdate"));
                nowdate++;
                if(nowdate == maxdate) ispassed = 3;
                sql = "update fangge set nowdate = "+ nowdate + ", ispassed = " + ispassed + " where id = " + t;
                db1.execSQL(sql);
            }
            c.moveToNext();
        }
        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForMemoryList myAdapter = new MyAdapterForMemoryList(list, this, db1, "memory");
        listView.setAdapter(myAdapter);
    }
}

//MyAdapter模块，调用相应按钮对应的文本并将其删除
class MyAdapterForMemoryList extends MyAdapterForList {
    TextView q, day;

    MyAdapterForMemoryList(List<Fangge> list, AppCompatActivity myList, SQLiteDatabase db1, String from) {
        super(list, myList, db1, from);
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
        fangge_infor.setText(fangge_item.infor);
        fangge_content.setText(fangge_item.content.substring(0, 7) + "...");

        q.setText("分值：" + fangge_item.q);
        if(fangge_item.day == 0) day.setText("通过!");
        else day.setText("下次间隔时间：" + fangge_item.day);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent().getParent();
                setAnimation(parentView, parent.getContext(), position);
            }
        });

//        设置样式
        has_collect = (fangge_item.isCollect == 1);
        if(has_collect)btn_collect.setBackgroundResource(R.drawable.collec2);
        else btn_collect.setBackgroundResource(R.drawable.collec);
        btn_collect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                String sql;
                if(has_collect) {
                    v.setBackgroundResource(R.drawable.collec);
                    sql = String.format("update fangge set iscollect = 0 where id = %d", fangge_number);
                    db1.execSQL(sql);
                    Toast.makeText(myList, "方歌取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    v.setBackgroundResource(R.drawable.collec2);
                    sql = String.format("update fangge set iscollect = 1 where id = %d", fangge_number);
                    db1.execSQL(sql);
                    Toast.makeText(myList, "方歌收藏成功", Toast.LENGTH_SHORT).show();
                }
                has_collect = !has_collect;
            }
        });

        fangge_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myList, readPage.class);
                intent.putExtra("fangge_number", fangge_number);
                intent.putExtra("from", from);
                myList.startActivity(intent);
            }
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
                db1.execSQL(sql);
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

