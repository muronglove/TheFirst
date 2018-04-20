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
import android.util.Log;
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
    private static final String TAG = "DisplayActivity";
    //private MyDatabaseHelper dbhelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        List<Map<String,Object>> listItems=QueryDb.showAll(this);
        Log.d(TAG, "onCreate: "+listItems.size());
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
