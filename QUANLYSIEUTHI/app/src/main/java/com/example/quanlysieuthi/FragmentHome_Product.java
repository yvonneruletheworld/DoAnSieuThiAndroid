package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentHome_Product extends Fragment {

    RecyclerView recyclerView;
    AdapterProducts adapterProducts;
    ShimmerFrameLayout layout;

    Handler handler=new Handler();

    ICartLoadListener iCartLoadListener;

    public FragmentHome_Product (ICartLoadListener listener)
    {
        this.iCartLoadListener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_home__product_, container, false);
        recyclerView = v.findViewById(R.id.recyclerview_home);
        layout=v.findViewById(R.id.shimmer);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.stopShimmer();
                layout.hideShimmer();
                layout.setVisibility(View.GONE);

            }
        },2000);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        loadProductFromDataBase();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterProducts.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterProducts.stopListening();
    }
    private void loadProductFromDataBase ()
    {
        FirebaseRecyclerOptions<PRODUCT> options =
                new FirebaseRecyclerOptions.Builder<PRODUCT>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product").limitToFirst(3), PRODUCT.class)
                        .build();
        adapterProducts = new AdapterProducts(options,iCartLoadListener);
        adapterProducts.context = getContext();
        recyclerView.setAdapter(adapterProducts);
    }
}