package com.example.administrator.thefirst;
/**
 * Created by Brandon on 2018/1/1.
 */


import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;


import com.example.administrator.thefirst.helper.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DisplayActivity extends Activity {

    private MyDatabaseHelper dbhelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        dbhelper = new MyDatabaseHelper(this,"Storage.db",null,1);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("storage", null, null, null, null, null, null, null);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        while(cursor.moveToNext()){
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
            listItems.add(listItem);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.item,new String[]{"color","caption","tag","cabinet","imageBitmap"},new int[]{R.id.color,R.id.caption,R.id.tag,R.id.cabinet,R.id.image_item});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        ListView myList = (ListView)findViewById(R.id.myList);
        myList.setAdapter(simpleAdapter);
    }
}
