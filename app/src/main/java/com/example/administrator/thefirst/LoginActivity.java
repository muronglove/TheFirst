package com.example.administrator.thefirst;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.thefirst.Service.WebService;

public class LoginActivity extends Activity {
    private Button login;//登录按钮
    private Button register;

    private EditText et1;//输入账号输入框
    private EditText et2;//输入密码输入框

    private CheckBox remember_key;//记住密码勾选框
    private CheckBox automatic_login;//自动登录选框
    private SharedPreferences sp;

    private String userNameValue;
    private String passwordValue;
    private String content = "";
    private Dialog mDialog = null;

    private Context mContext = this;

    private ServiceConnection conn;
    private WebService webService;
    private WebServiceReceiver receiver;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);

        //对but进行实例化
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);
        remember_key = (CheckBox) findViewById(R.id.remember_pass);
        automatic_login = (CheckBox) findViewById(R.id.auto_login);

        //创建but的单击事件，参数要传匿名内部类
        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);


        //绑定服务和广播接收器
        startAndBindService();

        remember_key.setChecked(true);//设置记住密码初始化为true

        //判断记住密码多选框的状态
        if (sp.getBoolean("rem_isCheck", false)) {
            //设置默认是记录密码状态
            remember_key.setChecked(true);
            et1.setText(sp.getString("username", ""));
            et2.setText(sp.getString("password", ""));
            Log.i("自动恢复保存的账号密码", "自动恢复保存的账号密码");

            //判断自动登陆多选框状态
            if (sp.getBoolean("auto_isCheck", false)) {
                //设置默认是自动登录状态
                automatic_login.setChecked(true);
//                try{webService.login(sp.getString("username", ""),sp.getString("password", ""));}catch (Exception e){
//                    e.printStackTrace();
//                }
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();
                //Toast toast1 = Toast.makeText(getApplicationContext(), "已自动登录", Toast.LENGTH_SHORT);
                Log.e("自动登陆", "自动登陆");
            }
        }


        //登录按钮监听方法
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userNameValue = et1.getText().toString();
                passwordValue = et2.getText().toString();
                webService.login(userNameValue,passwordValue);
            }
        });

        //注册按钮监听方法
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameValue = et1.getText().toString();
                passwordValue = et2.getText().toString();
                webService.register(userNameValue,passwordValue);
            }
        });

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("TAG", "-- "+msg);

            //tv_msg.setText(tv_msg.getText().toString() + content);

            try{if(content.equals("login succeeded")){
                webService.setUsernameAndPassword(userNameValue,passwordValue);
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                //登录成功和记住密码框为选中状态才保存用户信息
                if (remember_key.isChecked()) {
                    //记住用户名、密码、
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", userNameValue);
                    editor.putString("password", passwordValue);
                    editor.putBoolean("rem_isCheck", remember_key.isChecked());
                    editor.putBoolean("auto_isCheck", automatic_login.isChecked());
                    editor.commit();

                    Log.i("选中保存密码", "账号：" + userNameValue +
                            "\n" + "密码：" + passwordValue +
                            "\n" + "是否记住密码：" + remember_key.isChecked() +
                            "\n" + "是否自动登陆：" + automatic_login.isChecked());
                    editor.commit();
                }
                Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();
            }else if(content.equals("login failed")){
                ShowDialog("用户名或密码错误，请重新登录");
            }else if(content.equals("register succeeded")){
                ShowDialog("注册成功");
            }else if(content.equals("register failed")){
                ShowDialog("该用户名已被占用，请重新输入");
            }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    public void ShowDialog(String msg) {
        mDialog =  new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).show();
    }

    //绑定服务和广播接收器
    public void startAndBindService(){
        //绑定广播接收器
        receiver = new WebServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendContent");
        registerReceiver(receiver,intentFilter);


        //绑定服务
        conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //返回一个MsgService对象
                webService = ((WebService.WebBinder)service).getService();

            }
        };

        Intent serviceInent = new Intent(this,WebService.class);
        startService(serviceInent);

        //绑定Service
        Intent intent = new Intent(this,WebService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    class WebServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context,intent.getAction(),Toast.LENGTH_SHORT).show();
            content = intent.getStringExtra("content");
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    }
}
