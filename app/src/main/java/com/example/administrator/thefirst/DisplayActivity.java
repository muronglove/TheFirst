package com.example.administrator.thefirst;
/**
 * Created by Brandon on 2018/1/1.
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.administrator.thefirst.helper.QueryDb;

import java.util.List;
import java.util.Map;

public class DisplayActivity extends Activity {
    private static final String TAG = "DisplayActivity";
    //private MyDatabaseHelper dbhelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);

        List<Map<String,Object>> listItems= QueryDb.showAll(this);
        Log.d(TAG, "onCreate: "+listItems.size());
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.timeline_item,new String[]{"uuid","username","password","caption","label","number","color","position","date","imageBitmap","a"},new int[]{R.id.uuid,R.id.username,R.id.password,R.id.caption,R.id.label,R.id.color,R.id.number,R.id.position,R.id.show_time,R.id.image_item,R.id.a});
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
