package com.example.quanlysieuthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityCategory extends AppCompatActivity {

    GradientTextView txt_sanpham, txt_danhmuc;
    GridView gridView;
    AdapterProductTypes adapterProductTypes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<TYPE> types;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);
        mapping();
        init();
    }

    private void init() {
        txt_danhmuc.setColor(txt_danhmuc,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));
        txt_sanpham.setColor(txt_sanpham,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));
        loadProductFromDataBase();

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation);
        gridView.setAdapter(adapterProductTypes);
        gridView.setAnimation(anim);
        gridView.setNumColumns(2);
    }

    private void mapping() {
        txt_sanpham = findViewById(R.id.txt_sanpham);
        txt_danhmuc = findViewById(R.id.txt_danhmuc);
        gridView = findViewById(R.id.gridLayout);
    }
    private void loadProductFromDataBase ()
    {
        types = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ProductType");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                types.clear();

                for(DataSnapshot child : snapshot.getChildren())
                {
                    TYPE type = child.getValue(TYPE.class);
                    types.add(type);
                }

                adapterProductTypes = new AdapterProductTypes(types, ActivityCategory.this, false);
                gridView.setAdapter(adapterProductTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}