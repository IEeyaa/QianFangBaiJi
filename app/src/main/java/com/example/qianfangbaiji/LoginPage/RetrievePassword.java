package com.example.qianfangbaiji.LoginPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.MyEmailUtil.SysEmail;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class RetrievePassword extends AppCompatActivity {
    EditText password_input, mail_input, verification_code_input;
    Button back, confirm, send_verification_code;
    // 用于存储倒计时的计时器
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieve_password);

        // EditText
        mail_input = findViewById(R.id.mail);
        verification_code_input = findViewById(R.id.verification_code);
        password_input = findViewById(R.id.password);

        // Buttons
        send_verification_code = findViewById(R.id.send_verification_code);
        confirm = findViewById(R.id.confirm);
        back = findViewById(R.id.back);

        // 点击事件处理
        send_verification_code.setOnClickListener(v -> {
            String emailAddress = mail_input.getText().toString();

            // 邮箱格式验证
            if (SysEmail.isValidEmail(emailAddress)) {
                SysEmail.sendEmail(RetrievePassword.this, emailAddress);

                // 禁用按钮
                send_verification_code.setEnabled(false);
                send_verification_code.setAlpha(0.5f);
                // 开始倒计时
                countDownTimer = new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        send_verification_code.setText(millisUntilFinished / 1000 + "秒");
                    }
                    public void onFinish() {
                        send_verification_code.setEnabled(true);
                        send_verification_code.setText("获取");
                        send_verification_code.setAlpha(1.0f);
                    }
                }.start();
            } else {
                // 邮箱格式不合法，进行相应处理
                // 可以显示错误信息或者其他操作
                Toast.makeText(getApplicationContext(), "邮箱格式不合法", Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(v -> {
            String user_password = password_input.getText().toString();
            String user_email = mail_input.getText().toString();
            String user_verification_code = verification_code_input.getText().toString();

            if(Global.reFindCount(user_password, user_email, user_verification_code)){
                Intent intent=new Intent(RetrievePassword.this, Login.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(RetrievePassword.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        });

        //回退页面
        back.setOnClickListener(v -> {
            finish();
        });
    }

}