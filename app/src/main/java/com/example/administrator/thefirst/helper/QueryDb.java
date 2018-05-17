package com.example.administrator.thefirst.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.thefirst.Service.WebService;
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

        String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
        String username = "用户名:  "+cursor.getString(cursor.getColumnIndex("username"));
        String password = "密码:  "+cursor.getString(cursor.getColumnIndex("password"));
        String caption = "标题:  "+cursor.getString(cursor.getColumnIndex("caption"));
        String label = "标签:  "+cursor.getString(cursor.getColumnIndex("label"));
        String number = "数量:  "+cursor.getInt(cursor.getColumnIndex("number"));//注意类型
        String color = "颜色:  "+cursor.getString(cursor.getColumnIndex("color"));
        String position = "位置:  "+cursor.getString(cursor.getColumnIndex("position"));
        String date = "日期:  "+cursor.getString(cursor.getColumnIndex("date"));
        byte[] imageQuery = cursor.getBlob(cursor.getColumnIndex("image"));
        String a = "a:  "+cursor.getInt(cursor.getColumnIndex("a"));//注意类型

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageQuery,0,imageQuery.length);
        listItem.put("uuid",uuid);
        listItem.put("username",username);
        listItem.put("password",password);
        listItem.put("caption",caption);
        listItem.put("label",label);
        listItem.put("number",number);
        listItem.put("color",color);
        listItem.put("position",position);
        listItem.put("date",date);

        listItem.put("imageBitmap",imageBitmap);
        listItem.put("a",a);
        itemList.add(listItem);
    }

    public static List<Map<String,Object>> showAll(Context context){
        //SharedPreferences sp = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        String username = WebService.username;
        String password = WebService.password;
        MyDatabaseHelper dbhelper= MyDatabaseHelper.getInstance(context);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor cursor=db.query("storage",null,"username = '"+username+"' and password = '"+password+"' and a <> 1 and a <> 8",null,null,null,null,null);
        while(cursor.moveToNext()){
            addItem(cursor,itemList);
        }
        return itemList;
    }

    public static List<Map<String,Object>> queryByTag(String queryTag,Context context){
        String username = WebService.username;
        String password = WebService.password;
        MyDatabaseHelper dbhelper= MyDatabaseHelper.getInstance(context);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,"username = '"+username+"' and password = '"+password+"' and a <> 1 and a <> 8",null,null,null,null,null);
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
        String username = WebService.username;
        String password = WebService.password;
        MyDatabaseHelper dbhelper= MyDatabaseHelper.getInstance(context);
        List<Map<String,Object>> itemList=new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor=db.query("storage",null,"username = '"+username+"' and password = '"+password+"' and a <> 1 and a <> 8",null,null,null,null,null);
        String[] array =  queryStr.split("\\s+");
        while(cursor.moveToNext()){
            boolean flag = false;
            for(String s:array){
                if(cursor.getString(cursor.getColumnIndex("caption")).contains(s)||cursor.getString(cursor.getColumnIndex("label")).contains(s)||cursor.getString(cursor.getColumnIndex("color")).contains(s)||cursor.getString(cursor.getColumnIndex("position")).contains(s)||cursor.getString(cursor.getColumnIndex("date")).contains(s)){
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
        String username = WebService.username;
        String password = WebService.password;
        MyDatabaseHelper dbhelper= MyDatabaseHelper.getInstance(context);
        Set<String> labels=new HashSet<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Log.i("TAG","start query tag");
        Cursor cursor=db.query("storage",null,"username = '"+username+"' and password = '"+password+"' and a <> 1 and a <> 8",null,null,null,null,null);
        while (cursor.moveToNext()){
            labels.add(cursor.getString(cursor.getColumnIndex("label")));
            Log.i("TAG",cursor.getString(cursor.getColumnIndex("label")));
    }
        return new ArrayList<String>(labels);
    }





}
