package com.example.qianfangbaiji.StudyPage;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;
import com.example.qianfangbaiji.R;

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
@SuppressLint("Registered")
public class collectList extends AppCompatActivity {
    //创建一个List对象来存储数据
    List<Fangge> list = new ArrayList<>();
    Button btn_back, btn_search;
    EditText Edit;
    Cursor c;

    private void initView() {
        ListView listView = findViewById(R.id.fangge_list);
        MyAdapterForCollectList myAdapter = new MyAdapterForCollectList(list, this, "collect");
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collectlist);
        btn_search = findViewById(R.id.search_button);
        Edit = findViewById(R.id.search_box);
        btn_back = findViewById(R.id.button_back);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(collectList.this, selectmode.class);
            startActivity(intent);
        });

        btn_search.setOnClickListener(v -> {
            String str = Edit.getText().toString();
            c = MySQLHelper.getInstance().sqlSelect(String.format("SELECT * FROM fangge WHERE (infor LIKE '%%%s%%' " +
                            "or content LIKE '%%%s%%') and iscollect = 1", str, str));
            c.moveToFirst();
            list.clear();
            while (!c.isAfterLast()) {
                list.add(new Fangge(c));
                c.moveToNext();
            }
            c.close();
            initView();
        });

        c = MySQLHelper.getInstance().sqlSelect("SELECT * FROM fangge WHERE iscollect = 1");
        c.moveToFirst();

        while (!c.isAfterLast()) {
            list.add(new Fangge(c));
            c.moveToNext();
        }
        c.close();
        initView();
    }
}

class MyAdapterForCollectList extends MyAdapterForList {
    MyAdapterForCollectList(List<Fangge> list, AppCompatActivity myList, String from) {
        super(list, myList, from);
    }
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.listitem, null);
        super.init(view);
        Fangge fangge_item = list.get(position);
        final int fangge_number;
        fangge_number = fangge_item.id;
        fangge_id.setText(""+fangge_number);
        fangge_infor.setText(fangge_item.info);
        fangge_content.setText(fangge_item.content.substring(0, 7) + "...");
        btn_delete.setOnClickListener(v -> {
            View parentView = (View) v.getParent().getParent();
            setAnimation(parentView, parent.getContext(), position);
        });
        btn_collect.setVisibility(View.INVISIBLE);
        fangge_layout.setOnClickListener(v -> {
            Intent intent = new Intent(myList, readPage.class);
            intent.putExtra("fangge_number", fangge_number);
            intent.putExtra("from", "delete");
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
                String sql = String.format("update fangge set iscollect = 0 where id = %d", fangge_number);
                MySQLHelper.getInstance().sqlOther(sql);
                Toast.makeText(context, "方歌取消收藏成功", Toast.LENGTH_SHORT).show();
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
