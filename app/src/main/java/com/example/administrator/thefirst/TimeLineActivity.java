package com.example.administrator.thefirst;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.thefirst.helper.QueryDb;

import java.util.List;
import java.util.Map;

public class TimeLineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

//        SimpleAdapter simpleAdapter = new SimpleAdapter(this,getData(),R.layout.timeline_item,new String[]{"color","caption","tag","cabinet","imageBitmap"},new int[]{R.id.show_time,R.id.image_1,R.id.title});
//        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data,
//                                        String textRepresentation) {
//                // TODO Auto-generated method stub
//                if (view instanceof ImageView && data instanceof Bitmap) {
//                    ImageView i = (ImageView) view;
//                    i.setImageBitmap((Bitmap) data);
//                    return true;
//                }
//                return false;
//            }
//        });
//        ListView timelineList = (ListView)findViewById(R.id.timeline_list);
//        timelineList.setAdapter(simpleAdapter);
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
