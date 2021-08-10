package com.example.quanlysieuthi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterSearchView extends RecyclerView.Adapter<AdapterSearchView.VIEWHOLDER> implements Filterable {

    private Context context;
    private List<PRODUCT> search;
    List<PRODUCT> oldSearch ;
    public AdapterSearchView(Context context, List<PRODUCT> search) {
        this.context = context;
        this.oldSearch = search;
        this.search = search;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_cash, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
//
        Glide.with(context)
                .load(search.get(position).getPrd_hinh())
                .into(holder.cartView);
        holder.cart_name.setText(new StringBuilder().append(search.get(position).getPrd_ten()));
        holder.cart_quality.setTextSize(0);
        holder.cart_total.setTextSize(0);
        holder.cart_name.setTextSize(15);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ActivityDetails.class);
                i.putExtra("product",search.get(position));
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(holder.cartView,"image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context,pairs);
                context.startActivity(i, activityOptions.toBundle());
            }
        });
    }
    @Override
    public int getItemCount() {

            return search.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView cartView;
        TextView cart_name, cart_total, cart_quality;

        Unbinder unbinder;

        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
            unbinder = ButterKnife.bind(this, itemView);
            // mapping
            cartView = itemView.findViewById(R.id.c_img);
            cart_name = itemView.findViewById(R.id.c_name);
            cart_total  = itemView.findViewById(R.id.c_cost);
            cart_quality  = itemView.findViewById(R.id.c_quality);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty())
                {
                    search = oldSearch;
                }
                else
                {
//                    tao hai list de luc load lai k bi chong ket qua
                    List<PRODUCT> lst = new ArrayList<>();
                    for (PRODUCT prd : oldSearch)
                    {
                        if(prd.getPrd_ten().toLowerCase().contains(strSearch.toLowerCase().trim()))
                            lst.add(prd);
                    }
                    search = lst;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = search;

                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                search = (List<PRODUCT>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
