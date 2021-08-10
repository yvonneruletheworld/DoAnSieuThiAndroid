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

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.VIEWHOLDER> {

    private Context context;
    private String[] favs;
    List<CONTACT> contacts;
    PhoneItemClickListener phoneItemClickListener;
    int [] colors = {R.color.purple_200,
            R.color.purple_500,
            R.color.purple_700,
            R.color.teal_200,
            R.color.teal_700,
            R.color.peach_bg_1,
            R.color.colorAccent};
    int i  = 0;
    public AdapterFavorite(Context context, String[] favs) {
        this.context = context;
        this.favs = favs;
    }
    public AdapterFavorite(Context context, List<CONTACT> contacts, PhoneItemClickListener phoneItemClickListener) {
            this.context = context;
            this.contacts = contacts;
            this.phoneItemClickListener = phoneItemClickListener;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(favs != null)
        {
            return new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.item_chinhanh, parent, false)));
        }
        else
            return  new VIEWHOLDER((LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        if(favs != null)
        {
            String s = favs[position];
            if(s != null && s!= "")
            {
                holder.textView.setText(s);
            }
        }
        else
        {
            if(i > 6)
            {
                i = 0;
                holder.fn.setBackgroundColor(context.getResources().getColor(colors[i]));
            }
            else
            {
                holder.fn.setBackgroundColor(context.getResources().getColor(colors[i]));
            }
            i++;

            CONTACT ct = contacts.get(position);

            holder.fn.setText(ct.getFn());
            holder.fullname.setText(ct.getFullName());
            holder.phone.setText(ct.getPhone());
//            holder.textView.setText(contacts.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String [] phone = holder.textView.getText().toString().split("-");
                    phoneItemClickListener.onContactsItemClick(phone[1]);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(favs != null)
        return favs.length;
        else
            return  contacts.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {

        TextView textView;
        TextView fn, fullname, phone;
        Unbinder unbinder;

        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            //binder
            unbinder = ButterKnife.bind(this, itemView);
            // mapping
            if(favs != null)
            {
                textView = itemView.findViewById(R.id.location);
            }
            else
            {
                fn = itemView.findViewById(R.id.txt_name);
                fullname = itemView.findViewById(R.id.txt_fullname);
                phone = itemView.findViewById(R.id.txt_type);
            }
        }
    }

}
