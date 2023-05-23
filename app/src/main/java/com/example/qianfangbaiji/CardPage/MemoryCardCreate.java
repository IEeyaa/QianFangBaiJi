package com.example.qianfangbaiji.CardPage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.R;

//根据数据库创建
public class MemoryCardCreate extends AppCompatActivity {
    Button buttonBack;
    TextView cardReady;
    EditText[]hint = new EditText[4];
    EditText cardTitle;
    int cardNumber, cardMode;
    private Cursor c;
    SQLiteDatabase db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardedit);
        cardNumber = getIntent().getIntExtra("cardnum", 1);
        cardMode = getIntent().getIntExtra("cardMode", 1);

        final int number;
        cardReady = findViewById(R.id.cardReady);
        buttonBack = findViewById(R.id.back);
        cardTitle = findViewById(R.id.title);
        hint[0] = findViewById(R.id.hint1);
        hint[1] = findViewById(R.id.hint2);
        hint[2] = findViewById(R.id.hint3);
        hint[3] = findViewById(R.id.hint4);
        db1 = openOrCreateDatabase("database", Context.MODE_PRIVATE, null);
        c = db1.rawQuery("SELECT * FROM card", null);
        c.moveToLast();
        // 最后一个条文的number，也一定是里面最大的
        if(c.getCount() == 0) number = 0;
        else number = c.getInt(c.getColumnIndex("number"));
        if(cardMode != 1){
            c.moveToFirst();
            while(!c.isAfterLast()){
                if(c.getInt(c.getColumnIndex("number")) == cardNumber){
                    cardTitle.setHint(c.getString(c.getColumnIndex("title")));
                    hint[0].setHint(c.getString(c.getColumnIndex("hint1")));
                    hint[1].setHint(c.getString(c.getColumnIndex("hint2")));
                    hint[2].setHint(c.getString(c.getColumnIndex("hint3")));
                    hint[3].setHint(c.getString(c.getColumnIndex("hint4")));
                    break;
                }
                c.moveToNext();
            }
        }
        c.close();
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardCreate.this, MemoryCardList.class);
                intent.putExtra("cardnum", cardNumber);
                startActivity(intent);
            }
        });
        cardReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = hint[0].getText().toString();
                String text2 = hint[1].getText().toString();
                String text3 = hint[2].getText().toString();
                String text4 = hint[3].getText().toString();
                String text0 = cardTitle.getText().toString();
                //新建一张卡
                if(cardMode == 1){
                    CreateCard newc = new CreateCard(number+1, 0, text0, text1, text2, text3, text4, db1);
                    newc.execute();
                    c.close();
                    Intent intent = new Intent(MemoryCardCreate.this, MemoryCardList.class);
                    startActivity(intent);
                }
                //修改一张卡
                else{
                    CreateCard newc = new CreateCard(cardNumber, 2, text0, text1, text2, text3, text4, db1);
                    newc.execute();
                    c.close();
                    Intent intent = new Intent(MemoryCardCreate.this, MemoryCardLoad.class);
                    intent.putExtra("cardnum", cardNumber);
                    intent.putExtra("hintnum", 0);
                    startActivity(intent);
                }
            }
        });
    }
}