package com.example.administrator.thefirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.administrator.thefirst.Classification.ClassificationActivity;
import com.example.administrator.thefirst.Collection.CollectionAdviceActivity;

public class FirstActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        try{
            Button sn = (Button)findViewById(R.id.sn);
            sn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent1 = new Intent(FirstActivity.this,NewCamera.class);
                    startActivity(intent1);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        Button user = (Button)findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(FirstActivity.this, ClassificationActivity.class);
                startActivity(intent2);
            }
        });
        Button tj = (Button)findViewById(R.id.tj);
        tj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent3 = new Intent(FirstActivity.this,CollectionAdviceActivity.class);
                startActivity(intent3);
            }
        });

        //搜索
        final SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(FirstActivity.this,SearchResultActivity.class);
                intent.putExtra("str",searchView.getQuery().toString());
                startActivity(intent);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //展示所有物品
        Button display = (Button)findViewById(R.id.display);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,DisplayActivity.class);
                startActivity(intent);

            }
        });}catch (Exception e){
            e.printStackTrace();
        }

    }
}
