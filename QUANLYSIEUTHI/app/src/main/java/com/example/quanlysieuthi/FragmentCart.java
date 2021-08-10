package com.example.quanlysieuthi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class FragmentCart extends Fragment implements ICartLoadListener {

    RecyclerView recyclerView;
    TextView txt_total, txt_location, option;
    EditText cus_phone, cus_name;
    List<CART> carts;
    ICartLoadListener cartLoadListener;
    ScrollView main_layout;
    AdapterCart adapterCart;
    GradientTextView title;
    View btn_location, location_layout,v;
    Button btnCash, btnThuThap;
    BottomSheetDialog bottomSheetDialog;
    double sum  = 0;
    boolean isLocation = false;
    LottieAnimationView img;
    String [] a = null;
    FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList<String> locations = null;
    AdapterCustomerLocation adapterCustomerLocation = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode =  ThreadMode.MAIN, sticky = true)
    public void onUpdateCart (UpdateCartEvent event)
    {
        double sum  = 0;
        for (CART cart : carts)
        {
            sum += cart.getTotalPrice();
        }
        txt_total.setText(new StringBuilder(String.format("%,.0f", sum)).append(" đ"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart, container, false);
        mapping(v);
        init();
        loadCartFromFireBase();
        return v;
    }

    private void mapping (View v)
    {
        recyclerView = v.findViewById(R.id.c_recyclerView);
        title = v.findViewById(R.id.cart_lbl);
        txt_total = v.findViewById(R.id.c_tt);
        carts = new ArrayList<>();
        main_layout = v.findViewById(R.id.cart_main_layout);
        btn_location = v.findViewById(R.id.location_img);
        location_layout = v.findViewById(R.id.cus_orders);
        txt_location = v.findViewById(R.id.txt_location);
        btnCash = v.findViewById(R.id.btn_thanhtoan);
        btnThuThap = v.findViewById(R.id.btn_thuthap);
        cus_name = v.findViewById(R.id.c_cus_name);
        cus_phone = v.findViewById(R.id.c_cus_phone);
    }

    private void init ()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        btn_location.setOnClickListener(onClickListener);
        btnCash.setOnClickListener(onClickListener);
        location_layout.setOnClickListener(onClickListener);
        title.setColor(title,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));

        //customer
        loadCusInfo();
        //cart
        ButterKnife.bind(this.getActivity());

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), layoutManager.getOrientation()));

        SwpieHelper swpieHelper = new SwpieHelper(getContext(),recyclerView,200) {
            @Override
            public void instantiateCartButton(RecyclerView.ViewHolder viewHolder1, List<SwpieHelper.DeleteButton> buffer) {
                buffer.add(new DeleteButton(getContext(),
                        "Delete",
                        30,
                        0,
                        Color.parseColor("#FF3C30"),
                        new DeleteButtonListener () {
                            @Override
                            public void onClick(int pos) {
//                                Toast.makeText(ActivityCart.this,"Deleted", Toast.LENGTH_SHORT).show();
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("Xóa")
                                        .setMessage("Xóa "+carts.get(pos).getName()+" khỏi giỏ hàng ?")
                                        .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                                        .setPositiveButton("OK", (dialog2, which) ->
                                        {
                                            //
                                            deleteFromFirebase(pos);
                                            carts.remove(pos);
                                            adapterCart.notifyItemRemoved(pos);
                                            dialog2.dismiss();
                                            COMMON.chipNavigationBar.showBadge(R.id.menu_nav_cart,carts.size());
                                        }).create();
                                alertDialog.show();
                            }
                        }));
            }
        };

    }

    private void loadCusInfo() {
        cus_phone.setText(COMMON.phone);
        cus_name.setText(COMMON.currentUser.getUserName());


        String add = COMMON.currentUser.getAddress().replace(" ","");
        if(add != "")
        {
            a = COMMON.currentUser.getAddress().split("/");
            txt_location.setText(a[0]);
        }
        else
            loadBottomSheet(a);
    }

    private void deleteFromFirebase(int pos) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(COMMON.phone)
                .child(carts.get(pos).getName())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new UpdateCartEvent()));
    }
    private void loadCartFromFireBase() {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(COMMON.phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot cartItem : snapshot.getChildren())
                            {
                                CART cart = cartItem.getValue(CART.class);
                                carts.add(cart);
                            }
                            cartLoadListener.onCartLoadSuccess(carts);
                        }
                        else
                        {
                            cartLoadListener.onCartLoadFailed("Giỏ hàng trống");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onCartLoadSuccess(List<CART> cartList) {
        for (CART cart : cartList)
        {
            sum += cart.getTotalPrice();
        }
        txt_total.setText(new StringBuilder(String.format("%,.0f", sum)).append(" đ"));
        adapterCart = new AdapterCart(getContext(), cartList);
        recyclerView.setAdapter(adapterCart);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(main_layout,message,Snackbar.LENGTH_LONG).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id)
            {
                case R.id.location_img:
//                    loadLocation(txt_location);
                    loadBottomSheet(a);
                    break;
                case R.id.cus_orders:
//                    loadLocation(txt_location);
                    loadBottomSheet(a);
                    break;
                case R.id.btn_thanhtoan:
                    if(recyclerView.getChildCount() > 0
                    && !txt_location.getText().toString().contains("Thêm địa chỉ"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Cart", (Serializable) carts);
                        bundle.putDouble("Total", sum);
                        bundle.putString("Location", txt_location.getText().toString());

                        FragmentSubmitOrder submit = new FragmentSubmitOrder();
                        submit.setArguments(bundle);
                        LOADFRAGMENT(submit);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Vui lòng chọn địa chỉ nhận hàng", Toast.LENGTH_SHORT);
                    }
//                    ORDER ord = createOrder();
//                    submitOrder(ord);
                    break;
            }
        }
    };
    public void LOADFRAGMENT (Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    private void loadBottomSheet(String[] ar) {
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.bottom_sheet_location,
                (ViewGroup) v.findViewById(R.id.bottomSheetContainer1));
        // item
        option =  sheetView.findViewById(R.id.txt_opt);
        EditText location = sheetView.findViewById(R.id.txt_location);
        ListView cusLocations = sheetView.findViewById(R.id.cus_locations);
        Button btn = sheetView.findViewById(R.id.btn_location);
        img = sheetView.findViewById(R.id.location_opt);
        locations = new ArrayList<>();

        // init
        if(ar != null) {
            for (int i = 0; i < ar.length; i++) {
                locations.add(ar[i]);
            }
        }
        adapterCustomerLocation = new AdapterCustomerLocation(locations, getContext());

        cusLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_location.setText(locations.get(position));
                bottomSheetDialog.dismiss();
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id)
                {
                    case R.id.location_opt:
                        if (isLocation == false)
                        {
                            img.setMinAndMaxProgress(0.0f, 0.5f);
                            img.playAnimation();
                            option.setText("Sử dụng định vị");
                            location.setEnabled(false);
                            loadLocation(location);
                            isLocation = true;
                        }
                        else
                        {
                            img.setMinAndMaxProgress(0.5f, 1.0f);
                            img.playAnimation();
                            option.setText("Khách hàng tùy chỉnh");
                            location.setEnabled(true);
                            isLocation = false;
                        }
                        break;
                    case R.id.btn_location:
                        if(locations.size() > 0)
                        {
                            if(location.getText().toString().trim().contains("Địa chỉ"))
                            {
                                Toast.makeText(getContext(),"Vui lòng thêm địa chỉ", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                boolean isHave = false;
                                for (String l : locations)
                                {
                                    if(location.getText().toString().trim().contains(l))
                                    {
                                        isHave = true;
                                        break;
                                    }
                                }
                                if (isHave == false)
                                {
                                    locations.add(location.getText().toString().trim());
                                    adapterCustomerLocation.notifyDataSetChanged();

                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(COMMON.phone).child("address")
                                            .setValue(COMMON.currentUser.getAddress() +"/"+ location.getText().toString().trim());

                                    cusLocations.setAdapter(adapterCustomerLocation);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Địa chỉ này đã tồn tại", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        }
                        else {
                            locations.add(location.getText().toString().trim());
                            adapterCustomerLocation.notifyDataSetChanged();

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(COMMON.phone).child("address")
                                    .setValue(COMMON.currentUser.getAddress() + location.getText().toString().trim()+"/");

                            cusLocations.setAdapter(adapterCustomerLocation);
                        }
                        break;
                }
            }
        };

        cusLocations.setAdapter(adapterCustomerLocation);
        img.setOnClickListener(onClickListener);
        btn.setOnClickListener(onClickListener);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void loadLocation(EditText txt_location) {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        try {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),location.getLongitude(), 1
                            );
                            txt_location.setText(
                                    addresses.get(0).getAddressLine(0)

                            );
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Lỗi định vị", Toast.LENGTH_SHORT).show();
                        img.setMinAndMaxProgress(0.5f, 1.0f);
                        img.playAnimation();
                        option.setText("Khách hàng tùy chỉnh");
                        txt_location.setEnabled(true);
                        isLocation = false;
                    }
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
}