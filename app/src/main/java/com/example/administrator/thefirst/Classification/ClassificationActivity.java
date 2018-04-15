package com.example.administrator.thefirst.Classification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.administrator.thefirst.Collection.CollectionAdviceActivity;
import com.example.administrator.thefirst.PictureActivity;
import com.example.administrator.thefirst.R;
import com.example.administrator.thefirst.TimeLineActivity;

import java.util.ArrayList;
import java.util.List;

public class  ClassificationActivity extends AppCompatActivity {
    private static final String TAG = "ClassificationActivity";
    
    private List<Classification> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        Button btnTimeLine=(Button) findViewById(R.id.btn_timeline);
        btnTimeLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ClassificationActivity.this,TimeLineActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreate: first of all");
        
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar_classification);
        
        setSupportActionBar(toolbar);
        init();

        Log.d(TAG, "onCreate: after init");
        
        RecyclerView recyclerView=findViewById(R.id.recyclerview_classification);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ClassAdapter adapter=new ClassAdapter(list);
        adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void clicked(int position) {
//                Classification classification=list.get(position);
//                Toast.makeText(ClassificationActivity.this,classification.getName(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ClassificationActivity.this,PictureActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_close:
                Toast.makeText(ClassificationActivity.this,"classificationActivity clicked",Toast.LENGTH_LONG).show();
                finish();
                break;
            default:break;
        }
        return true;
    }

    void init(){
        Classification classification1=new Classification();
        classification1.setName("上衣");
        list.add(classification1);

        Classification classification2=new Classification();
        classification2.setName("裤子");
        list.add(classification2);

        Classification classification3=new Classification();
        classification3.setName("帽子");
        list.add(classification3);

        Classification classification4=new Classification();
        classification4.setName("围巾");
        list.add(classification4);

        Classification classification5=new Classification();
        classification5.setName("口罩");
        list.add(classification5);

        Classification classification6=new Classification();
        classification6.setName("袜子");
        list.add(classification6);

    }
}
