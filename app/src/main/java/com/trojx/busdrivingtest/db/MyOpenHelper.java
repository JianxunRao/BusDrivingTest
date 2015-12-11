package com.trojx.busdrivingtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/11.
 */
public class MyOpenHelper extends SQLiteOpenHelper {
    /**
     * 建表语句
     */
    public static  final  String Create_Record="create table Record(id integer primary key autoincrement,SN integer,time integer,XAccel real,YAccel real,ZAccel real,Lat real,Lng real,XOrien real,YOrien real,ZOrien real)";

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(Create_Record);//创建Record表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
