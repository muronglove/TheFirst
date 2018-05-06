package com.example.administrator.thefirst;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.thefirst.Classification.ClassificationActivity;
import com.example.administrator.thefirst.Service.WebService;
import com.example.administrator.thefirst.helper.AndroidShare;

public class UserActivity extends Activity {
    private SharedPreferences sp;
    private Button btn_loginout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btn_loginout = (Button) findViewById(R.id.btn_loginout);
        btn_loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除登录界面的自动登录和记住密码的代码
                sp.edit().putBoolean("automatic_login",false).commit();
                sp.edit().putBoolean("rem_isCheck",false).commit();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
        Button btn_invitation=(Button) findViewById(R.id.btn_invitation);
        btn_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    AndroidShare.sharedQQ(UserActivity.this);}catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        TextView username = (TextView)findViewById(R.id.activity_user_name);
        username.setText(WebService.username);
    }
}
