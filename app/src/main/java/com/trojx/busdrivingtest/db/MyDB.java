package com.trojx.busdrivingtest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.trojx.busdrivingtest.domain.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class MyDB {
    /**
     * 数据库名
     */
    public  static  final  String DB_NAME="record";
    public static final int DB_VERSION=1;
    private  static  MyDB myDB;
    private SQLiteDatabase db;

    /**
     * 私有化构造方法
     * @param context
     */
    private  MyDB(Context context){
        MyOpenHelper myOpenHelper=new MyOpenHelper(context,DB_NAME,null,DB_VERSION);
        db=myOpenHelper.getWritableDatabase();
    }

    /**
     * 获取myDB实例
     * @param context
     * @return
     */
    public synchronized  static MyDB getInstance(Context context){
        if (myDB==null){
            myDB=new MyDB(context);
        }
        return myDB;
    }

    /**
     * 将record实例存入数据库
     * @param record
     */
    public void saveRecord(Record record){
        if(record!=null){
            ContentValues values=new ContentValues();
            Date mdate=record.getDate();
//            Timestamp timestamp = new Timestamp(mdate.getTime());
             long mdateTime=mdate.getTime();
            values.put("SN",record.getSN());
            values.put("time",mdateTime);
            values.put("XAccel",record.getxAccel());
            values.put("YAccel",record.getyAccel());
            values.put("ZAccel",record.getzAccel());
            values.put("Lat",record.getLat());
            values.put("Lng",record.getLng());
            values.put("XOrien",record.getxOrien());
            values.put("YOrien",record.getyOrien());
            values.put("ZOrien",record.getzOrien());
            db.insert("Record",null,values);
        }
    }

    /**
     * 按SN号取出所有record记录
     */
    public List<Record> loadRecords(int SN){
        List<Record> list = new ArrayList<Record>();
        Cursor cursor=db.query("Record",null,"SN=?",new String[]{String.valueOf(SN)},null,null,null);
        if(cursor.moveToFirst()){
            Record record=new Record();
            record.setSN(SN);
            long time=cursor.getInt(cursor.getColumnIndex("time"));
            Date mdate=new Date(time);
            record.setDate(mdate);
            record.setxAccel(cursor.getFloat(cursor.getColumnIndex("XAccel")));
            record.setyAccel(cursor.getFloat(cursor.getColumnIndex("YAccel")));
            record.setzAccel(cursor.getFloat(cursor.getColumnIndex("ZAccel")));
            record.setLat(cursor.getFloat(cursor.getColumnIndex("Lat")));
            record.setLng(cursor.getFloat(cursor.getColumnIndex("Lng")));
            record.setxOrien(cursor.getFloat(cursor.getColumnIndex("XOrien")));
            record.setyOrien(cursor.getFloat(cursor.getColumnIndex("YOrien")));
            record.setzOrien(cursor.getFloat(cursor.getColumnIndex("ZOrien")));
            list.add(record);
        }
        if(cursor!=null){
            cursor.close();
        }
        return  list;
    }


}
