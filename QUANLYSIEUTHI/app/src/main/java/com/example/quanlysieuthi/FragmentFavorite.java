package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentFavorite extends Fragment {

    GradientTextView title;
    RecyclerView rvc;
    AdapterFavorite adapterFavorite;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        mapping(v);
        hooking(v);
        return v ;
    }

    private void hooking(View v) {
        title.setColor(title,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));
        FirebaseDatabase.getInstance().getReference().child("Favorite").child(COMMON.phone)
                .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String value = snapshot.getValue(String.class);
                    String [] favs = value.split("/");
                    adapterFavorite = new AdapterFavorite(getContext(),favs);
                    rvc.setAdapter(adapterFavorite);
                    rvc.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void mapping(View v) {
        title = v.findViewById(R.id.txt_fav);
        rvc = v.findViewById(R.id.rv_fav);
    }
}