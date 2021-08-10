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
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterDelivery extends RecyclerView.Adapter<AdapterDelivery.VIEWHOLDER> {

    private Context context;
    private List<DELIVERY> deliveries;
    ItemClickListener itemClickListener;

    public AdapterDelivery(Context context, List<DELIVERY> deliveries, ItemClickListener itemClickListener) {
        this.context = context;
        this.deliveries = deliveries;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_gh,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        holder.name.setText(new StringBuilder().append(deliveries.get(position).getGh_des()));
        holder.cost.setText(new StringBuilder(String.format("%,.0f", Double.parseDouble(deliveries.get(position).getGh_phi()))).append(" đ"));
        holder.cb.setText(deliveries.get(position).getGh_name());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.deliveryClickItem(deliveries.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        TextView cb;
        TextView name, cost;
        RelativeLayout layout;
        Unbinder unbinder;
         public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
             unbinder = ButterKnife.bind(this,itemView);
            // mapping
             cb = itemView.findViewById(R.id.cb_gh);
             name = itemView.findViewById(R.id.gh_name);
             cost  = itemView.findViewById(R.id.gh_cost);
             layout = itemView.findViewById(R.id.gh_layout);
        }
    }

}
