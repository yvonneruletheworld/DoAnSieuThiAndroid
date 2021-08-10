package com.example.quanlysieuthi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterRating extends RecyclerView.Adapter<AdapterRating.VIEWHOLDER> {

    private Context context;
    private List<RATING> ratings;

    public AdapterRating(Context context, List<RATING> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_rating,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        holder.rt_cmt.setText(ratings.get(position).getCmt());
        holder.rt_phone.setText(ratings.get(position).getPhone());
        holder.rt_rat.setRating(Float.parseFloat(ratings.get(position).rat));
        holder.rt_rat.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        TextView rt_phone;
        TextView rt_cmt;
        RatingBar rt_rat;
        Unbinder unbinder;
         public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
             unbinder = ButterKnife.bind(this,itemView);
            // mapping
             rt_phone = itemView.findViewById(R.id.rt_phone);
             rt_cmt = itemView.findViewById(R.id.rt_cmt);
             rt_rat  = itemView.findViewById(R.id.rt_rat);
        }
    }

}
