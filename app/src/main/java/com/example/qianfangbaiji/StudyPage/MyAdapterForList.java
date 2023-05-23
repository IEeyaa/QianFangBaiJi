package com.example.qianfangbaiji.StudyPage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Fangge;
import com.example.qianfangbaiji.R;

import java.util.List;

//MyAdapter模块，调用相应按钮对应的文本并将其删除
public class MyAdapterForList extends BaseAdapter {
    public List<Fangge> list;
    public TextView fangge_id;
    public TextView fangge_infor;
    public TextView fangge_content;
    public Button btn_collect, btn_delete;
    public LinearLayout fangge_layout;
    public AppCompatActivity myList;
    public SQLiteDatabase db1;
    public String from;
    public boolean has_collect = false;

    public MyAdapterForList(List<Fangge> list, AppCompatActivity myList, SQLiteDatabase db1, String from) {
        this.list = list;
        this.myList = myList;
        this.db1 = db1;
        this.from = from;
    }


    public void init(View view){
        fangge_id = view.findViewById(R.id.fangge_id);
        fangge_infor =  view.findViewById(R.id.fangge_infor);
        fangge_content = view.findViewById(R.id.fangge_content);
        fangge_layout = view.findViewById(R.id.fangge_item);
        btn_collect = view.findViewById(R.id.btn_collect);
        btn_delete = view.findViewById(R.id.btn_delete);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.listitem, null);
        init(view);
        Fangge fangge_item = list.get(position);
        final int fangge_number;
        fangge_number = fangge_item.id;
        fangge_id.setText(""+fangge_number);
        fangge_infor.setText(fangge_item.infor);
        fangge_content.setText(fangge_item.content.substring(0, 7) + "...");

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
    private void setAnimation(View view, final Context context, final int position) {
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
