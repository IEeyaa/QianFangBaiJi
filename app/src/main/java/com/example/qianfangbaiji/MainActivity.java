package com.example.qianfangbaiji;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.OtherClass.MyDBOpenHelper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView txt;
    @SuppressLint({"Recycle", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关联到首页Activity_main，并获取其中的《穷经》text1
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.text1);
        //动画设计，字体逐渐向上展现
        AnimationSet animationSet=new AnimationSet(true);
        AlphaAnimation alphaAction = new AlphaAnimation(0, 1);
        //动画效果，Alpha从0到1的变化
        alphaAction.setDuration(2000);
        alphaAction.setFillAfter(true);

        MyDBOpenHelper db;


//        打开数据库，如果没有则复制
        db = new MyDBOpenHelper(this);
        try {
            db.openDB();
        } catch (SQLException sqle) {
            try {
                db.createDB();
            } catch (IOException ioe) {
                throw new Error("Database not created....");
            }
        }
        db.close();

        animationSet.addAnimation(alphaAction);
        txt.startAnimation(animationSet);
        // 动画相关监听，动画结束后通过Intent跳转到SelectBook;
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(MainActivity.this, daily.class);
                startActivity(intent);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}