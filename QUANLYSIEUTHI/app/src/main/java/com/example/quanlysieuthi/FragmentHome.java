package com.example.quanlysieuthi;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    ImageView user_ava, btn_search;
    TextView tab_sp, tab_camnang, username;
    RecyclerView cat_recycler;
    FrameLayout product_frame;
    int isCheck;

    ICartLoadListener iCartLoadListener;
    AdapterSearch adapterSearch;


    public  FragmentHome (ICartLoadListener listener)
    {
        this.iCartLoadListener = listener;
    }
    public FragmentHome(){

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mapping(v);
        initPage();

        return v;
    }

    private void initPage() {
        LOADFRAGMENT(new FragmentHome_Product(iCartLoadListener));
        isCheck = tab_sp.getId();

        tab_sp.setOnTouchListener(tabClick);
        tab_camnang.setOnTouchListener(tabClick);
        String[] longUserName = COMMON.currentUser.getUserName().split(" ");
        username.setText("Xin chào, "+longUserName[longUserName.length - 1]+"!");
        Glide.with(user_ava).load(COMMON.currentUser.getHinh()).into(user_ava);
        loadHighlySearchKey();
    }

    private void loadHighlySearchKey() {
        List<SEARCHKEY> searchkeyList = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("SearchKey")
                .orderByChild("srch_rat").limitToLast(6)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot item : snapshot.getChildren())
                            {
                                SEARCHKEY searchkey = item.getValue(SEARCHKEY.class);
                                searchkeyList.add(searchkey);
                            }
                            adapterSearch = new AdapterSearch(getContext(), searchkeyList);
                            cat_recycler.setAdapter(adapterSearch);
                            cat_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void mapping (View view)
    {
        user_ava = view.findViewById(R.id.imageView3);
        btn_search = view.findViewById(R.id.imageView2);
        tab_sp = view.findViewById(R.id.textView2);
        tab_camnang = view.findViewById(R.id.textView);
        cat_recycler = view.findViewById(R.id.cat_recycler);
        product_frame = view.findViewById(R.id.product_frame);
        username = view.findViewById(R.id.txt_username);


        tab_sp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getContext(),"ok", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public  void LOADFRAGMENT (Fragment currentFragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.product_frame, currentFragment);
        transaction.commit();
    }

    View.OnTouchListener tabClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            isCheck = v.getId();

            tab_camnang.setTextColor(getResources().getColor(R.color.grey));
            tab_sp.setTextColor(getResources().getColor(R.color.grey));
            tab_sp.setTypeface(null, Typeface.BOLD);
            tab_camnang.setTypeface(null, Typeface.BOLD);

            switch (isCheck)
            {
                case  R.id.textView2:
                    tab_sp.setTextColor(getResources().getColor(R.color.yellow_bg));
                    tab_sp.setTypeface(null, Typeface.ITALIC);
                    return false;
                case  R.id.textView:
                    tab_camnang.setTextColor(getResources().getColor(R.color.yellow_bg));
                    tab_camnang.setTypeface(null, Typeface.ITALIC);
                    return false;
            }
            return false;
        }
    };

//    View.OnTouchListener hideKeybroad = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            search.onEditorAction(EditorInfo.IME_ACTION_DONE);
//            return true;
//        }
//    };
}