package com.example.administrator.thefirst;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
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
    private String imeinumber;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定服务和广播接收器
        startAndBindService();
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("userInfo", Context.MODE_MULTI_PROCESS);
        imeinumber = getIMEI(LoginActivity.this);
        Log.i("TAG",imeinumber);

        //对but进行实例化
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);
        remember_key = (CheckBox) findViewById(R.id.remember_pass);
        automatic_login = (CheckBox) findViewById(R.id.auto_login);

        //创建but的单击事件，参数要传匿名内部类
        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);




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
                webService.login(userNameValue,passwordValue,imeinumber);
            }
        });

        //注册按钮监听方法
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameValue = et1.getText().toString();
                passwordValue = et2.getText().toString();
                webService.register(userNameValue,passwordValue,imeinumber);
            }
        });
        requestPermissions();

    }


    public void requestPermissions() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            //doBackup();
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                //doBackup();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }


    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("此应用需要『存储』、『电话』、『相机』和『读取本机识别码』权限，请到 “应用管理 -> 收纳记录 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }









    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("TAG", "-- "+msg);

            //tv_msg.setText(tv_msg.getText().toString() + content);

            try{if(content.equals("login succeeded")){
                webService.setUsernameAndPassword(userNameValue,passwordValue,imeinumber);
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
                ShowDialog("该账号已被占用，请重新输入");
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


    public static String getIMEI(Context context) {
        try{
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            return imei;
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;


    }



}
