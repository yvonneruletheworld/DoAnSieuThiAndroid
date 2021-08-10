package com.example.quanlysieuthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ActivityDetails extends AppCompatActivity {

    ImageView  btn_like, prd_img, btn_min, btn_max,btn_like1;
    Button btn_addtocart;
    PRODUCT product;
    ConstraintLayout main_layout;
    TextView  txt_tonkho,txt_ratting, txt_dongia, txt_ten, txt_mota, txt_sl, txt_dongiachuagiam, txt_tag1, txt_tag2;
    ICartLoadListener iCartLoadListener;
    String [] favs;
    String value;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details);
        mapping();
        init();
    }

    private void mapping ()
    {
        btn_like = findViewById(R.id.btn_like);
        btn_like1 = findViewById(R.id.btn_like1);
        prd_img = findViewById(R.id.dt_product_img);
        btn_min = findViewById(R.id.btn_minius);
        btn_max = findViewById(R.id.btn_plus);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        txt_dongia = findViewById(R.id.txt_gia);
        txt_tonkho = findViewById(R.id.txt_tonkho);
        txt_dongiachuagiam = findViewById(R.id.txt_giachuagiam);
        txt_ten = findViewById(R.id.txt_tenhang);
        txt_mota = findViewById(R.id.txt_congdung);
        txt_sl = findViewById(R.id.txt_sl);
        txt_tag1 = findViewById(R.id.txt_tag1);
        txt_tag2 = findViewById(R.id.txt_tag2);
        main_layout = findViewById(R.id.main_layout);
        ratingBar = findViewById(R.id.rattingBar);
        txt_ratting = findViewById(R.id.txt_ratting);
    }

    private void init()
    {
        product = (PRODUCT) getIntent().getSerializableExtra("product");
        if(product != null)
        {
            Glide.with(this).load(product.getPrd_hinh()).into(prd_img);
            txt_ten.setText(product.getPrd_ten());
            txt_dongia.setText(product.getPrd_dongia());
            if(Float.parseFloat(product.getPrd_dongia()) > Float.parseFloat(product.getPrd_gia()))
            {
                txt_dongiachuagiam.setPaintFlags(txt_dongiachuagiam.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txt_dongiachuagiam.setVisibility(View.VISIBLE);

            }
            txt_mota.setText(product.getPrd_congdung()+product.getPrd_dungtic());
            txt_tag1.setText(product.getPrd_nhanhang());
            txt_tag2.setText(product.getPrd_xuatxu());
            txt_tonkho.setText(product.getPrd_soton());
            if(product.getPrd_tinhtrang().equals("Tạm Ngưng") || product.getPrd_tinhtrang().equals("Tạm ngưng") )
            {
                btn_addtocart.setEnabled(false);
                btn_addtocart.setText("Thông báo khi có hàng");
                btn_addtocart.setTextSize(9);
                btn_addtocart.setTextColor(getResources().getColor(R.color.black));
            }
            else
            {
                btn_addtocart.setEnabled(true);
                btn_addtocart.setText("Thêm vào giỏ hàng");
                btn_addtocart.setTextSize(10);
                btn_addtocart.setTextColor(getResources().getColor(R.color.white));
            }
        }

        //cart
        iCartLoadListener = ActivityHomePage.cartLoadListener;
        btn_addtocart.setOnClickListener(onClickListener);
        btn_max.setOnClickListener(onClickListener);
        btn_min.setOnClickListener(onClickListener);
        btn_like.setOnClickListener(onClickListener);
        btn_like1.setOnClickListener(onClickListener);
        txt_ratting.setOnClickListener(onClickListener);
        ratingBar.setOnRatingBarChangeListener(onRatingBarChangeListener);

        themYeuThich();
    }

    RatingBar.OnRatingBarChangeListener onRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ProgressDialog dialog = new ProgressDialog(ActivityDetails.this);
            dialog.setMessage("Đợi trong giây lát...");
            dialog.show();
            loadBottomSheet(dialog);
        }
    };

    private void loadBottomSheet(ProgressDialog dialog) {
        BottomSheetDialog bottomSheetDialog =
                new BottomSheetDialog(ActivityDetails.this,R.style.BottomSheetDialogTheme);
        View sheetRatting = LayoutInflater.from(ActivityDetails.this)
                .inflate(R.layout.bottom_sheet_rating,
                        (ViewGroup) findViewById(R.id.bottomSheetContainer2));
        //mapping

        RatingBar rtb = sheetRatting.findViewById(R.id.rattingBar_sh);
        TextView txt_opt = sheetRatting.findViewById(R.id.txt_opt);
        EditText txt_location = sheetRatting.findViewById(R.id.txt_location);
        Button btn = sheetRatting.findViewById(R.id.btn_rating);
        RecyclerView cus_locations = sheetRatting.findViewById(R.id.cus_locations);

        // init
        FirebaseDatabase.getInstance().getReference("Rating").child(product.getPrd_ten().trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            ArrayList<RATING> ratings = new ArrayList<>();
                            float total = 0;
                            for(DataSnapshot dt : snapshot.getChildren())
                            {
                                RATING rt = dt.getValue(RATING.class);
                                rt.phone = dt.getKey();
                                ratings.add(rt);
                                total += Float.parseFloat(rt.rat.trim());
                            }
                            rtb.setRating(tinhRating(ratings.size(),total));
                            txt_opt.setText("Đánh giá chung:  "+ rtb.getRating() +"/5.0");
                            AdapterRating adapterRating = new AdapterRating(getApplicationContext(),ratings);
                            cus_locations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            cus_locations.setAdapter(adapterRating);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_location.getText().toString().trim().isEmpty())
                    Toast.makeText(ActivityDetails.this,"Vui lòng nhập",Toast.LENGTH_SHORT).show();
                else
                {
                    String rating = ratingBar.getRating()+ " ";
                    String cmt = txt_location.getText().toString().trim();
                    RATING newRT = new RATING(COMMON.phone,cmt,rating);
                    try {
                        FirebaseDatabase.getInstance().getReference().child("Rating")
                                .child(product.getPrd_ten().trim())
                                .child(COMMON.phone).setValue(newRT);
//                        cus_locations.getAdapter().notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(ActivityDetails.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.dismiss();

        bottomSheetDialog.setContentView(sheetRatting);
        bottomSheetDialog.show();


        //event
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                txt_location.setTextColor(getResources().getColor(R.color.grey_bg));
            }
        });
    }

    private float tinhRating(int size, float total) {
        return (total/ (size) * 5);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int sl = Integer.parseInt(txt_sl.getText().toString());
            int id = v.getId();
            switch (id)
            {
                case R.id.btn_minius:
                    if(sl > 1)
                        txt_sl.setText(String.valueOf(sl - 1));
                    break;
                case R.id.txt_ratting:
                    txt_ratting.setTextColor(getResources().getColor(R.color.colorPrimary));
                    ProgressDialog dialog = new ProgressDialog(ActivityDetails.this);
                    dialog.setMessage("Đợi trong giây lát...");
                    dialog.show();
                    loadBottomSheet(dialog);
                    break;
                case R.id.btn_like:
                    if(btn_like1.getVisibility() == View.GONE)
                    {
                        btn_like.setVisibility(View.GONE);
                        btn_like1.setVisibility(View.VISIBLE);
                        themYeuThich(true);
                    }
                    break;
                case R.id.btn_like1:
                    if(btn_like.getVisibility() == View.GONE)
                    {
                        btn_like1.setVisibility(View.GONE);
                        btn_like.setVisibility(View.VISIBLE);
                        themYeuThich(false);
                    }
                    break;
                case R.id.btn_plus:
                    if(sl < Integer.parseInt(product.getPrd_soton()))
                        txt_sl.setText(String.valueOf(sl + 1));
                    break;
                case R.id.btn_addtocart:
                    if(Integer.parseInt(product.getPrd_soton()) < 1)
                    {
                        Snackbar.make(main_layout, "Mặt hàng hết hàng",Snackbar.LENGTH_SHORT).show();
                    }
                else
                    {
                        addToCart(product);
                    }
                    break;
            }
        }
    };

    private void themYeuThich(boolean b) {
        if(b)
        {
            value += product.getPrd_ten() + "/";
        }
        else
        {
            value.replace(product.getPrd_ten(),"");
        }
        FirebaseDatabase.getInstance().getReference().child("Favorite")
                .child(COMMON.phone)
                .child("name").setValue(value);
    }
    private void themYeuThich() {
            FirebaseDatabase.getInstance().getReference().child("Favorite")
                    .child(COMMON.phone)
                    .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        value = snapshot.getValue(String.class);
                        favs = value.split("/");
                        for(String s : favs)
                        {
                            if(s!= "" && s!= null && s.contains(product.getPrd_ten()))
                            {
                                if(btn_like1.getVisibility() == View.GONE)
                                {
                                    btn_like.setVisibility(View.GONE);
                                    btn_like1.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    private void addToCart(PRODUCT product) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(COMMON.phone);
        if(userCart != null)
        {
            // lấy trong giỏ hàng sản phẩm có tên là tên sản phẩm hiện tại trong chi tiết
            userCart.child(product.getPrd_ten())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())// truong hop mat hang da duoc them vao gio
                            {
                                //update gio hang
                                CART cart = snapshot.getValue(CART.class);
                                cart.setQuantity(cart.getQuantity() + 1);
                                Map<String, Object> updateData = new HashMap<>();
                                updateData.put("quantity", cart.getQuantity());
                                updateData.put("totalPrice", cart.getQuantity()*Float.parseFloat(cart.getCost()));

                                userCart.child(product.getPrd_ten())
                                        .updateChildren(updateData)
                                        .addOnSuccessListener(aVoid -> {
                                            iCartLoadListener.onCartLoadFailed("Đã thêm vào giỏ hàng");
                                        })
                                        .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                                Snackbar.make(main_layout, "Đã thêm vào giỏ hàng",Snackbar.LENGTH_SHORT).show();
                            }
                            else // thêm một item mới vào giỏ hàng
                            {
                                CART cart =new CART();
                                String currentTime, currentDate;
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM ,yyyy");
                                currentDate = dateFormat.format(calendar.getTime());

                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
                                currentTime = timeFormat.format(calendar.getTime());
//
                                cart.setName(product.getPrd_ten());
                                cart.setImg(product.getPrd_hinh());
                                cart.setPrice(product.getPrd_dongia());
                                cart.setCost(product.getPrd_gia());
                                cart.setQuantity(1);
                                cart.setDate(currentDate);
                                cart.setTime(currentTime);
                                cart.setTotalPrice(Float.parseFloat(product.getPrd_gia()));

                                userCart.child(product.getPrd_ten())
                                        .setValue(cart)
                                        .addOnSuccessListener(aVoid -> {
                                            iCartLoadListener.onCartLoadFailed("Đã thêm vào giỏ hàng");
                                        })
                                        .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                                Snackbar.make(main_layout, "Đã thêm vào giỏ hàng",Snackbar.LENGTH_SHORT).show();
                            }
                            EventBus.getDefault().postSticky(new UpdateCartEvent());
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
            cart.setName(product.getPrd_ten());
            cart.setImg(product.getPrd_hinh());
            cart.setPrice(product.getPrd_dongia());
            cart.setCost(product.getPrd_gia());
            cart.setQuantity(1);
            cart.setDate(currentDate);
            cart.setTime(currentTime);
            cart.setTotalPrice(Float.parseFloat(product.getPrd_gia()));

            userCart.setValue(cart);
        }
    }
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}