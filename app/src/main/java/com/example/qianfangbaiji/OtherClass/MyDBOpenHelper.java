package com.example.qianfangbaiji.OtherClass;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MyDBOpenHelper extends SQLiteOpenHelper {
    private static String PACKAGE_NAME = "com.example.qianfangbaiji"; //包名
    private static String DB_PATH =  "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";
    private static String DB_NAME = "database";
    private SQLiteDatabase db;
    private final Context context;

    public MyDBOpenHelper(Context context) {
        super(context,  DB_NAME , null, 1);
        this.context  = context;
    }


    public void createDB() throws IOException {
        this.getWritableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    public void copyDB() throws IOException{
        try {
            InputStream ip =  context.getAssets().open(DB_NAME+".db");
            Log.i("Input Stream....",ip+"");
            String op=  DB_PATH  +  DB_NAME ;
            OutputStream output = new FileOutputStream(op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
                Log.i("Content.... ",length+"");
            }
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.v("error", e.toString());
        }
    }

    public void openDB() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        Log.i("db path", myPath);
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.i("open DB......",db.toString());
    }

    @Override
    public synchronized void close() {

        if(db != null)
            db.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
