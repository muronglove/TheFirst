package com.example.administrator.thefirst.Collection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.thefirst.R;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CollectionAdviceActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AdviceAdapter adapter;
    private List<Advice> adviceList = new ArrayList<>();

    private List<Bitmap> bitmapList;

    private org.jsoup.nodes.Document doc;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x9527:
                    //final AdviceAdapter adapter = new AdviceAdapter(adviceList);
                    progressBar.setVisibility(View.GONE);
                    adapter = new AdviceAdapter(adviceList,bitmapList);
                    adapter.setOnItemClickListener(new AdviceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position) {
                            Advice advice = adviceList.get(position);
                            Intent intent = new Intent(CollectionAdviceActivity.this, AdviceDetailActivity.class);
                            intent.putExtra("article", advice.getArticle());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    break;
                default:break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_advice);


        Toolbar toolbar = findViewById(R.id.toolbar_collection);
        setSupportActionBar(toolbar);

        progressBar=findViewById(R.id.progress_bar_collection);
        progressBar.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerview_collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        final AdviceAdapter adapter = new AdviceAdapter(adviceList);
//        adapter.setOnItemClickListener(new AdviceAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClickListener(int position) {
//                Advice advice = adviceList.get(position);
//                Intent intent = new Intent(CollectionAdviceActivity.this, WebActivity.class);
//                intent.putExtra("url", advice.getArticle());
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                InitArticleList();
                bitmapList= getBitmapList(adviceList);
                Message msg=new Message();
                msg.what=0x9527;
                handler.sendMessage(msg);
            }
        }).start();

//        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipe_refresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                InitArticleList();
//                                adapter.notifyDataSetChanged();
//                                swipeRefresh.setRefreshing(false);
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
    }

    private void InitArticleList() {
        try {
            doc = Jsoup.connect("http://dress.pclady.com.cn/style/").get();
            Elements iPic = doc.select(".iPic");
            Elements eTit = doc.select(".eTit");
            Elements eTime = doc.select(".eTime");
            Elements sDes = doc.select(".sDes");
            Elements sLab = doc.select(".sLab");
            //Log.i("Dress",listItems.html());
            for (int i = 0; i < iPic.size(); i++) {
                String image = iPic.get(i).child(0).child(0).attr("src");
                String caption = eTit.get(i).text();
                String time = eTime.get(i).text();
                String detail = sDes.get(i).text();
                String tag = sLab.get(i).text();
                String article = eTit.get(i).child(0).attr("href");
                Advice articleItem = new Advice(image, caption, time, detail, tag, article);
                adviceList.add(articleItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<Bitmap> getBitmapList(List<Advice> adviceList){
        List<Bitmap> bitmapList=new ArrayList<>();
        for(Advice advice:adviceList){
            try {
                Bitmap bmp = null;
                //Log.i("Dress",article.image);
                URL url=new URL("http:"+advice.image);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                //从InputStream流中解析出图片
                bmp = BitmapFactory.decodeStream(is);
                Bitmap resizeBmp= ThumbnailUtils.extractThumbnail(bmp,300,380);
                is.close();
                bitmapList.add(resizeBmp);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bitmapList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_close:
                //Toast.makeText(this, "Chicked", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
