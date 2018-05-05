package com.example.administrator.thefirst.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.administrator.thefirst.Service.WebService;

/**
 * Created by Brandon on 2017/12/4.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_STORAGE = "create table storage("
            +"uuid text,"
            +"username text,"
            +"password text,"

            +"caption text,"
            +"label text,"
            +"number integer,"
            +"color text,"
            +"position text,"
            +"date text,"
            +"image blob,"

            +"a integer)";

    private Context mContext;
    //public static boolean flag = true;

    private static MyDatabaseHelper mInstance = null;

    public synchronized static MyDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDatabaseHelper(context, "Storage.db", null, 1);
        }
        return mInstance;
    };
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_STORAGE);


        try{
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();}catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}