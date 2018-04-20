package com.example.administrator.thefirst;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.thefirst.helper.MyDatabaseHelper;

import org.apache.http.client.fluent.Content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ZZh on 2018/4/20.
 */

public class QueryDb {
    private static void addItem(Cursor cursor,List<Map<String,Object>> itemList){
        Map<String,Object> listItem = new HashMap<String,Object>();
        String color = "颜色:"+cursor.getString(1);
        String caption = "标题:"+cursor.getString(2);
        String tag = "标签:"+cursor.getString(3);
        String cabinet = "柜子:"+cursor.getInt(4);
        byte[] imageQuery = cursor.getBlob(5);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageQuery,0,imageQuery.length);
        listItem.put("color",color);
        listItem.put("caption",caption);
        listItem.put("tag",tag);
        listItem.put("cabinet",cabinet);
        listItem.put("imageBitmap",imageBitmap);
        itemList.add(listItem);
    }

    public static List<Map<String,Object>> showAll(Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"Storage.db",null,1);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            addItem(cursor,itemList);
        }
        return itemList;
    }

    public static List<Map<String,Object>> queryByTag(String queryTag,Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"Storage.db",null,1);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,null,null,null,null,null,null);

        while(cursor.moveToNext()){
            boolean flag = false;

            if(cursor.getString(3)==queryTag){
                flag = true;
                break;
            }
            if(flag){
                addItem(cursor,itemList);
            }
        }
        return itemList;
    }

    public static List<Map<String,Object>> queryAll(String queryStr,Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"Storage.db",null,1);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,null,null,null,null,null,null);

        String[] array =  queryStr.split("\\s+");
        while(cursor.moveToNext()){
            boolean flag = false;
            for(String s:array){
                if(cursor.getString(1).contains(s)||cursor.getString(2).contains(s)||cursor.getString(3).contains(s)||cursor.getString(4).contains(s)){
                    flag = true;
                    break;
                }
            }
            if(flag){
                addItem(cursor,itemList);
                //String Temperature = cursor.getString(1);
                //TextView tv = new TextView(this);
                //2.把人物的信息设置为文本框的内容
                //tv.setText(id+": "+Temperature);
                //tv.setTextSize(18);
                //设置完上述两条语句并不会把TextView显示在界面上，
                //所以需要第三步，将其与layout关联起来；
                //3.把textView设置为线性布局的子节点
                //ll.addView(tv);
            }
        }
        return itemList;
    }

    public static List<String> getTags(Context context){
        MyDatabaseHelper dbhelper=new MyDatabaseHelper(context,"Storage.db",null,1);
        Set<String> tags=new HashSet<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            tags.add(cursor.getString(3));
    }
        return new ArrayList<String>(tags);
    }


}
