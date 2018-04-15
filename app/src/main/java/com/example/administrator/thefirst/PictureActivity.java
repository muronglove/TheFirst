package com.example.administrator.thefirst;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    private Clothes[] clothes = {new Clothes(R.drawable.a),new Clothes(R.drawable.b),new Clothes(R.drawable.c),
            new Clothes(R.drawable.d),
    new Clothes(R.drawable.e),new Clothes(R.drawable.f)};

    private List<Clothes> clothesList = new ArrayList<>();

    private ClothesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initClothes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutMannager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutMannager);
        adapter = new ClothesAdapter(clothesList);
        recyclerView.setAdapter(adapter);
    }

    private void initClothes(){
        clothesList.clear();
        for ( int i = 0; i < 6; i++) {
            clothesList.add(clothes[i]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.back:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
