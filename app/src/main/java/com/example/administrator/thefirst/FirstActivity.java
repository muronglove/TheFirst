package com.example.administrator.thefirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.thefirst.Classification.ClassificationActivity;
import com.example.administrator.thefirst.Collection.CollectionAdviceActivity;

public class FirstActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button sn = (Button)findViewById(R.id.sn);
        sn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent1 = new Intent(FirstActivity.this,NewCamera.class);
                startActivity(intent1);
            }
        });
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
    }
}
