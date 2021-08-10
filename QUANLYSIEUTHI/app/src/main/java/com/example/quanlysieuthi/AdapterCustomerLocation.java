package com.example.quanlysieuthi;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterCustomerLocation extends BaseAdapter{
    ArrayList<String> locations;
    Context context;

//    private int [] backgroundColor = {R.color.yellow_bg, R.color.colorPrimary, R.color.peach_bg, R.color.peach_bg_1, R.color.blue_navy, R.color.purple_200, R.color.search_color, R.color.control};

    public AdapterCustomerLocation(ArrayList<String> locations, Context context) {
        this.locations = locations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chinhanh, null);

            TextView name = convertView.findViewById(R.id.location);
            name.setText(locations.get(position));
        }

        return convertView;
    }

    public void update(ArrayList<String> locations)
    {
        this.locations = locations;
        notifyDataSetChanged();
    }
}
