package com.example.administrator.thefirst.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Brandon on 2017/12/4.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_STORAGE = "create table storage("
            +"id integer primary key autoincrement,"
            +"color text,"
            +"caption text,"
            +"tag text,"
            +"cabinet integer,"
            +"image blob,"
            +"time text)";

    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_STORAGE);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}