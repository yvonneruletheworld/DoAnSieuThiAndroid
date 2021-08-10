package com.example.quanlysieuthi;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterProductTypes extends BaseAdapter  {
    ArrayList<TYPE> Types;
    Context context;
    boolean isSearch;
//    List<PRODUCT> products, sub;
    List<PRODUCT>  sub;
    List<PRODUCT> newLst = new ArrayList<PRODUCT>();

//    private int [] backgroundColor = {R.color.yellow_bg, R.color.colorPrimary, R.color.peach_bg, R.color.peach_bg_1, R.color.blue_navy, R.color.purple_200, R.color.search_color, R.color.control};

    public AdapterProductTypes(ArrayList<TYPE> rooms, Context context, boolean isSearch) {
        this.Types = rooms;
        this.context = context;
        this.isSearch = isSearch;
    }
//    public AdapterProductTypes(List<PRODUCT> sub, Context context) {
////        this.products = rooms;
//        this.context = context;
//        this.isSearch = true;
////        sub =products.subList(products.size() - 5, products.size() -1);
//        this.sub = sub;
//    }

    @Override
    public int getCount() {
        if(isSearch)
            return sub.size();
        return Types.size();
    }

    @Override
    public Object getItem(int position) {
        if(isSearch)
            return sub.get(position);
        return Types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_product_type, null);

            ImageView imageView = convertView.findViewById(R.id.product_type_img);
            TextView name = convertView.findViewById(R.id.product_type_name);
            if(isSearch == false)
            {
            Glide.with(context).load(Types.get(position).getHinh()).placeholder(R.drawable.icons8_customer_32_2).into(imageView);
            name.setText(Types.get(position).getName());


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        COMMON.type = Types.get(position);
                        context.startActivity(new Intent(context,ActivityProducts.class));
//                    .putExtra("Type", Types.get(position).Name)
                    }
                });
            }
            else
            {
//
//                Glide.with(context).load(sub.get(position).getPrd_hinh()).placeholder(R.drawable.icons8_customer_32_2).into(imageView);
//                name.setText(sub.get(position).getPrd_ten());
//
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(context,ActivityDetails.class);
//                        i.putExtra("product",sub.get(position));
//                        Pair[] pairs = new Pair[1];
//                        pairs[0] = new Pair<View,String>(imageView,"image");
//                        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context,pairs);
//                        context.startActivity(i, activityOptions.toBundle());
//                    }
//                });
            }

        }

        return convertView;
    }

}
