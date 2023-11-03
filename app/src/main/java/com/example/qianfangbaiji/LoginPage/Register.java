package com.example.qianfangbaiji.LoginPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.MyEmailUtil.SysEmail;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    EditText nameInput, passwordInput, emailInput, verificationCodeInput;
    Button send_verification_code_button, register_button, back_button;
    // 用于存储倒计时的计时器
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // EditText
        nameInput = findViewById(R.id.name);
        passwordInput = findViewById(R.id.password);
        emailInput = findViewById(R.id.mail);
        verificationCodeInput = findViewById(R.id.verification_code);

        // Buttons
        send_verification_code_button = findViewById(R.id.send_verification_code);
        register_button = findViewById(R.id.confirm);
        back_button = findViewById(R.id.back);

        // 点击事件处理
        send_verification_code_button.setOnClickListener(v -> {
            String emailAddress = emailInput.getText().toString();

            // 邮箱格式验证
            if (SysEmail.isValidEmail(emailAddress)) {
                SysEmail.sendVerifyEmail(Register.this, emailAddress);

                // 禁用按钮
                send_verification_code_button.setEnabled(false);
                send_verification_code_button.setAlpha(0.5f);
                // 开始倒计时
                countDownTimer = new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        send_verification_code_button.setText(millisUntilFinished / 1000 + "秒");
                    }
                    public void onFinish() {
                        send_verification_code_button.setEnabled(true);
                        send_verification_code_button.setText("获取");
                        send_verification_code_button.setAlpha(1.0f);
                    }
                }.start();
            } else {
                // 邮箱格式不合法，进行相应处理
                // 可以显示错误信息或者其他操作
                Toast.makeText(getApplicationContext(), "邮箱格式不合法", Toast.LENGTH_SHORT).show();
            }
        });

        register_button.setOnClickListener(v -> {
            String user_name = nameInput.getText().toString();
            String user_password = passwordInput.getText().toString();
            String user_email = emailInput.getText().toString();
            String user_verification_code = verificationCodeInput.getText().toString();

            Log.d(TAG, "user_name: " + user_name + ", user_password: " + user_password);
            Log.d(TAG, "user_email: " + user_email + ", user_verification_code: " + user_verification_code);
            if(Global.createCount(user_name, user_password, user_email, user_verification_code)){
                Intent intent=new Intent(Register.this, Login.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Register.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        });

        //回退页面
        back_button.setOnClickListener(v -> finish());
    }
}