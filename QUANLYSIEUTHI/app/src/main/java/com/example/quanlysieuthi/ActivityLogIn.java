package com.example.quanlysieuthi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class ActivityLogIn extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        intent = getIntent();
        int option = intent.getIntExtra("Option", 0);

        getIntenExtraOption(option);
        // mapping

    }

    public void getIntenExtraOption (int opt)
    {
        switch (opt)
        {
            case 0:
                LOADFRAGMENT(new FragmentLogin());
//                finish();
                break;
            case 1:
                LOADFRAGMENT(new FragmentRegister());
//                finish();
                break;
            case 2:
                break;
        }
    }
    public  void LOADFRAGMENT (Fragment currentFragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, currentFragment);
        transaction.commit();
    }

}