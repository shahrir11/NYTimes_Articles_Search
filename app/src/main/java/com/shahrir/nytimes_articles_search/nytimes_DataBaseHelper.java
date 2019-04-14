package com.shahrir.nytimes_articles_search;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.app.Activity;



public class nytimes_DataBaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "MyDatabase";
    public static final String TABLE_NAME = "ArticlesTable";
    public static final int VERSION_NUM = 9;
    public static final String COL_ID = "_id";
    public static final String COL_HEADER = "ARTICLE_HEADER";
    public static final String COL_URL = "ARTICLE_URL";
    public static final String COL_PIC_URL = "PICTURE_URL";


    //creating constructor of the class, matching super
   public nytimes_DataBaseHelper(Activity contex){
       super(contex, DATABASE_NAME, null,VERSION_NUM);

   }

   //Initiating Database Filed
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COL_HEADER + " TEXT, " + COL_URL + " TEXT, " + COL_PIC_URL + " TEXT)" );
    }

    //dropping old tables, before creating new on UPGRADING
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       //debugging point
        Log.i("DB upgrade", "Old Versn:" + oldVersion + " new Versn:" + newVersion);

        //Drop tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //create tables
        onCreate(db);

    }
    //dropping old tables, before creating new on DOWNGRADING
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //debugging point
        Log.i("DB downgrade", "Old Versn:" + oldVersion + " new Versn:" + newVersion);

        //Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //create table
        onCreate(db);


    }
}
