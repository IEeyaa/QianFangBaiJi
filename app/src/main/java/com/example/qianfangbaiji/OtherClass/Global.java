package com.example.qianfangbaiji.OtherClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//    全局变量
public class Global {

    private static String date = "0000-00-00";

    private static SharedPreferences sharedPreferences;

//    全局固定变量
//    总条数
    public static int total_number = 205;
//    当前已经完成
    public static int already_over_number = 0;
//    问题个数
    public static int question_number = 4;
//    问题答案个数
    public static int answer_number = 3;
//    每日固定新背诵个数
    public static int daily_new_number = 5;


//    全局可变变量
    public static int stage = 0;
//    今日所有背诵条文数量
    public static int number = 0;
//    当前背诵条文序列号
    public static int now = 0;
//    新学条文分界线
    public static int edge = 0;
//    条文总信息
    public static List<int[]> fangge_info_array = new ArrayList<>();

    public static void init(Context context){
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
//      格式化日期为字符串
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        sharedPreferences = context.getSharedPreferences("global_prefs", Context.MODE_PRIVATE);
        String preDate = sharedPreferences.getString("date", "0000-00-00");
//        基本全局变量更新
        LoadInfo();
//        时间更新
        if(!formattedDate.equals(preDate)){
            RestartInfo();
            date = formattedDate;
            sharedPreferences.edit().putString("date", formattedDate).apply();
        }
    }

    @SuppressLint("DefaultLocale")
//      每日更新 & 更换学习计划 & 主动开始新的一轮 时使用
    public static void RestartInfo() {
//      数据库加载，复习部分
        Cursor c = MySQLHelper.getInstance().sqlSelect(
                "SELECT id, ispassed\n" +
                "FROM fangge\n" +
                "WHERE ispassed = 3 AND iscut != 1\n");
        c.moveToFirst();
        int tempEdge = 0;
        fangge_info_array.clear();
        while (!c.isAfterLast()) {
            fangge_info_array.add(new int[]{c.getInt(c.getColumnIndex("id")), 0});
            tempEdge++;
            c.moveToNext();
        }
        c.close();
//      数据库加载，新学部分   3 6
        c = MySQLHelper.getInstance().sqlSelect(String.format(
                "SELECT id, ispassed\n" +
                "FROM fangge\n" +
                "WHERE ispassed = 0 AND iscut != 1\n" +
                "LIMIT %d", daily_new_number));
        c.moveToFirst();
        int tempNumber = tempEdge;
        edge = tempEdge;

        while (!c.isAfterLast()) {
            fangge_info_array.add(new int[]{c.getInt(c.getColumnIndex("id")), 0});
            tempNumber++;
            c.moveToNext();
        }
        c.close();
//      纠错
        c = MySQLHelper.getInstance().sqlSelect("SELECT id FROM fangge WHERE ispassed = 2");
        already_over_number = c.getCount();
        c.close();

        number = tempNumber;
        stage = 0;
        if(edge == 0){
            stage = 1;
        }
        now = 0;
        StoreInfo();
    }

    public static void LoadInfo(){
        total_number = sharedPreferences.getInt("total_number", total_number);
        already_over_number = sharedPreferences.getInt("already_over_number", already_over_number);
        daily_new_number = sharedPreferences.getInt("daily_new_number", daily_new_number);

        int tempNumber;
        int tempQ;
        now = sharedPreferences.getInt("now", now);
        edge = sharedPreferences.getInt("edge", edge);
        number = sharedPreferences.getInt("number", number);
        stage = sharedPreferences.getInt("stage", stage);

        fangge_info_array.clear();
        for(int i=0;i<number; i++){
            tempNumber = sharedPreferences.getInt("array" + i, 0);
            tempQ = sharedPreferences.getInt("q" + i, 0);
            fangge_info_array.add(new int[]{tempNumber, tempQ});
        }
    }

    public static void StoreInfo(){
        sharedPreferences.edit().putInt("total_number", total_number).apply();
        sharedPreferences.edit().putInt("already_over_number", already_over_number).apply();
        sharedPreferences.edit().putInt("daily_new_number", daily_new_number).apply();

        int i = 0;
        for(int[] item: fangge_info_array){
            sharedPreferences.edit().putInt("array" + i, item[0]).apply();
            sharedPreferences.edit().putInt("q" + i, item[1]).apply();
            i++;
        }

        sharedPreferences.edit().putInt("number", number).apply();
        sharedPreferences.edit().putInt("now", now).apply();
        sharedPreferences.edit().putInt("edge", edge).apply();
        sharedPreferences.edit().putInt("stage", stage).apply();
    }

    public static void LazyStore(int store_number){
        sharedPreferences.edit().putInt("q" + store_number, fangge_info_array.get(store_number)[1]).apply();
        sharedPreferences.edit().putInt("now", now).apply();
        sharedPreferences.edit().putInt("stage", stage).apply();
    }
//    重置所有参数
    public static void ReStart(){
        String sql = "UPDATE fangge SET nowdate = 0, ispassed = 0, maxdate = 0, times = 0, EF = 2.5";
        MySQLHelper.getInstance().sqlOther(sql);
    }
}
