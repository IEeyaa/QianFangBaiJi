package com.example.qianfangbaiji.OtherClass;

import android.database.Cursor;

public class Fangge {
    public int id;
    public String dynasty;
    public String table_name;
    public String info;
    public String book;
    public String content;
    public int isCut;
    public int isCollect;
    public int isPassed;
    public int q = 0;
    public int day = 0;

    public Fangge(int id, String dynasty, String table_name, String info, String book, String content){
        this.id = id;
        this.dynasty = dynasty;
        this.table_name = table_name;
        this.info = info;
        this.book = book;
        this.content = content;
        this.isCut = 0;
        this.isCollect = 0;
        this.isPassed = 0;
    }

    public Fangge(Cursor c){
        this.id = c.getInt(c.getColumnIndex("id"));
        this.dynasty = c.getString(c.getColumnIndex("dynasty"));
        this.book = c.getString(c.getColumnIndex("book"));
        this.content = c.getString(c.getColumnIndex("content"));
        this.table_name = c.getString(c.getColumnIndex("table_name"));
        this.info = c.getString(c.getColumnIndex("infor"));
        this.isCut = c.getInt(c.getColumnIndex("iscut"));
        this.isCollect = c.getInt(c.getColumnIndex("iscollect"));
        this.isPassed = c.getInt(c.getColumnIndex("ispassed"));
    }

    public Fangge(Cursor c, int q, int day){
        this.id = c.getInt(c.getColumnIndex("id"));
        this.dynasty = c.getString(c.getColumnIndex("dynasty"));
        this.book = c.getString(c.getColumnIndex("book"));
        this.content = c.getString(c.getColumnIndex("content"));
        this.table_name = c.getString(c.getColumnIndex("table_name"));
        this.info = c.getString(c.getColumnIndex("infor"));
        this.isCut = c.getInt(c.getColumnIndex("iscut"));
        this.isCollect = c.getInt(c.getColumnIndex("iscollect"));
        this.q = q;
        this.day = day;
    }
}
