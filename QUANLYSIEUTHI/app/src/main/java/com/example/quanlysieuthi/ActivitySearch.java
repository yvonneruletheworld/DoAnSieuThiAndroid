package com.example.quanlysieuthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivitySearch extends AppCompatActivity {

    SearchView txt_search;
    Animation animation;
    RecyclerView lvSearch, lvRecent;
    AdapterSearchView adapterProductSearchs;
    AdapterSearch adapterRecentSearchs;
    ArrayList<PRODUCT> products;
    SharedPreferences sharedPreferences;
    String searchHistory;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        mapping();
        hooking();
    }


    private void hooking() {
        animation = AnimationUtils.loadAnimation
                (this,R.anim.name_animation);
        txt_search.setAnimation(animation);
        txt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if(query!=null && query.length()!=0){
//                    ArrayList<PRODUCT> filteredData = FilterNews(query);
////                    Toast.makeText(ActivitySearch.this," " + String.valueOf( filteredData.size()) + " Result", Toast.LENGTH_SHORT).show();
////                    searchfragment = new Fragment_Search(filteredData);
////                    LoadFragment(searchfragment);
//                    lvSearch.setAdapter(new AdapterSearchView(ActivitySearch.this,filteredData));
//                    return true;
//                }
//                else
//                    return false;
                adapterProductSearchs.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterProductSearchs.getFilter().filter(newText);
                return false;
            }
        });
        generalSearchResult();
        generalSearchRecent();
    }

    private void generalSearchResult() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    products = new ArrayList<>();
                    for(DataSnapshot item : snapshot.getChildren())
                    {
                        PRODUCT prd = item.getValue(PRODUCT.class);
                        products.add(prd);
                        if(products.size() == 0)
                            break;
                    }
//                    List<PRODUCT> newProducts =products.subList(products.size() - 5, products.size() -1);
                    adapterProductSearchs = new AdapterSearchView(ActivitySearch.this,products);
                    lvSearch.setAdapter(adapterProductSearchs);
                    lvSearch.setLayoutManager(new LinearLayoutManager(ActivitySearch.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void generalSearchRecent() {
        sharedPreferences = this.getSharedPreferences("searchHistory",MODE_PRIVATE);
        boolean isSave = sharedPreferences.getBoolean("isSave",false);
        if(isSave)
        {
            searchHistory = sharedPreferences.getString("searchKey","Không có tìm kiếm gần đây");
        }
        if(searchHistory != null)
        {
            String [] searchKeys = searchHistory.split(",");
            lvRecent.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            adapterRecentSearchs = new AdapterSearch(this,searchKeys);
            lvRecent.setAdapter(adapterRecentSearchs);
        }
    }

    private void mapping() {
        txt_search = findViewById(R.id.txt_search);
        lvSearch = findViewById(R.id.lv_search);
        lvRecent = findViewById(R.id.lv_recentSearch);
    }

    private ArrayList<PRODUCT> FilterNews(String query)
    {
        ArrayList<PRODUCT> filterPrd = new ArrayList<>();
        if(products != null)
        {
            for(PRODUCT prd : products)
                filterPrd.add(prd);
        }
        return filterPrd;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        txt_search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        String s = txt_search.getQuery().toString();
        if(s.trim().isEmpty() || s.trim().toLowerCase().length() < 1)
        {
        }
        else
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("searchKey",searchHistory+ "," + s.trim());
            editor.putBoolean("isSave", true);
            editor.commit();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SearchKey");
            if(db != null)
            {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot item : snapshot.getChildren())
                            {
                                SEARCHKEY sk = item.getValue(SEARCHKEY.class);
                                if(sk.srch_key.contains(s.trim()))
                                {
                                    double rat = sk.srch_rat + 1;
                                    FirebaseDatabase.getInstance().getReference().child("SearchKey")
                                            .child(item.getKey()).child("srch_rat").setValue(rat);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else
            {
                Random r = new Random(2);
                SEARCHKEY sk = new SEARCHKEY ();
                sk.srch_key = s;
                sk.srch_rat = 1;
                FirebaseDatabase.getInstance().getReference().child("SearchKey").child(r.toString()).push().setValue(sk);
            }
        }

        super.onBackPressed();
    }
}