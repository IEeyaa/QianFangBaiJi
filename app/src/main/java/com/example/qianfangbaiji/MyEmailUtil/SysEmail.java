package com.example.qianfangbaiji.MyEmailUtil;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.example.qianfangbaiji.OtherClass.Global;

import java.util.Random;

public class SysEmail {
    // 邮箱格式验证方法
    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+(\\.[a-z]+)+";
        return email.matches(emailPattern);
    }
    public static void sendEmail(Context context, String emailAddress){
        Random random = new Random();
        int randomCode = random.nextInt(900000) + 100000; // 生成6位随机数

        Global.storeMailInfo(emailAddress, String.valueOf(randomCode));
        // 创建发送邮件的Intent
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, "千方百剂验证");
        email.putExtra(Intent.EXTRA_TEXT, "你的验证码是：" + randomCode);
        startActivity(context, Intent.createChooser(email, "验证"), null);
    }
}
