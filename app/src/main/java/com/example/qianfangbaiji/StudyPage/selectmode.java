package com.example.qianfangbaiji.StudyPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.MemoryPage.memoryStart;
import com.example.qianfangbaiji.R;
import com.example.qianfangbaiji.TestPage.testStart;

@SuppressLint("Registered")
public class selectmode extends AppCompatActivity {
    LinearLayout readModeButton, randomTestButton, favoriteButton, deleteButton, dailyTestButton;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmode);

        back = findViewById(R.id.back_button);

        readModeButton = findViewById(R.id.read_mode_button);
        randomTestButton = findViewById(R.id.random_test_button);
        favoriteButton = findViewById(R.id.favorite_button);
        deleteButton = findViewById(R.id.delete_button);
        dailyTestButton = findViewById(R.id.daily_test_button);

        back.setOnClickListener(v -> finish());

        readModeButton.setOnClickListener(v -> {
            Intent intent=new Intent(selectmode.this,readList.class);
            startActivity(intent);
        });

        randomTestButton.setOnClickListener(v -> {
            Intent intent=new Intent(selectmode.this, testStart.class);
            startActivity(intent);
        });

        favoriteButton.setOnClickListener(v -> {
            Intent intent=new Intent(selectmode.this,collectList.class);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            Intent intent=new Intent(selectmode.this,deleteList.class);
            startActivity(intent);
        });

        dailyTestButton.setOnClickListener(v -> {
            Intent intent=new Intent(selectmode.this, memoryStart.class);
            startActivity(intent);
        });
    }

}