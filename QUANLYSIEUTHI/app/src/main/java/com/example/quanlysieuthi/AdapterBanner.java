package com.example.quanlysieuthi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterBanner extends RecyclerView.Adapter<AdapterBanner.MyViewHolder> {

    List<BANNER> listBanner;

    public AdapterBanner(List<BANNER> listBanner) {
        this.listBanner = listBanner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(holder.view).load(listBanner.get(position).bn_img).into(holder.view);
    }

    @Override
    public int getItemCount() {
        return listBanner.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.bn_img);
        }
    }
}
