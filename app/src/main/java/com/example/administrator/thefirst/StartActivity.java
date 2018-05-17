package com.example.administrator.thefirst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.administrator.thefirst.UIController.Titanic;
import com.example.administrator.thefirst.UIController.TitanicTextView;
import com.example.administrator.thefirst.UIController.Typefaces;

public class StartActivity extends Activity {
    private ImageView iv_start = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        //水漫效果
//        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);
//
//        // set fancy typeface
//        tv.setTypeface(Typefaces.get(StartActivity.this, "Satisfy-Regular.ttf"));
//
//        // start animation
//        new Titanic().start(tv);

        initImage();
    }

    private void initImage() {
        iv_start = (ImageView) findViewById(R.id.iv_start);
        iv_start.setImageResource(R.drawable.start);
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.24f, 1.0f, 1.24f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2400);
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //可以在这里先进行某些操作

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnimation);
    }

    private void startActivity() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}
