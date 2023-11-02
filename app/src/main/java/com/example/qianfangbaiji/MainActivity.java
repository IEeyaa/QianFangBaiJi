package com.example.qianfangbaiji;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.LoginPage.Login;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.OtherClass.MySQLHelper;

public class MainActivity extends AppCompatActivity {
    @SuppressLint({"Recycle", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 动画设计，字体逐渐向上展现
        AnimationSet animationSet=new AnimationSet(true);
        AlphaAnimation alphaAction = new AlphaAnimation(0, 1);
        // 动画效果，Alpha从0到1的变化
        alphaAction.setDuration(2000);
        alphaAction.setFillAfter(true);
        Global.init(this);
        MySQLHelper.getInstance().createDB(this);

        setContentView(R.layout.activity_main);
        TextView txt = findViewById(R.id.text1);

        animationSet.addAnimation(alphaAction);
        txt.startAnimation(animationSet);
        // 动画相关监听，动画结束后通过Intent跳转到SelectBook;
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent;
                if(!Global.checkCount()){
                    intent = new Intent(MainActivity.this, Login.class);
                }
                else{
                    intent = new Intent(MainActivity.this, daily.class);
                }
                startActivity(intent);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}