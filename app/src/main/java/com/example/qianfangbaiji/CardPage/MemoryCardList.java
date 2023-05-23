package com.example.qianfangbaiji.CardPage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.OtherClass.Card;
import com.example.qianfangbaiji.R;

import java.util.ArrayList;
import java.util.List;

//根据数据库创建
public class MemoryCardList extends AppCompatActivity {
    Button buttonBack,searchCard;
    TextView cardTitle, createCard;
    EditText cardName;
    ListView cardList;
    List<Card>list = new ArrayList<>();
    int position;
    private Cursor c;
    SQLiteDatabase db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardlist);
        buttonBack = findViewById(R.id.back);
        cardTitle = findViewById(R.id.title);
        cardName = findViewById(R.id.cardName);
        createCard = findViewById(R.id.cardCreate);
        searchCard = findViewById(R.id.search);
        cardList = this.findViewById(R.id.card_info);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardList.this, daily.class);
                intent.putExtra("cardnum", position);
                startActivity(intent);
            }
        });
        createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardList.this, MemoryCardCreate.class);
                intent.putExtra("cardMode", 1);
                startActivity(intent);
            }
        });
        cardLoad(1, "");
    }
    public void search(View view){
        switch(view.getId()){
            case R.id.search:
                cardLoad(2, cardName.getText().toString());
                break;
        }
    }

    public void cardLoad(int mode, String name){
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery("SELECT * FROM card", null);
        list.clear();
        c.moveToFirst();
        if(mode == 1){
            while(!c.isAfterLast()){
                list.add(new Card(c.getInt(c.getColumnIndex("number")), c.getString(c.getColumnIndex("title"))));
                c.moveToNext();
            }
        }
        else{
            while(!c.isAfterLast()){
                if(c.getString(c.getColumnIndex("title")).contains(name)){
                    list.add(new Card(c.getInt(c.getColumnIndex("number")), c.getString(c.getColumnIndex("title"))));
                }
                c.moveToNext();
            }
        }
        c.close();
        MyAdapterForCard myAdapter = new MyAdapterForCard(list, this, db1);
        cardList.setAdapter(myAdapter);
    }
}
//MyAdapter模块，调用相应按钮对应的文本并将其删除

class MyAdapterForCard extends BaseAdapter {
    private List<Card> list;
    private MemoryCardList pre;
    private SQLiteDatabase db1;

    public MyAdapterForCard(List<Card> list, MemoryCardList myCardList, SQLiteDatabase db1) {
        this.list = list;
        this.pre = myCardList;
        this.db1 = db1;
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

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = View.inflate(parent.getContext(), R.layout.item_card, null);
        TextView title = view.findViewById(R.id.title);
        Button button2 = view.findViewById(R.id.delete);
        LinearLayout desc = view.findViewById(R.id.layout);
        title.setText(list.get(position).title);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View) v.getParent().getParent();
                setAnimation(parentView, parent.getContext(), position);
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pre, MemoryCardLoad.class);
                int number = list.get(position).cardNumber;
                intent.putExtra("cardnum", number);//参数：name、value
                pre.startActivity(intent);
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
                int number = list.get(position).cardNumber;
                CreateCard newc = new CreateCard(number, db1);
                newc.execute();
                Toast.makeText(context, "卡片删除成功", Toast.LENGTH_SHORT).show();
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
