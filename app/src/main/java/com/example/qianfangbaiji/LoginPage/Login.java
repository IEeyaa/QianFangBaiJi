package com.example.qianfangbaiji.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class Login extends AppCompatActivity {
    EditText name_input, password_input;
    Button login_button, register_button;
    TextView forget_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // EditText
        name_input = findViewById(R.id.name);
        password_input = findViewById(R.id.password);

        // TextView
        forget_password = findViewById(R.id.forget_password);

        // Buttons
        login_button = findViewById(R.id.login);
        register_button = findViewById(R.id.confirm);

        // 找回密码界面
        forget_password.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, RetrievePassword.class);
            startActivity(intent);
        });

        // 登录
        login_button.setOnClickListener(v -> {
            String user_name = name_input.getText().toString();
            String user_password = password_input.getText().toString();
            if(Global.checkCount(user_name, user_password)){
                Intent intent=new Intent(Login.this, daily.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(Login.this, "账号信息错误", Toast.LENGTH_SHORT).show();
                name_input.setText("");
                password_input.setText("");
            }
        });

        // 注册页面
        register_button.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

}