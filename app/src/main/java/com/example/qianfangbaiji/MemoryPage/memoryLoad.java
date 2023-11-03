package com.example.qianfangbaiji.MemoryPage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
public class memoryLoad extends AppCompatActivity {
    Button btn_back;
    List<Fangge> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoryload);


        btn_back = findViewById(R.id.button_back);

        // for get back
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(memoryLoad.this, memoryStart.class);
            startActivity(intent);
        });

        Cursor c = MySQLHelper.getInstance().sqlSelect("SELECT * FROM fangge WHERE ispassed = 2");
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list.add(new Fangge(c));
            c.moveToNext();
        }
        initView();
    }

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForLoadList myAdapter = new MyAdapterForLoadList(list, this, "load");
        listView.setAdapter(myAdapter);
    }
}

//MyAdapter模块，调用相应按钮对应的文本并将其删除
class MyAdapterForLoadList extends MyAdapterForList {
    TextView q, day;

    MyAdapterForLoadList(List<Fangge> list, AppCompatActivity myList, String from) {
        super(list, myList, from);
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.memoryitem, null);
        super.init(view);

        Fangge fangge_item = list.get(position);
        final int fangge_number;
        fangge_number = fangge_item.id;
        fangge_id.setText(""+fangge_number);
        fangge_infor.setText(fangge_item.info);
        fangge_content.setText(fangge_item.content.substring(0, 7) + "...");
        this.q = view.findViewById(R.id.q);
        this.day = view.findViewById(R.id.day);
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
                String sql = String.format("update fangge set ispassed = 0, nowdate = 0, maxdate = 0, times = 0, EF = 2.5 " +
                        "where id = %d", fangge_number);
                db1.execSQL(sql);
                Toast.makeText(context, "方歌设置为：重新背诵", Toast.LENGTH_SHORT).show();
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
