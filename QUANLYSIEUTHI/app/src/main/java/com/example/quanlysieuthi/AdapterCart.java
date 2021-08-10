package com.example.quanlysieuthi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.VIEWHOLDER> {

    private Context context;
    private List<CART> carts;

    public AdapterCart(Context context, List<CART> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        Glide.with(context)
                .load(carts.get(position).getImg())
                .into(holder.cartView);
        holder.cart_name.setText(new StringBuilder().append(carts.get(position).getName()));
        holder.cart_total.setText(new StringBuilder(String.format("%,.0f", carts.get(position).getTotalPrice())).append(" đ"));
        holder.cart_cost.setText(new StringBuilder(String.format("%,.0f",Double.parseDouble(carts.get(position).getCost()))).append(" đ"));
        if(Float.parseFloat(carts.get(position).getPrice()) > Float.parseFloat(carts.get(position).getCost()))
        {
            holder.cart_pride.setText(carts.get(position).getPrice());
            holder.cart_pride.setPaintFlags(holder.cart_pride.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cart_pride.setVisibility(View.VISIBLE);
        }
        holder.btnmin.setOnClickListener(v -> {
            miniusCartItem (holder, carts.get(position));
        });
        holder.btnmax.setOnClickListener(v -> {
            plusCartItem (holder, carts.get(position));
        });
        holder.cart_quality.setText(new StringBuilder()
                .append(carts.get(position).getQuantity()));
    }

    private void plusCartItem(VIEWHOLDER holder, CART cart) {
        cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity()*
                    Float.parseFloat(cart.getCost()));
            //update
            holder.cart_quality.setText(new StringBuilder()
                    .append(cart.getQuantity()));
        holder.cart_total.setText(new StringBuilder(String.format("%,.0f", cart.getTotalPrice())).append(" đ"));
            updateFirebase(cart);
    }
    private void miniusCartItem(VIEWHOLDER holder, CART cart) {
        if(cart.getQuantity() > 1)
        {
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getQuantity()*
                    Float.parseFloat(cart.getCost()));
            //update
            holder.cart_quality.setText(new StringBuilder()
            .append(cart.getQuantity()));
            holder.cart_total.setText(new StringBuilder(String.format("%,.0f", cart.getTotalPrice())).append(" đ"));
            updateFirebase(cart);
        }
    }

    public void notifyCart (int pos)
    {
        notifyItemRemoved(pos);
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
        return carts.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView cartView, btnmin, btnmax;
        TextView cart_name, cart_cost, cart_total, cart_pride, cart_quality;

        Unbinder unbinder;
         public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
             unbinder = ButterKnife.bind(this,itemView);
            // mapping
            cartView = itemView.findViewById(R.id.c_img);
            cart_name = itemView.findViewById(R.id.c_name);
            cart_cost = itemView.findViewById(R.id.c_cost);
            cart_total  = itemView.findViewById(R.id.c_total);
            cart_pride = itemView.findViewById(R.id.c_pride);
            btnmin = itemView.findViewById(R.id.btnMinius);
            btnmax = itemView.findViewById(R.id.btnMax);
            cart_quality = itemView.findViewById(R.id.cart_quality);
        }
    }

}
