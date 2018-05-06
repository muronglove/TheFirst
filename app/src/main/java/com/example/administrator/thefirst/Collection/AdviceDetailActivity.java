package com.example.administrator.thefirst.Collection;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.thefirst.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdviceDetailActivity extends AppCompatActivity {
    private static final String TAG = "hhhh";
    
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_detail);

        progressBar=findViewById(R.id.detail_progress_bar);

        Intent intent=getIntent();
        String data=intent.getStringExtra("article");

        try{
            new MyTask().execute(data);
            Log.i("Article","4");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class MyTask extends AsyncTask<String,Integer,List<Object>> {
        @Override
        protected List<Object> doInBackground(String... params) {
            List<Object> objlist = new ArrayList<>();
            try{
                Document doc = Jsoup.connect("http:"+params[0]).get();
                //Log.d(TAG, "doInBackground: "+"http:"+params[0]);
                doc = Jsoup.parse(doc.toString().replace("&nbsp;", ""));
                objlist.add(doc.select("h1").text());
                objlist.add(doc.select(".artInfo").text());
                objlist.add(doc.select(".artSum").text());
                Elements result = doc.select("p");
                for(Element pItem : result){
                    Log.i("Detail",""+pItem.childNodeSize());
                    Log.i("Detail",""+pItem.text());

                    if((pItem.childNodeSize()!=0)&&(pItem.hasAttr("style"))&&(pItem.child(0).is("img"))){
                        Bitmap bmp = null;
                        //Log.i("Dress",article.image);
                        //Log.d(TAG, "doInBackground: "+pItem.child(0).attr("src"));
                        URL url=new URL("http:"+pItem.child(0).attr("src"));
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        //从InputStream流中解析出图片
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                        objlist.add(bmp);
                    }else{
                        objlist.add(pItem.text());
                    }
                }

            }catch (Exception e){
                Log.d(TAG, "doInBackground: exception");
                e.printStackTrace();
            }
            return objlist;
        }

        @Override
        protected void onPostExecute(List<Object> objlist) {
            super.onPostExecute(objlist);
            try{

                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.dress_detail_linearlayout);

                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "onPostExecute: size:  "+objlist.size());

                TextView text1 =new TextView(AdviceDetailActivity.this);
                text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                text1.setGravity(Gravity.CENTER);
                text1.setText((String)objlist.get(0));
                text1.setTextColor(getResources().getColor(R.color.textColor));
                linearLayout.addView(text1);

                TextView text2=new TextView(AdviceDetailActivity.this);
                text2.setGravity(Gravity.CENTER);
                //text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                text2.setText((String)objlist.get(1));
                //text2.setTextColor(getResources().getColor(R.color.textColor));
                linearLayout.addView(text2);

                for(int i=3;i<objlist.size();i++){
                    Object obj=objlist.get(i);
                    if(obj instanceof Bitmap){
                        ImageView imageView = new ImageView(AdviceDetailActivity.this);
                        imageView.setImageBitmap((Bitmap)obj);
                        linearLayout.addView(imageView);
                    }else{
                        TextView textView = new TextView(AdviceDetailActivity.this);
                        textView.setText("    "+((String)obj).trim());
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        textView.setTextColor(getResources().getColor(R.color.textColor));
                        linearLayout.addView(textView);
                    }
                    //Log.d(TAG, "onPostExecute: "+i);
                }

//                Log.d(TAG, "onPostExecute: start");
//                TextView text3=new TextView(AdviceDetailActivity.this);
//                Log.d(TAG, "onPostExecute: start 1");
//                text3.setTextSize(15);
//                Log.d(TAG, "onPostExecute: start 2");
//                text3.setText((String)objlist.get(objlist.size()-1));
//                Log.d(TAG, "onPostExecute: start 3");
//                linearLayout.addView(text3);
//                Log.d(TAG, "onPostExecute: end");

            }catch (Exception e){
                Log.d(TAG, "onPostExecute: exception");
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
