package com.example.quanlysieuthi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ActivityWelcome extends AppCompatActivity {

    //Variables
    Animation nameAnimation;
    TextView welcomeName, welcomeDetail;
    private static int SPLASH_SCREEN = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        init();
        directionToLogin();
    }

    private void init()
    {
        //Animations
        nameAnimation = AnimationUtils.loadAnimation(this,R.anim.name_animation);
//        imgAnimation = AnimationUtils.loadAnimation(this, R.anim.img_animation);

        //Hooks

//        welcomeImg = findViewById(R.id.image);
        welcomeName = findViewById(R.id.textView);
        welcomeDetail = findViewById(R.id.name_detail);

//        welcomeImg.setAnimation(imgAnimation);
        welcomeName.setAnimation(nameAnimation);
        welcomeDetail.setAnimation(nameAnimation);

    }

    private void directionToLogin()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivityWelcome.this, ActivityLogIn.class);
                intent.putExtra("Option",0);
                Pair [] pairs = new Pair[1];
                pairs[0] = new Pair<View, String> (welcomeName, "title_transition");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions activityOptions =  ActivityOptions.makeSceneTransitionAnimation(ActivityWelcome.this,pairs);
                    startActivity(intent, activityOptions.toBundle());
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }
}