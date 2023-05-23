package com.example.qianfangbaiji.StudyPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.MemoryPage.memoryStart;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.TestPage.testStart;

@SuppressLint("Registered")
public class selectmode extends AppCompatActivity {
    LinearLayout ButtonToRead, ButtonToMemory, ButtonToCollect, ButtonToDelete, ButtonToTest;
    Button Btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmode);

        ButtonToRead = findViewById(R.id.buttonToRead);
        ButtonToMemory = findViewById(R.id.buttonToMemory);
        ButtonToCollect = findViewById(R.id.buttonToCollect);
        ButtonToDelete = findViewById(R.id.buttonToDelete);
        ButtonToTest = findViewById(R.id.buttonToTest);
        Btnback = findViewById(R.id.buttonBack);

        ButtonToRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this,readList.class);
                startActivity(intent);
            }
        });

        ButtonToMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this, testStart.class);
                startActivity(intent);
            }
        });

        ButtonToCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this,collectList.class);
                startActivity(intent);
            }
        });
        ButtonToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this,deleteList.class);
                startActivity(intent);
            }
        });

        ButtonToTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this, memoryStart.class);
                startActivity(intent);
            }
        });

        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(selectmode.this, daily.class);
                startActivity(intent);
            }
        });
    }

}