package com.example.qianfangbaiji.LoginPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

import java.util.Random;

public class register extends AppCompatActivity {
    EditText name, password, mail, checkMail;
    Button back, register, getCheckText;
    TextView find;
    String nameWord = "", passwordWord = "", mailWord = "", checkWord = "";
    // 用于存储倒计时的计时器
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        mail = findViewById(R.id.mail);
        checkMail = findViewById(R.id.mailCheck);

        back = findViewById(R.id.back);
        register = findViewById(R.id.register);
        getCheckText = findViewById(R.id.mailGet);

        find = findViewById(R.id.find);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameWord = name.getText().toString();
                passwordWord = password.getText().toString();
                mailWord = mail.getText().toString();
                checkWord = checkMail.getText().toString();

                if(Global.createCount(nameWord, passwordWord, mailWord, checkWord)){
                    Intent intent=new Intent(com.example.qianfangbaiji.LoginPage.register.this, login.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(com.example.qianfangbaiji.LoginPage.register.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        回退页面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.qianfangbaiji.LoginPage.register.this, login.class);
                startActivity(intent);
            }
        });
        // 点击事件处理
        getCheckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = mail.getText().toString();

                // 邮箱格式验证
                if (isValidEmail(emailAddress)) {
                    // 生成随机的6位数
                    Random random = new Random();
                    int randomCode = random.nextInt(900000) + 100000; // 生成6位随机数

                    Global.storeMailInfo(emailAddress, String.valueOf(randomCode));
                    // 创建发送邮件的Intent
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("message/rfc822");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
                    email.putExtra(Intent.EXTRA_SUBJECT, "千方百剂验证");
                    email.putExtra(Intent.EXTRA_TEXT, "你的验证码是：" + randomCode);
                    startActivity(Intent.createChooser(email, "验证"));

                    // 禁用按钮
                    getCheckText.setEnabled(false);
                    getCheckText.setAlpha(0.5f);
                    // 开始倒计时
                    countDownTimer = new CountDownTimer(60000, 1000) {
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {
                            getCheckText.setText(millisUntilFinished / 1000 + "秒");
                        }
                        public void onFinish() {
                            getCheckText.setEnabled(true);
                            getCheckText.setText("获取");
                            getCheckText.setAlpha(1.0f);
                        }
                    }.start();
                } else {
                    // 邮箱格式不合法，进行相应处理
                    // 可以显示错误信息或者其他操作
                    Toast.makeText(getApplicationContext(), "邮箱格式不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // 邮箱格式验证方法
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(\\.[a-z]+)+";
        return email.matches(emailPattern);
    }

}