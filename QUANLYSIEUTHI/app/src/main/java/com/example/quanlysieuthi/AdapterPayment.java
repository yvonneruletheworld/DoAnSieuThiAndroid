package com.example.quanlysieuthi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.VIEWHOLDER> {

    private Context context;
    private List<PAYMENT> payments;
    private ItemClickListener itemClickListener;
    private int row_index;

    public AdapterPayment(Context context, List<PAYMENT> payments, ItemClickListener itemClickListener) {
        this.context = context;
        this.payments = payments;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        holder.name.setText(new StringBuilder().append(payments.get(position).getPm_name()));
        holder.des.setText(new StringBuilder().append(payments.get(position).getPm_des()));
        holder.link.setText(new StringBuilder(payments.get(position).getPm_name()));
        Glide.with(holder.logo).load(payments.get(position).pm_img).into(holder.logo);
        holder.pm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.paymentClickItem(payments.get(position));
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.back_gd.setBackgroundResource(R.drawable.banner_background);
            holder.des.setBackgroundResource(R.drawable.edittext_curve_bg);
            holder.link.setTextColor(context.getResources().getColor(R.color.white));
            holder.name.setTextColor(context.getResources().getColor(R.color.white));
            holder.des.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.back_gd.setBackgroundResource(R.drawable.radius_border_layout);
            holder.des.setBackgroundResource(R.drawable.banner_background);
            holder.link.setTextColor(context.getResources().getColor(R.color.black));
            holder.name.setTextColor(context.getResources().getColor(R.color.black));
            holder.des.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView name, des, link;
        RelativeLayout pm_layout;
        View back_gd;

        Unbinder unbinder;

        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
            unbinder = ButterKnife.bind(this, itemView);
            // mapping
            logo = itemView.findViewById(R.id.pm_img);
            name = itemView.findViewById(R.id.pm_name);
            des = itemView.findViewById(R.id.pm_des);
            link = itemView.findViewById(R.id.pm_lk);
            pm_layout = itemView.findViewById(R.id.pm_layout);
            back_gd = itemView.findViewById(R.id.rectangle_9);
        }
    }

}
