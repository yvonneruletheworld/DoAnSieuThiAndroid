package com.example.quanlysieuthi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.VIEWHOLDER> {

    private Context context;
    private List<SEARCHKEY> search;
    String[] history;
    boolean isHistory = false;
    public AdapterSearch(Context context, List<SEARCHKEY> search) {
        this.context = context;
        this.search = search;
    }
    public AdapterSearch(Context context, String[] history) {
        this.context = context;
        this.history = history;
        isHistory = true;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
//
        if(isHistory == true)
        {
            if(history[position].trim() != "" &&history[position] != null &&history[position] != "null" &&history[position] != "Kem" )
                holder.catname.setText(new StringBuilder().append(history[position]));
        }
        else
            holder.catname.setText(new StringBuilder().append(search.get(position).srch_key));
         }

    @Override
    public int getItemCount() {
        if(isHistory == true)
            return history.length;
        else
            return search.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        TextView catname;

        Unbinder unbinder;

        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
            unbinder = ButterKnife.bind(this, itemView);
            // mapping
            catname = itemView.findViewById(R.id.cat_name);
        }
    }

}
