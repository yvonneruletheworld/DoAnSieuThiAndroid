package com.example.quanlysieuthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImgActivity extends AppCompatActivity {

//    RecyclerView recyclerView;
//    AdapterProducts adapterProducts;
    FrameLayout frameLayout;
//    ArrayList<PRODUCT> products = new ArrayList<>();
//    AdapterProduct adapterProduct;

//    FirebaseDatabase firebaseDatabase;
//DatabaseReference reference;
//    StorageReference storageRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_img);

//        recyclerView = findViewById(R.id.temp_rcl);
//        firebaseDatabase =  FirebaseDatabase.getInstance();
//        reference  = firebaseDatabase.getReference("Product");
            frameLayout = findViewById(R.id.fl);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        loadProductFromDataBase();
//        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl, new FragmentCart());
//        transaction.commit();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapterProducts.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapterProducts.stopListening();
//    }
//    private void loadProductFromDataBase ()
//    {
//        FirebaseRecyclerOptions<PRODUCT> options =
//                new FirebaseRecyclerOptions.Builder<PRODUCT>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), PRODUCT.class)
//                        .build();
//        adapterProducts = new AdapterProducts(options);
//        recyclerView.setAdapter(adapterProducts);
//        FirebaseRecyclerOptions<PRODUCT> options =
//                new FirebaseRecyclerOptions.Builder<PRODUCT>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), PRODUCT.class)
//                        .build();
//        FirebaseRecyclerAdapter<PRODUCT,ViewHolderProduct> adapter = new FirebaseRecyclerAdapter<PRODUCT, ViewHolderProduct>(options){
//            @Override
//            protected void onBindViewHolder(@NonNull ViewHolderProduct holder, int position, @NonNull PRODUCT model) {
//                holder.title.setText(model.getPrd_ten());
//                holder.cost.setText(model.getPrd_gia().toString());
//                Picasso.get().load(model.getPrd_hinh()).fit().centerCrop().into(holder.imageView);
//
//                final PRODUCT clickItem = model;
//                holder.title.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(ImgActivity.this,""+clickItem.getPrd_ten().toString(),Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.item_home_product, parent, false);
//
//                return new ViewHolderProduct(view);
//            }
//        };
//        recyclerView.setAdapter(adapter);
//    }


//    private void getImg  (String filename)
//    {
//        storageRef = FirebaseStorage.getInstance().getReference("/product_img/"+filename);
//    }
}