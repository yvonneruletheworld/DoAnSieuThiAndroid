package com.example.quanlysieuthi;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterCash extends RecyclerView.Adapter<AdapterCash.VIEWHOLDER> {

    private Context context;
    private List<CART> carts;
    private List<PRODUCT> product;
    private int type;

    public AdapterCash(Context context, List<CART> carts) {
        this.context = context;
        this.carts = carts;
        this.type = 0;
    }

    public AdapterCash(Context context, List<PRODUCT> product, int type) {
        this.context = context;
        this.product = product;
        this.type = type;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_cash,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        if(this.type == 0)
        {
            Glide.with(context)
                    .load(carts.get(position).getImg())
                    .into(holder.cartView);
            holder.cart_name.setText(new StringBuilder().append(carts.get(position).getName()));
            holder.cart_total.setText(new StringBuilder(String.format("%,.0f", carts.get(position).getTotalPrice())).append(" đ"));
            holder.cart_quality.setText("x " + carts.get(position).getQuantity());
        }
        else
        {
            Glide.with(context)
                    .load(product.get(position).getPrd_hinh())
                    .into(holder.cartView);
            holder.cart_name.setText(new StringBuilder().append(product.get(position).getPrd_ten()));
            holder.cart_quality.setText("x " + product.get(position).getPrd_soton());
        }
    }

    private  void updateFirebase (CART cart)
    {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(COMMON.phone)
                .child(cart.getName())
                .setValue(cart)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }
    @Override
    public int getItemCount() {
        return type == 0?  carts.size(): product.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView cartView;
        TextView cart_name, cart_total, cart_quality;

        Unbinder unbinder;
         public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
             unbinder = ButterKnife.bind(this,itemView);
            // mapping
            cartView = itemView.findViewById(R.id.c_img);
            cart_name = itemView.findViewById(R.id.c_name);
            cart_total  = itemView.findViewById(R.id.c_cost);
            cart_quality  = itemView.findViewById(R.id.c_quality);
        }
    }

}
