package com.example.quanlysieuthi;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentShop extends Fragment {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    //main functions
    ImageView btn_fav, btn_notification, btn_category;
    ViewPager2 slideViewPager;
    AdapterBanner adapterBanner;
    LinearLayout dotLayout;
    TextView[] dots;
    EditText txt_search;
    ConstraintLayout btn_chat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        mapping(v);
        hooks();
        return v;
    }

    private void mapping (View v) {
        spinner = v.findViewById(R.id.spinner1);
        adapter  = ArrayAdapter.createFromResource(getContext(),
                R.array.chinhanh, R.layout.item_chinhanh);
        btn_fav = v.findViewById(R.id.btn_search);
        btn_category = v.findViewById(R.id.btn_cate);
        btn_notification = v.findViewById(R.id.imageView3);
        slideViewPager = v.findViewById(R.id.baner_slide);
        dotLayout = v.findViewById(R.id.sh_dots_container);
        txt_search = v.findViewById(R.id.editTextTextPersonName);
        btn_chat = v.findViewById(R.id.btn_chat);
    }

    private void  hooks () {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        btn_fav.setOnClickListener(onClickListener);
        btn_category.setOnClickListener(onClickListener);
        btn_notification.setOnClickListener(onClickListener);
        txt_search.setOnClickListener(onClickListener);
        btn_chat.setOnClickListener(onClickListener);

        // banner
        List<BANNER> banners = new ArrayList<>();
        loadBanner(banners);
        slideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedIndicator(position);
                super.onPageSelected(position);
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

    private void dotsIndicator() {
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(18);
            dotLayout.addView(dots[i]);
        }
    }
    private void loadBanner(List<BANNER> banners) {
        FirebaseDatabase.getInstance().getReference("Banner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot item : snapshot.getChildren())
                            {
                                BANNER b = item.getValue(BANNER.class);
                                banners.add(b);
                            }
                            adapterBanner = new AdapterBanner(banners);
                            slideViewPager.setAdapter(adapterBanner);

                            dots = new TextView[banners.size()];
                            dotsIndicator();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_chat:
                    String phone = "+8462728880";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                    break;
                case R.id.btn_search:
                    LOADFRAGMENT(new FragmentFavorite());
                    break;
                case R.id.btn_cate:
                    Toast.makeText(getContext(), "Cate", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),ActivityCategory.class));
                    break;
                case R.id.imageView3:
                    Toast.makeText(getContext(), "Noti", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.editTextTextPersonName:
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String> (txt_search,"title_transition");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),pairs);
                        startActivity(new Intent(getActivity(),ActivitySearch.class),activityOptions.toBundle());
                    }
                    break;
            }
        }
    };

    private void LOADFRAGMENT(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main,fragment);
        transaction.commit();
    }
}