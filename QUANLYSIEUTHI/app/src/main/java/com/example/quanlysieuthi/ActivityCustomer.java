package com.example.quanlysieuthi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;

public class ActivityCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer);

        switch (getIntent().getIntExtra("Fragment",0))
        {
            case 0:
                LOADFRAGMENT(new FragmentCustomer());
                break;
            case 1:
                LOADFRAGMENT(new FragmentReceipt());
                break;
        }
    }

    private void LOADFRAGMENT (Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_cus, fragment);
        transaction.commit();
    }
}