package com.example.qianfangbaiji.MyEmailUtil;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.example.qianfangbaiji.OtherClass.Global;
import com.example.qianfangbaiji.OtherClass.VerificationCode;

public class SysEmail {
    // 邮箱格式验证方法
    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z0-9]+(\\.[a-z]+)+";
        return email.matches(emailPattern);
    }
    public static void sendVerifyEmail(Context context, String emailAddress){
        String verification_code = VerificationCode.generateVerificationCode();
        Global.storeMailInfo(emailAddress, verification_code); // save verification code
        // 创建发送邮件的Intent
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, "千方百剂验证");
        email.putExtra(Intent.EXTRA_TEXT, "你的验证码是：" + verification_code);
        startActivity(context, Intent.createChooser(email, "验证"), null);
    }
}
