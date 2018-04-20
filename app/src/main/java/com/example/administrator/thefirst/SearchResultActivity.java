package com.example.administrator.thefirst;

/**
 * Created by Brandon on 2018/1/1.
 */


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.administrator.thefirst.helper.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends Activity {

    //private MyDatabaseHelper dbhelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        List<Map<String,Object>> listItems;
        //dbhelper = new MyDatabaseHelper(this,"Storage.db",null,1);
        //SQLiteDatabase db = dbhelper.getWritableDatabase();
        //Cursor cursor = db.query("storage", null, null, null, null, null, null, null);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");

        listItems=QueryDb.queryAll(str,this);

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
