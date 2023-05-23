package com.example.qianfangbaiji.CardPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.R;

//根据数据库创建
public class MemoryCardLoad extends AppCompatActivity{
    Button buttonBack,buttonToLeft, buttonToRight, editCard;
    TextView cardTitle, NextHint;
    TextView []hint = new TextView[4];
    View []hintLines = new View[4];
    int position, hintNumber, prePosition, nextPosition;
    private Cursor c;
    SQLiteDatabase db1;

    private void init() {
        buttonBack = findViewById(R.id.back);
        NextHint = findViewById(R.id.toNext);
        buttonToLeft = findViewById(R.id.toLeft);
        buttonToRight = findViewById(R.id.toRight);
        editCard = findViewById(R.id.editCard);
        cardTitle = findViewById(R.id.title);
        hint[0] = findViewById(R.id.hint1);
        hint[1] = findViewById(R.id.hint2);
        hint[2] = findViewById(R.id.hint3);
        hint[3] = findViewById(R.id.hint4);
        hintLines[0] = findViewById(R.id.hintLine1);
        hintLines[1] = findViewById(R.id.hintLine2);
        hintLines[2] = findViewById(R.id.hintLine3);
        hintLines[3] = findViewById(R.id.hintLine4);
    }

    @SuppressLint("SetTextI18n")
    private void reachAndDisplay(int cardNum, int hintNum){
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery("SELECT * FROM card", null);
        c.moveToFirst();
        prePosition = 0;
        nextPosition = 0;
        while(!c.isAfterLast()){
            if(c.getInt(c.getColumnIndex("number")) == cardNum){
                cardTitle.setText(c.getString(c.getColumnIndex("title")));
                //可见的hint以及分割线
                for(int i=0;i<hintNum;i++){
                    hint[i].setText("内容" + (i + 1) + ": " + c.getString(c.getColumnIndex("hint" + (i+1))));
                    hintLines[i].setVisibility(View.VISIBLE);
                }
                //剩余的不可见
                for(int i=hintNum;i<4;i++){
                    hint[i].setText("");
                    hintLines[i].setVisibility(View.INVISIBLE);
                }
                // 获取前后情况
                c.moveToPrevious();
                if(!c.isBeforeFirst()) prePosition = c.getInt(c.getColumnIndex("number"));
                c.moveToNext();
                c.moveToNext();
                if(!c.isAfterLast()) nextPosition = c.getInt(c.getColumnIndex("number"));
                break;
            }
            c.moveToNext();
        }
        c.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        position = getIntent().getIntExtra("cardnum", 1);
        hintNumber = getIntent().getIntExtra("hintnum", 0);
        final int number;
        init();
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery("SELECT * FROM card", null);
        number = c.getCount();

        if(position == number){
            buttonToRight.setEnabled(false);
            buttonToRight.setVisibility(View.INVISIBLE);
        }
        if(position == 1){
            buttonToLeft.setEnabled(false);
            buttonToLeft.setVisibility(View.INVISIBLE);
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardLoad.this, MemoryCardList.class);
                intent.putExtra("cardnum", position);
                startActivity(intent);
            }
        });
        buttonToLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardLoad.this, MemoryCardLoad.class);
                intent.putExtra("cardnum", prePosition);//参数：name、value
                startActivity(intent);
            }
        });
        buttonToRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardLoad.this, MemoryCardLoad.class);
                intent.putExtra("cardnum", nextPosition);//参数：name、value
                startActivity(intent);
            }
        });
       editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardLoad.this, MemoryCardCreate.class);
                intent.putExtra("cardnum", position);//参数：name、value
                intent.putExtra("cardMode", 2);//参数：name、value
                startActivity(intent);
            }
        });
        reachAndDisplay(position, 0);
    }

    public void hintToNext(View view){
        switch(view.getId()){
            case R.id.toNext:
                hintNumber =(hintNumber + 1)%5;
                reachAndDisplay(position, hintNumber);
                break;
        }
    }
}