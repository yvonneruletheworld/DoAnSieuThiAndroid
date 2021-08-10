package com.example.quanlysieuthi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdapterProducts extends FirebaseRecyclerAdapter<PRODUCT,AdapterProducts.VIEWHOLDER> {

    Context context;
    ICartLoadListener iCartLoadListener;
    private  int [] backGrounds = {R.drawable.round_product_bg,R.drawable.round_product_bg2,R.drawable.round_product_bg1};
    public AdapterProducts(@NonNull FirebaseRecyclerOptions<PRODUCT> options, ICartLoadListener listener) {

        super(options);
        this.iCartLoadListener = listener;
    }
    public AdapterProducts(@NonNull FirebaseRecyclerOptions<PRODUCT> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VIEWHOLDER holder, int position, @NonNull PRODUCT model) {
        holder.cost.setText(new StringBuilder(String.format("%,.0f",Double.parseDouble(model.getPrd_dongia()))).append(" đ"));

        holder.size.setText(model.getPrd_dungtic());

        Glide.with(holder.imageView.getContext()).load(model.getPrd_hinh()).into(holder.imageView);
        holder.title.setText(cutTitle(model.getPrd_ten()));
        holder.background.setBackgroundResource(backGrounds[position]);

        final PRODUCT product = model;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,product.getPrd_ten(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ActivityDetails.class);
                intent.putExtra("product",product);

                Pair[] paints = new Pair[1];
                paints[0] = new Pair<View, String>(holder.imageView,"image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context,paints);
                context.startActivity(intent, activityOptions.toBundle());
            }
        });
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row_item,parent,false);
        return new VIEWHOLDER(view);
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView imageView, background;
        TextView title, cost, size;
        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_img);
            title = itemView.findViewById(R.id.product_title);
            cost = itemView.findViewById(R.id.product_cost);
            size = itemView.findViewById(R.id.product_quality);
            background = itemView.findViewById(R.id.prod_image);
        }
    }

    private String cutTitle (String title)
    {
        String shortTitle = "";
        char [] array = title.toCharArray();
        for(int i = 0; i < array.length; i++)
        {
            shortTitle += array[i];
            if(i == 30) {
                shortTitle += "...";
                break;
            }
        }
        return  shortTitle;
    }
}
