package com.example.quanlysieuthi;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderProduct extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    TextView title, cost;
    private ItemClickListener itemClickListener;

    public ViewHolderProduct(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_img);
        title = itemView.findViewById(R.id.product_title);
        cost = itemView.findViewById(R.id.product_cost);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false );
    }


}
