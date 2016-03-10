package com.team3.wirelessattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class StuDatabaseHelper {

    private static final int dbVersion = 13;
    private static final String tablename = "attend";
    private static final String dbName = "stutracker.db";
    private static final String stuID = "_id";
    private static final String stuName = "name";
    private static final String stuAddr = "address";
    private static final String stuRoll = "roll";


    public static StuTrackerOpenHelper stuTrackerOpenHelper;
    private static SQLiteDatabase db;
    private static StuDatabaseHelper sInstance;

    public static synchronized StuDatabaseHelper getInstance(Context context){
        if (sInstance==null){
            sInstance = new StuDatabaseHelper(context);
            return sInstance;
        }
        else
            return sInstance;
    }

    private StuDatabaseHelper(Context context){

        stuTrackerOpenHelper = new StuTrackerOpenHelper(context);
        db = stuTrackerOpenHelper.getWritableDatabase();

    }

    public Cursor viewAttendance(String day){

        if(CheckDate(day)) {
            return db.rawQuery("SELECT * FROM " + tablename + " WHERE d" + day + "=1", null);
        }
        else{
            return db.rawQuery("SELECT * FROM "+tablename+" WHERE 1=0",null);
        }
    }

    public int getAttCount(String day){
        if(CheckDate(day)) {
            Cursor c = db.rawQuery("SELECT * FROM " + tablename + " WHERE d" + day + "=1", null);
            return c.getCount();
        }
        else{
            return 0;
        }
    }

    public Cursor InitDay(String day){

        Cursor sendToAdpt;
        Boolean dateflag = CheckDate(day);
        if(!dateflag){
            db.execSQL("ALTER TABLE "+tablename+" ADD 'd"+day+"' integer DEFAULT 0");
        }
        db.execSQL("UPDATE " + tablename + " SET 'd"+day+"'= 0");
        sendToAdpt = db.rawQuery("SELECT * FROM "+tablename+" WHERE 'd"+day+"'= 1",null);
        return sendToAdpt;

    }

    public void BroadReceive(String address, String day, StudentTrackerAdaptor studentTrackerAdaptor){

        Cursor addr = db.rawQuery("SELECT * FROM "+tablename+" WHERE "+stuAddr+"='"+address+"'",null);
        //String att;
        if(addr.getCount()!=0){

            String name;//roll;
            addr.moveToFirst();
            name = addr.getString(addr.getColumnIndexOrThrow(stuName));
            //roll = addr.getString(addr.getColumnIndexOrThrow(stuRoll));

            ContentValues cv = new ContentValues();
            cv.put("'d"+day+"'",1);

            int affected = db.update(tablename,cv,"address ='"+address+"'",null);
            //db.rawQuery("UPDATE "+tablename+" SET '"+day+"'='1' WHERE "+stuAddr+"='"+address+"'",null);
            addr.close();
            if(affected!=1){
                Log.d("UnexpectedSQLBehaviour","Non-singular("+affected+") attendance registered while updating "+name+":"+address);
            }
        }
        Cursor adptCursor = db.rawQuery("SELECT * FROM "+tablename+" WHERE d"+day+"=1", null);

        //adptCursor =db.query(tablename,adptCursor.getColumnNames(),"'"+day+"' = 1",null,null,null,null);
        /*Cursor debugCuror = db.rawQuery("SELECT * FROM "+tablename,null);
        debugCuror.moveToFirst();
        int val1 = debugCuror.getInt(debugCuror.getColumnIndex("d"+day));
        debugCuror.moveToNext();
        int val2 = debugCuror.getInt(debugCuror.getColumnIndex("d"+day));

        int colcount= adptCursor.getColumnCount();
        String colList[] = adptCursor.getColumnNames();
        adptCursor.moveToFirst();
        int cint = adptCursor.getCount();*/

        studentTrackerAdaptor.changeCursor(adptCursor);
        studentTrackerAdaptor.notifyDataSetChanged();

    }

    private boolean CheckDate(String date){

        Cursor cursor = db.rawQuery("SELECT * FROM "+tablename,null);
        if(cursor.getColumnIndex("d"+date)==-1){
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }
    }


    private class StuTrackerOpenHelper extends SQLiteOpenHelper {

        StuTrackerOpenHelper(Context context) {
            super(context, dbName, null, dbVersion);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table " + tablename + " ('" + stuID + "' integer primary key, " + stuName + " text, " + stuAddr + " text, " + stuRoll + " text)");

            //Enter all student details in ContentValues, BEGIN

            ContentValues nokia = new ContentValues();
            nokia.put(stuName,"Nokia E63");
            nokia.put(stuAddr, "0C:DD:EF:04:96:52");
            nokia.put(stuRoll, "02");
            db.insert(tablename, null, nokia);

            ContentValues rca = new ContentValues();
            rca.put(stuAddr,"8C:84:01:C2:A5:B7");
            rca.put(stuRoll,"03");
            rca.put(stuName, "RCA Tablet");
            db.insert(tablename, null, rca);

            ContentValues anish = new ContentValues();
            anish.put(stuAddr,"50:A7:2B:3D:F2:09");
            anish.put(stuRoll,"12");
            anish.put(stuName, "Anish Ravishankar");
            db.insert(tablename, null, anish);

            ContentValues amrith1 = new ContentValues();
            amrith1.put(stuAddr,"A0:F8:95:17:CE:4A");
            amrith1.put(stuRoll,"10");
            amrith1.put(stuName, "Amrith M");
            db.insert(tablename, null, amrith1);

            ContentValues amrith2 = new ContentValues();
            amrith2.put(stuAddr,"D8:3C:69:0D:0A:9A");
            amrith2.put(stuRoll,"11");
            amrith2.put(stuName, "Sulatha");
            db.insert(tablename, null, amrith2);

            ContentValues sandra = new ContentValues();
            sandra.put(stuAddr,"5C:51:88:3B:E4:DE");
            sandra.put(stuRoll,"49");
            sandra.put(stuName, "Sandra Maria Jose");
            db.insert(tablename, null, sandra);


            //END of Student Details



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("drop table if exists " +tablename);
            onCreate(db);

        }
    }
}

