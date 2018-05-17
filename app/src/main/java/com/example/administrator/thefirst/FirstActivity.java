package com.example.administrator.thefirst;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thefirst.Classification.ClassificationActivity;
import com.example.administrator.thefirst.Collection.CollectionAdviceActivity;
import com.example.administrator.thefirst.Service.WebService;
import com.example.administrator.thefirst.helper.MyDatabaseHelper;
import com.example.administrator.thefirst.helper.QueryDb;

import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private ServiceConnection conn;
    private WebService webService;
    private WebServiceReceiver receiver;
    private String content = "";
    private boolean flag = false;
    private ImageView mDynamicCircle;
    private ObjectAnimator mCircleAnimator;
    MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //绑定服务和广播接收器
        startAndBindService();




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
            mDynamicCircle = (ImageView) findViewById(R.id.image_rotate);
            mCircleAnimator = ObjectAnimator.ofFloat(mDynamicCircle, "rotation", 0.0f, 360.0f);
            mCircleAnimator.setDuration(1000);
            mCircleAnimator.setInterpolator(new LinearInterpolator());
            mCircleAnimator.setRepeatCount(-1);
            mCircleAnimator.setRepeatMode(ObjectAnimator.RESTART);
            mDynamicCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        mCircleAnimator.start();
                        webService.sync(FirstActivity.this);
                }
            });

            CardView cardView = (CardView)findViewById(R.id.activity_first_cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this,ClassificationActivity.class);
                    startActivity(intent);
                }
            });



            Button user = (Button)findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try{Intent intent = new Intent(FirstActivity.this, UserActivity.class);
                startActivity(intent);}catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //铃铛按钮
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
                try{
                    Intent intent = new Intent(FirstActivity.this,SearchResultActivity.class);
                    intent.putExtra("str",searchView.getQuery().toString());
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //绑定服务和广播接收器
    public void startAndBindService(){
        receiver = new WebServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendContent");
        registerReceiver(receiver,intentFilter);
        conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //返回一个MsgService对象
                webService = ((WebService.WebBinder)service).getService();
                webService.sync(FirstActivity.this);
                mCircleAnimator.start();
            }
        };

        Intent serviceInent = new Intent(this,WebService.class);
        startService(serviceInent);

        //绑定Service
        Intent intent = new Intent(this,WebService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        List list = QueryDb.showAll(this);
        TextView text_sum = (TextView)findViewById(R.id.sum);
        text_sum.setText("目前一共收纳"+list.size()+"件物品");
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("TAG", "-- "+msg);
            if(content.equals("syncfromclientover")){
                mCircleAnimator.end();
                Toast.makeText(FirstActivity.this,"同步完成",Toast.LENGTH_SHORT).show();
            }
        }
    };


    class WebServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context,intent.getAction(),Toast.LENGTH_SHORT).show();
            content = intent.getStringExtra("content");
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
