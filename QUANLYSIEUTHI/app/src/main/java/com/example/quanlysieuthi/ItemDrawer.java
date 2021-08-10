package com.example.quanlysieuthi;

import android.view.ViewGroup;

public abstract class ItemDrawer <T extends  AdapterDrawer.VIEWHOLDER> {
    protected boolean isChecked;

    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    public ItemDrawer<T> setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isSelectable() {
        return true;
    }
}
