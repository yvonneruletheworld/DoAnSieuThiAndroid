 package com.example.quanlysieuthi;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 public class AdapterProductsOfTypes extends RecyclerView.Adapter<AdapterProductsOfTypes.VIEWHOLDER> {

    private Context context;
    private List<PRODUCT> products;
    ICartLoadListener iCartLoadListener;
     private  CART cart ;
     FloatingActionButton btn;
     NotificationBadge badge;
     private  int count, pos;

    public AdapterProductsOfTypes(Context context, List<PRODUCT> products, FloatingActionButton btn, NotificationBadge badge) {
        this.context = context;
        this.products = products;
        iCartLoadListener = ActivityHomePage.cartLoadListener;
        this.btn = btn;
        this.badge = badge;
    }

    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product, parent, false);
        return new AdapterProductsOfTypes.VIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        pos = position;
        holder.cost.setText(new StringBuilder(String.format("%,.0f",Double.parseDouble(products.get(position).getPrd_dongia()))).append(" đ"));

        Glide.with(holder.imageView.getContext()).load(products.get(position).getPrd_hinh()).into(holder.imageView);
        holder.title.setText(cutTitle(products.get(position).getPrd_ten()));
        if(Float.parseFloat(products.get(position).getPrd_gia()) > Float.parseFloat(products.get(position).getPrd_gia()))
        {
            holder.pride.setText(new StringBuilder(String.format("%,.0f",Double.parseDouble(products.get(position).getPrd_gia()))).append(" đ"));
            holder.pride.setPaintFlags(holder.pride.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.pride.setVisibility(View.VISIBLE);
        }

        if(products.get(position).getPrd_tinhtrang().equals("Tạm Ngưng") || products.get(position).getPrd_tinhtrang().equals("Tạm ngưng"))
        {
            holder.btnAddToCart.setEnabled(false);
            holder.btnAddToCart.setText("Thông báo khi có hàng");
            holder.btnAddToCart.setTextSize(9);
            holder.btnAddToCart.setTextColor(context.getResources().getColor(R.color.black));
        }
        final PRODUCT product = products.get(position);
        holder.btnmin.setOnClickListener(v -> {
//            miniusCartItem (holder, carts.get(position));
            miniusCartItem (holder);
        });
        holder.btnmax.setOnClickListener(v -> {
//            plusCartItem (holder, carts.get(position));
            plusCartItem (holder);
        });
        holder.imageView.setOnClickListener(v -> {
            directToProductDetail(product, holder);
        }) ;
        holder.btnAddToCart.setOnClickListener(v -> {
            btnAddToCartClick(holder,product);
        });
    }

    private void btnAddToCartClick(AdapterProductsOfTypes.VIEWHOLDER holder, PRODUCT prd) {
        if(holder.addQualityLayout.getVisibility() == View.GONE){
            holder.addQualityLayout.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.quality.setText("1");
            addToCart(prd);

            new CircleAnimationUtil().attachActivity((Activity)context).setTargetView(holder.imageView)
                    .setMoveDuration(1000).setDestView(btn).setAnimationListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(holder.imageView.getVisibility() == View.INVISIBLE)
                        holder.imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).startAnimation();
        }
    }

    private void addToCart(@NotNull PRODUCT prd) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(COMMON.phone);
        if (userCart != null)
        {
            userCart.child(prd.getPrd_ten()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        // đã có mặt hàng trong giỏ hàng
                        cart = snapshot.getValue(CART.class);
                        cart.setQuantity(cart.getQuantity() + 1);
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("quantity", cart.getQuantity());
                        updateData.put("totalPrice", cart.getQuantity()*Float.parseFloat(cart.getCost()));

                        userCart.child(prd.getPrd_ten())
                                .updateChildren(updateData)
                                .addOnSuccessListener(aVoid -> {
                                    iCartLoadListener.onCartLoadFailed("Đã thêm vào giỏ hàng");
                                })
                                .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                    }
                    else // thêm một item mới vào giỏ hàng
                    {
                        cart =new CART();
                        String currentTime, currentDate;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
                        currentDate = dateFormat.format(calendar.getTime());

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                        currentTime = timeFormat.format(calendar.getTime());
//
                        cart.setName(prd.getPrd_ten());
                        cart.setImg(prd.getPrd_hinh());
                        cart.setPrice(prd.getPrd_dongia());
                        cart.setCost(prd.getPrd_gia());
                        cart.setQuantity(1);
                        cart.setDate(currentDate);
                        cart.setTime(currentTime);
                        cart.setTotalPrice(Float.parseFloat(prd.getPrd_gia()));

                        userCart.child(prd.getPrd_ten())
                                .setValue(cart)
                                .addOnSuccessListener(aVoid -> {
                                    iCartLoadListener.onCartLoadFailed("Đã thêm vào giỏ hàng");
                                })
                                .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                    }
                    EventBus.getDefault().postSticky(new UpdateCartEvent());
                    badge.setNumber((++count));
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    iCartLoadListener.onCartLoadFailed(error.getMessage());
                }
            });
        }
        else {
            CART cart =new CART();
            String currentTime, currentDate;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
            currentDate = dateFormat.format(calendar.getTime());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
            currentTime = timeFormat.format(calendar.getTime());
//
            cart.setName(prd.getPrd_ten());
            cart.setImg(prd.getPrd_hinh());
            cart.setPrice(prd.getPrd_dongia());
            cart.setCost(prd.getPrd_gia());
            cart.setQuantity(1);
            cart.setDate(currentDate);
            cart.setTime(currentTime);
            cart.setTotalPrice(Float.parseFloat(prd.getPrd_gia()));

            userCart.setValue(cart);
        }
    }

    private void directToProductDetail(PRODUCT product, VIEWHOLDER holder) {
//        Toast.makeText(context,product.getPrd_ten(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ActivityDetails.class);
        intent.putExtra("product",product);

        Pair[] paints = new Pair[1];
        paints[0] = new Pair<View, String>(holder.imageView,"image");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context,paints);
        context.startActivity(intent, activityOptions.toBundle());
    }

    private void plusCartItem(VIEWHOLDER holder) {
        holder.quality.setText(String.valueOf(Integer.parseInt(holder.quality.getText().toString()) + 1));
        cart.setQuantity(cart.getQuantity() + 1);
        cart.setTotalPrice(cart.getQuantity()*
                Float.parseFloat(cart.getCost()));
        //update
        updateFirebase(cart);
        badge.setNumber((++count));
    }


    private void miniusCartItem(AdapterProductsOfTypes.VIEWHOLDER holder) {
        if(Integer.parseInt(holder.quality.getText().toString()) >= 1)
        {
            if(Integer.parseInt(holder.quality.getText().toString()) == 1)
            {
                if(holder.btnAddToCart.getVisibility() == View.GONE)
                holder.btnAddToCart.setVisibility(View.VISIBLE);
                holder.addQualityLayout.setVisibility(View.GONE);
                deleteCart(cart);
                return;
            }
            holder.quality.setText(String.valueOf(Integer.parseInt(holder.quality.getText().toString()) - 1));
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getQuantity()*
                    Float.parseFloat(cart.getCost()));
            //update
             updateFirebase(cart);
             if(count > 0)
                 badge.setNumber((--count));
        }
    }

     private void deleteCart(CART cart) {
         FirebaseDatabase.getInstance()
                 .getReference("Cart")
                 .child(COMMON.phone)
                 .child(cart.getName())
                 .removeValue()
                 .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
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
        return products.size();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        ImageView imageView, btnmin, btnmax;
        TextView title, cost, quality, pride, btnAddToCart;
        LinearLayout addQualityLayout;

        public VIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_img);
            title = itemView.findViewById(R.id.product_title);
            cost = itemView.findViewById(R.id.product_cost);
            pride = itemView.findViewById(R.id.product_pride);
            quality = itemView.findViewById(R.id.product_quality);
            btnmin = itemView.findViewById(R.id.btnMinius);
            btnmax = itemView.findViewById(R.id.btnMax);
            btnAddToCart = itemView.findViewById(R.id.btn_addtocart);
            addQualityLayout = itemView.findViewById(R.id.quality_layout);
        }
    }

    private String cutTitle(String title) {
        String shortTitle = "";
        char[] array = title.toCharArray();
        for (int i = 0; i < array.length; i++) {
            shortTitle += array[i];
            if (i == 30) {
                shortTitle += "...";
                break;
            }
        }
        return shortTitle;
    }

}
