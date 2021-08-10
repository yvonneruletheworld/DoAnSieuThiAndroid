package com.example.quanlysieuthi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterDiscount extends RecyclerView.Adapter<AdapterDiscount.VIEWHOLDER> {

    private Context context;
    private List<DISCOUNT> discounts;
    double total;
    String location;
    ItemClickListener itemClickListener;

    public AdapterDiscount(Context context, List<DISCOUNT> discounts, double total, String location, ItemClickListener itemClickListener) {
        this.context = context;
        this.discounts = discounts;
        this.total = total;
        this.location = location;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_discount,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        Glide.with(context)
                .load(discounts.get(position).getDis_img())
                .into(holder.disView);
        holder.dis_name.setText(new StringBuilder().append(discounts.get(position).getDis_code()));
        holder.dis_des.setText(new StringBuilder().append(discounts.get(position).getDis_describe()));
        holder.exp.setText(new StringBuilder().append(discounts.get(position).getDis_exp()));

        //check required
        if(android.text.TextUtils.isDigitsOnly(discounts.get(position).getDis_required()))
        {
            int required = Integer.parseInt(discounts.get(position).getDis_required());
            if(required <=8 && required >= 2)
            {
                //cac thu trong ngay
                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                int curDay = cal.get(Calendar.DAY_OF_WEEK);
                if(required != curDay)
                {
//                    holder.get_dis.setBackgroundResource(R.color.grey_med);
                    holder.get_dis.setEnabled(false);
                    holder.dis_value.setText(new StringBuilder(discounts.get(position).getDis_value()).append(" %"));
                }
            }
            else if(this.total < required)
            {
//                holder.get_dis.setBackgroundResource(R.color.grey_med);
                holder.get_dis.setEnabled(false);
                holder.dis_value.setText(new StringBuilder(String.format("%,.0f", Double.parseDouble(discounts.get(position).getDis_value()))).append(" đ"));
            }
        }
        else {
            if(!discounts.get(position).getDis_required().equals(location))
            {
//                holder.get_dis.setBackgroundResource(R.color.grey_med);
                holder.get_dis.setEnabled(false);
                holder.dis_value.setText("Freeship Nội thành Hồ Chí Minh");
            }
        }

        if(holder.get_dis.isEnabled())
        {
            holder.get_dis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    itemClickListener.discountClickItem(discounts.get(position), discounts.get(position).getDis_type());
                    v.setEnabled(false);
                }
            });
        }
    }

    private  void updateFirebase (CART cart)
    {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(COMMON.phone)
                .child(cart.getName())
                .setValue(cart)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }
    @Override
    public int getItemCount() {
        return discounts.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView disView;
        TextView dis_name, dis_value, dis_des, exp;
        Button get_dis;

        Unbinder unbinder;
         public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
             unbinder = ButterKnife.bind(this,itemView);
            // mapping
             disView = itemView.findViewById(R.id.c_img);
             dis_name = itemView.findViewById(R.id.c_name);
             dis_value  = itemView.findViewById(R.id.c_total);
             dis_des  = itemView.findViewById(R.id.c_cost);
             get_dis  = itemView.findViewById(R.id.get_dis);
             exp  = itemView.findViewById(R.id.c_date);
        }
    }

}
