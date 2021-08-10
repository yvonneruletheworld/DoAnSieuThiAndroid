package com.example.quanlysieuthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.icu.util.EthiopicCalendar;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class ActivityProducts extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView txt_tenLoai;
    ArrayList<PRODUCT> types;
    String typeID;
    NotificationBadge badge;
    FloatingActionButton floatBtn;
    AdapterProductsOfTypes adapter;
    GradientTextView title;
    //adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mapping ();
        general();
        init ();
    }

    private void general() {
        typeID = COMMON.type.Name;
        types = new ArrayList<PRODUCT>();
    }

    private void init() {
        title.setColor(title,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        txt_tenLoai.setText(typeID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        loadProductOfType();
    }

    private void mapping() {
        swipeRefreshLayout = findViewById(R.id.swpieRefresh);
        recyclerView = findViewById(R.id.recyclerView_prdtype);
        txt_tenLoai = findViewById(R.id.textView);
        badge = findViewById(R.id.cart_badge);
        floatBtn = findViewById(R.id.btn_cart);
        title = findViewById(R.id.textView);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadProductOfType();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private void loadProductOfType() {
            Query query = FirebaseDatabase.getInstance().getReference("Product")
                    .orderByChild("prd_loai").equalTo(typeID);
            query.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PRODUCT product = dataSnapshot.getValue(PRODUCT.class);
                    types.add(product);
                    if(types.size() == 0)
                        break;
                }
                adapter = new AdapterProductsOfTypes(ActivityProducts.this, types, floatBtn, badge);
                recyclerView.setAdapter(adapter);

            }
            if (types.size() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityProducts.this)
                        .setTitle("Thông báo")
                        .setMessage("Danh mục "+ typeID +" chưa thêm mặt hàng. Vui lòng xem mục khác")
                        .setPositiveButton("OK", (dialog2, which) ->
                        {
                            ActivityProducts.this.finish();
                            dialog2.dismiss();
                        }).create();
                alertDialog.show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}