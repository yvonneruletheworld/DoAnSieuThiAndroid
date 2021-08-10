package com.example.quanlysieuthi;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentSubmitOrder extends Fragment {

    GradientTextView title;
    TextView giagiam,magiamgia, apdung, loaiGiaoHang, thoiGianNhan, giaGiaoHang, ketLuan, chonPTTT, tongTienHang, phiGiaoHang, tongCong;
    RecyclerView cart, pttt;
    Button btn_thanhToan;
    ConstraintLayout gh_layout, discount_layout;
    View btn_back;
    //cart
    List<CART> carts;
    AdapterCash adapterCash;
    double sum, giam, ship = 0;
    //Bottom Sheet
    BottomSheetDialog bottomSheetDialog;
    //View
    View v;
    //Location
    String city, pay_name;
    //bottom sheet
    ArrayList<DISCOUNT> result;
    //bundle
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_submit_order, container, false);
        mapping (v);
        init ();
        return v;
    }

    private void init() {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Đợi trong giây lát...");
        dialog.show();
        title.setColor(title,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));
        loadCart(dialog);
        loadPayment(dialog);
        discount_layout.setOnClickListener(onClickListener);
        gh_layout.setOnClickListener(onClickListener);
        apdung.setOnClickListener(onClickListener);
        btn_thanhToan.setOnClickListener(onClickListener);
    }

    private void loadPayment(ProgressDialog dialog) {
        final DatabaseReference[] payment = {FirebaseDatabase
                .getInstance().getReference("Payment")};
        payment[0].addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<PAYMENT> result = new ArrayList<>();
                    for (DataSnapshot pay : snapshot.getChildren())
                    {
                        PAYMENT p = pay.getValue(PAYMENT.class);
                        result.add(p);
                    }
                    AdapterPayment adapterPayment = new AdapterPayment(getContext(), result, new ItemClickListener() {
                        @Override
                        public void onClick(View v, int position, boolean isLong) {

                        }

                        @Override
                        public void discountClickItem(DISCOUNT dis, int type) {

                        }

                        @Override
                        public void deliveryClickItem(DELIVERY del) {

                        }

                        @Override
                        public void paymentClickItem(PAYMENT pay) {
                            if(pay != null)
                            {
                                pay_name = pay.pm_name.toString();
                                Toast.makeText(getContext(),pay_name, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    pttt.setAdapter(adapterPayment);
                    pttt.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCart(ProgressDialog dialog) {
        bundle = this.getArguments();
        if(bundle != null)
        {
            this.carts = (List<CART>) bundle.getSerializable("Cart");
            if (carts.size() > 0)
            {
                //cart rv
                adapterCash = new AdapterCash(this.getContext(),carts);
                cart.setAdapter(adapterCash);
                cart.setLayoutManager(new LinearLayoutManager(getContext()));
                // total
                sum = bundle.getDouble("Total");
                tongTienHang.setText(new StringBuilder(String.format("%,.0f",sum)).append("đ"));
                tongCong.setText(new StringBuilder(String.format("%,.0f",sum)).append("đ"));
                ketLuan.setText(carts.size() + " mặt hàng. Tổng cộng:  " +tongTienHang.getText());
                if(bundle.getString("Location").contains("HCM") || bundle.getString("Location").contains("Hồ Chí Minh"))
                    city = "HCM";
                else
                    city = "other";
            }
            if(cart.getChildCount() > 0)
                dialog.dismiss();
        }
    }

    private void mapping(View v) {
        title = v.findViewById(R.id.cart_lbl2);
        apdung = v.findViewById(R.id.txt_discount);
        loaiGiaoHang = v.findViewById(R.id.txt_giaohang);
        thoiGianNhan = v.findViewById(R.id.textView5);
        giaGiaoHang = v.findViewById(R.id.txt_hieuluc);
        giagiam = v.findViewById(R.id.txt_giamgia);
        magiamgia = v.findViewById(R.id.text);
        ketLuan = v.findViewById(R.id.txt_caculation);
        chonPTTT = v.findViewById(R.id.txt_pttt_more);
        tongTienHang = v.findViewById(R.id.txt_tongtienhangtt);
        phiGiaoHang  = v.findViewById(R.id.txt_phigiaohang);
        tongCong   = v.findViewById(R.id.txt_tongcong);
        cart   = v.findViewById(R.id.c_recyclerView1);
        pttt   = v.findViewById(R.id.c_recyclerViewPTTT);
        btn_thanhToan   = v.findViewById(R.id.btn_thanhtoan);
        gh_layout    = v.findViewById(R.id.constraintLayout2);
        discount_layout    = v.findViewById(R.id.discount_layout);
        btn_back    = v.findViewById(R.id.od_back_icon2);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id)
            {
                case R.id.txt_discount:
                    if(apdung.getText().toString().trim().contains("Xóa"))
                    {
                        magiamgia.setText("Mã giảm giá (mã chỉ áp dụng 1 lần)");
                        apdung.setText("Áp dụng      ");
                        sum += giam;
                        tongCong.setText(String.format("%,.0f",sum) + " đ");
                        giagiam.setText("000");
                    }
                    //voucher
                    break;
                case R.id.constraintLayout2:
                    //voucher
                    loadBottomSheet(v, 0);
                    break;
                case R.id.discount_layout:
                    //voucher
                    loadBottomSheet(v, 1);
                    break;
                case R.id.sheet_back:
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.btn_thanhtoan:
                    if(sum == 0 || pay_name == null || loaiGiaoHang == null)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setTitle("Cảnh báo")
                                .setMessage("Vui lòng chọn hình thức giao hàng và thanh toán")
                                .setPositiveButton("OK", (dialog2, which) ->
                                {
                                    dialog2.dismiss();
                                }).create();
                        alertDialog.show();


                    }
                    else {
                        ORDER newOrder = createOrder();

                        submitOrder(newOrder);
                    }
                    break;
            }
        }
    };

    private void cleanCart() {
        int n = carts.size();
        if(n > 0) {
            deleteCart(n);
            for (int i =0 ; i < n; i++)
            {
                carts.remove(i);
            }
            AdapterCash adapterCash = new AdapterCash(getContext(), carts);
            cart.setAdapter(adapterCash);
            cart.setLayoutManager(new LinearLayoutManager(getContext()));

        }
    }
    public void LOADFRAGMENT (Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }
    private void deleteCart(int cartSize) {
        for(int i = 0; i < cartSize; i++)
        {
            FirebaseDatabase.getInstance()
                    .getReference("Cart")
                    .child(COMMON.phone)
                    .child(carts.get(i).getName())
                    .removeValue()
                    .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
        }
    }

    private void loadBottomSheet(View v, int loai) {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Đợi trong giây lát...");
        dialog.show();

        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.bottom_sheet,
                (ViewGroup) v.findViewById(R.id.bottomSheetContainer));
        //rv
        RecyclerView shrv = sheetView.findViewById(R.id.sheet_rv);
        TextView txt = sheetView.findViewById(R.id.sheet_title);
        if(loai == 1)
        {
            loadDiscount(shrv, dialog);
            txt.setText("Mã giảm giá nè");
        }
        else
        {
            loadDelivery(shrv, dialog);
            txt.setText("Loại Giao Hàng");
        }
        //back
        sheetView.findViewById(R.id.sheet_back).setOnClickListener(onClickListener);
        //title

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void loadDiscount(RecyclerView shrv, ProgressDialog dialog) {
        int type;
        DatabaseReference discount = FirebaseDatabase
                .getInstance()
                .getReference("Discount");

        discount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    result = new ArrayList<DISCOUNT>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        DISCOUNT dis = dataSnapshot.getValue(DISCOUNT.class);
                        result.add(dis);
                    }
                    AdapterDiscount adapterDiscount = new AdapterDiscount(getContext(), result, sum, city, new ItemClickListener() {
                        @Override
                        public void onClick(View v, int position, boolean isLong) {

                        }

                        @Override
                        public void discountClickItem(DISCOUNT dis, int type) {
                            if(dis != null)
                            {
                                magiamgia.setText("Mã giảm giá: " + dis.dis_code);
                                if(type == 0) {
                                    giam = Double.parseDouble(dis.getDis_value());
                                    sum -= giam;
                                }
                                else if(type == 1)
                                {
                                    giam = sum * 0.01;
                                    sum -= giam;
                                }
                                else
                                {
                                    sum -= ship;
                                    ship = 0;
                                    phiGiaoHang.setText("000 d");
                                }
                                giagiam.setText("-" + String.format("%,.0f", giam) + " đ");
                                tongCong.setText(String.format("%,.0f", sum) + " đ");
                                if (apdung.getText().toString().trim().contains("Áp dụng"))
                                    apdung.setText("Xóa     ");
                            }
                        }

                        @Override
                        public void deliveryClickItem(DELIVERY del) {

                        }

                        @Override
                        public void paymentClickItem(PAYMENT pay) {

                        }
                    });
                    shrv.setAdapter(adapterDiscount);
                    shrv.setLayoutManager(new LinearLayoutManager(getContext()));
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDelivery(RecyclerView shrv, ProgressDialog dialog) {
        DatabaseReference discount = FirebaseDatabase
                .getInstance()
                .getReference("Delivery");

        discount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ArrayList<DELIVERY> result = new ArrayList<DELIVERY>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        DELIVERY del = dataSnapshot.getValue(DELIVERY.class);
                        result.add(del);
                    }
                    AdapterDelivery adapterDelivery = new AdapterDelivery(getContext(), result, new ItemClickListener() {
                        @Override
                        public void onClick(View v, int position, boolean isLong) {

                        }

                        @Override
                        public void discountClickItem(DISCOUNT dis, int type) {

                        }

                        @Override
                        public void deliveryClickItem(DELIVERY del) {
                            if(del != null)
                            {
                                Toast.makeText(getContext(), del.getGh_name(), Toast.LENGTH_SHORT).show();
                                loaiGiaoHang.setText(del.getGh_name());
                                giaGiaoHang.setText(String.format("%,.0f",Double.parseDouble(del.getGh_phi())) + " đ");
                                phiGiaoHang.setText(String.format("%,.0f",Double.parseDouble(del.getGh_phi()))+ " đ");
                                thoiGianNhan.setText(del.gh_temp);
                                ship = Double.parseDouble(del.getGh_phi());
                                sum += ship;
                                tongCong.setText(String.format("%,.0f",sum)  + " đ");
                            }
                        }

                        @Override
                        public void paymentClickItem(PAYMENT pay) {

                        }
                    });
                    shrv.setAdapter(adapterDelivery);
                    shrv.setLayoutManager(new LinearLayoutManager(getContext()));
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitOrder(ORDER ord) {
    try {
        DatabaseReference orderOfDays = FirebaseDatabase
                .getInstance()
                .getReference("Orders");
        orderOfDays.push().setValue(ord);
        cleanCart();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Đặt hàng")
                .setMessage("Đặt hàng thành công. Quý khách sẽ được chuyển hướng về trang chủ")
                .setPositiveButton("OK", ((dialog2, which) ->
                {
                    LOADFRAGMENT(new FragmentHome());
                    COMMON.chipNavigationBar.dismissBadge(R.id.menu_nav_cart);
                    COMMON.chipNavigationBar.setItemSelected(R.id.menu_nav_home, true);
                    dialog2.dismiss();
                })).create();
        alertDialog.show();
    }
    catch (Exception e)
    {

    }
    }

    private ORDER createOrder() {
        if(carts.size() > 0)
        {
            String ord_img = "", ord_date, ord_product = "", ord_address, ord_total, ord_quality = "", ord_time, ord_receive, ord_delivery, ord_discount;
            //time, date
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
            ord_date = dateFormat.format(calendar.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
            ord_time = timeFormat.format(calendar.getTime());
            //order
            ORDER newOrder = null;
            for (CART c : carts) {
                ord_product += c.getName() + "/";
                ord_quality += c.getQuantity() + ",";
                ord_img += c.getImg() + ",";
            }
            ord_address = bundle.getString("Location");
            ord_total = String.valueOf(sum);
            ord_delivery = String.valueOf(ship);
            ord_discount = String.valueOf(giam);
            ord_receive = "no";

            newOrder = new ORDER(ord_product,ord_address,ord_quality
                        ,ord_receive ,ord_total,ord_time,ord_date,COMMON.phone,ord_delivery,ord_discount,pay_name,ord_img,"Chờ tiếp nhận");
            return newOrder;
        }
        else
            return null;
    }
}