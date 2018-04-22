package com.example.administrator.thefirst;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeLineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,getData(),R.layout.timeline_item,new String[]{"show_time","image","title"},new int[]{R.id.show_time,R.id.image_1,R.id.title});
        ListView timelineList = (ListView)findViewById(R.id.timeline_list);
        timelineList.setAdapter(simpleAdapter);
    }

    private List<Map<String,Object>> getData(){
//        List<Map<String,Object>> result = new ArrayList<>();
//        Map<String,Object> map = new HashMap<>();
//        map.put("show_time",1);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第一幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",2);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第二幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",3);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第三幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",4);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第四幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",5);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第五幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",6);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第六幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",7);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第七幅图");
//        result.add(map);
//
//        map = new HashMap<>();
//        map.put("show_time",8);
//        map.put("image",R.drawable.ic_close);
//        map.put("title","第八幅图");
//        result.add(map);
//
//        return result;
    return QueryDb.showAll(TimeLineActivity.this);
    }
}
