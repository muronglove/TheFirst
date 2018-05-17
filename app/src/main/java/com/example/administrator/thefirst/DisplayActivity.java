package com.example.administrator.thefirst;
/**
 * Created by Brandon on 2018/1/1.
 */


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.administrator.thefirst.Adapter.DisplayAdapter;
import com.example.administrator.thefirst.helper.QueryDb;
import com.example.administrator.thefirst.helper.SimpleItemTouchHelperCallback;

import java.util.List;
import java.util.Map;

public class DisplayActivity extends Activity {
    private static final String TAG = "DisplayActivity";
    //private MyDatabaseHelper dbhelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        List<Map<String,Object>> listItems= QueryDb.showAll(this);
        Log.d(TAG, "onCreate: "+listItems.size());


        RecyclerView recyclerView=findViewById(R.id.recyclerview_display);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DisplayAdapter adapter = new DisplayAdapter(listItems,DisplayActivity.this);

        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        }catch (Exception e){
        e.printStackTrace();
    }
    }
}
