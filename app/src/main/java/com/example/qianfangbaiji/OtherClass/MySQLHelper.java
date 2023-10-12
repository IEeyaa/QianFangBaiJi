package com.example.qianfangbaiji.OtherClass;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class MySQLHelper {
    private static MySQLHelper instance = new MySQLHelper();
    private MyDBOpenHelper db;
    private SQLiteDatabase db1;

    private MySQLHelper() {}

    public void createDB(Context context){
        instance.db = new MyDBOpenHelper(context);
        try {
            instance.db.openDB();
        } catch (SQLException sqle) {
            try {
                instance.db.createDB();
            } catch (IOException ioe) {
                throw new Error("Database not created....");
            }
        }
        instance.db.close();
    }

    public Cursor sqlSelect(String sql){
        instance.db1 = instance.db.getWritableDatabase();
        Cursor c = instance.db1.rawQuery(sql, null);
        return c;
    }

    public void sqlOther(String sql){
        instance.db1 = instance.db.getWritableDatabase();
        instance.db1.execSQL(sql);
    }

    public static MySQLHelper getInstance(){
        return instance;
    }
}