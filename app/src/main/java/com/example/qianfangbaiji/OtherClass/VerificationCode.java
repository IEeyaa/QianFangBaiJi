package com.example.qianfangbaiji.OtherClass;

import java.util.Random;

public class VerificationCode {
    public static String generateVerificationCode(){
        Random random = new Random();
        int randomCode = random.nextInt(900000) + 100000; // 生成6位随机数
        return String.valueOf(randomCode);
    }
}