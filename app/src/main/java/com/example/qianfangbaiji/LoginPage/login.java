package com.example.qianfangbaiji.LoginPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qianfangbaiji.DailyPage.daily;
import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.R;

public class login extends AppCompatActivity {
    EditText name, password;
    Button login, register;
    TextView find;
    String nameWord = "", passwordWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        find = findViewById(R.id.find);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameWord = name.getText().toString();
                passwordWord = password.getText().toString();
                if(Global.checkCount(nameWord, passwordWord)){
                    Intent intent=new Intent(login.this, daily.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(login.this, "账号信息错误", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    password.setText("");
                }
            }
        });

//      注册页面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
//      找回密码界面
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, refind.class);
                startActivity(intent);
            }
        });


    }

}