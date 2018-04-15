package com.example.administrator.thefirst;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class IntroductionActivity extends AppCompatActivity {

    public static final String CLOTHES_IMAGE_ID = "clothes_image_id";

    private TextView textView1;

    private TextView textView2;

    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        Intent intent = getIntent();
        int clothesImageId = intent.getIntExtra(CLOTHES_IMAGE_ID,0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView clothesImageView = (ImageView) findViewById(R.id.clothes_image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle("Detailed Information");
        Glide.with(this).load(clothesImageId).into(clothesImageView);
        textView1 = (TextView) findViewById(R.id.intro_text1);
        textView2 = (TextView) findViewById(R.id.intro_text2);
        textView3 = (TextView) findViewById(R.id.intro_text3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case    android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
