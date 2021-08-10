 package com.example.quanlysieuthi;


import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDrawer extends RecyclerView.Adapter<AdapterDrawer.VIEWHOLDER> {

    private List<ItemDrawer> item;
    private Map<Class<? extends ItemDrawer>, Integer> viewType;
    private SparseArray<ItemDrawer> holderFactories;

    private OnItemSelectedListener listener;

    public AdapterDrawer(List<ItemDrawer> item) {
        this.item = item;
        this.viewType = new HashMap<>();
        this.holderFactories = new SparseArray<>();
        processView();
    }

    private void processView() {
        int type = 0;
        for (ItemDrawer i : item) {
            if (!viewType.containsKey(item.getClass())) {
                viewType.put(i.getClass(), type);
                holderFactories.put(type, i);
                type++;
            }
        }
    }

    public void setSelected(int position) {
        ItemDrawer newChecked = item.get(position);
        if (!newChecked.isSelectable()) {
            return;
        }

        for (int i = 0; i < item.size(); i++) {
            ItemDrawer im = item.get(i);
            if (im.isChecked()) {
                im.setChecked(false);
                notifyItemChanged(i);
                break;
            }
        }

        newChecked.setChecked(true);
        notifyItemChanged(position);

        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    public interface  OnItemSelectedListener {
        void  onItemSelected(int position);
    }
    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VIEWHOLDER holder = holderFactories.get(viewType).createViewHolder(parent);
        holder.adapterDrawer = this;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        item.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.get(item.get(position).getClass());
    }

    static  abstract  class  VIEWHOLDER extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private    AdapterDrawer adapterDrawer;
    public VIEWHOLDER(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        adapterDrawer.setSelected(getAdapterPosition());
    }
}

}
