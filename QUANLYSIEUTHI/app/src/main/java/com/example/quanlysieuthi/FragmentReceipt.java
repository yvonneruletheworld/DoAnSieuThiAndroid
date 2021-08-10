package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentReceipt extends Fragment {

    TextView[] dots;
    ViewPager2 viewPagerSlide;
    LinearLayout dotLayout;
    AdapterReceipt adapterReceipt;
    ArrayList<ORDER> orders;
    ArrayList<String> orderIDs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.a, container, false);


        mapping (v);
        init();
        reInit();
        return v;
    }

    private void reInit() {
        viewPagerSlide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedIndicator(position);
                super.onPageSelected(position);
            }
        });
    }

    private void dotsIndicator() {
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(18);
            dotLayout.addView(dots[i]);
        }
    }

    private void init() {
        orders = new ArrayList<>();
        orderIDs = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                ORDER order = item.getValue(ORDER.class);
                                if (order.getOrd_phone().equals(COMMON.phone)){
                                    orders.add(order);
                                    orderIDs.add(item.getKey());
                                }
                            }
                            adapterReceipt = new AdapterReceipt(getContext(), orders, orderIDs);
                            viewPagerSlide.setAdapter(adapterReceipt);

                            dots = new TextView[orders.size()];
                            dotsIndicator();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void selectedIndicator(int position) {
        for(int i = 0; i< dots.length; i ++)
        {
            if(i == position)
                dots[i].setTextColor(getResources().getColor(R.color.yellow_bg));
            else
                dots[i].setTextColor(getResources().getColor(R.color.grey_med));
        }
    }

    private void mapping(View v) {
        viewPagerSlide = v.findViewById(R.id.image_container);
        dotLayout = v.findViewById(R.id.dots_container);
    }


}