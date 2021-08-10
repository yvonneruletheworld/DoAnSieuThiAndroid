package com.example.quanlysieuthi;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ActivityHomePage extends AppCompatActivity implements ICartLoadListener {


    //menu
    private static final int POS_CLOSE = 0;
    private static final int POS_CARD = 1;
    private static final int POS_MY_PROFILE = 2;
    private static final int POS_NEARBY_RES = 3;
    private static final int POS_SETTING = 4;
    private static final int POS_ABOUT_US = 5;
    private static final int POS_LOGOUT = 7;
    private static final String CHANNEL_ID = "101";

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    RelativeLayout mainLayout;
    ChipNavigationBar chipNavigationBar;
    BroadcastReceiverConnect broadcastReceiverConnect;
    public static ICartLoadListener cartLoadListener;

    @Override
    protected void onStart() {
        registedNetworkChange();
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        unregistedNetwork();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(UpdateCartEvent event) {
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        broadcastReceiverConnect = new BroadcastReceiverConnect();
//        registedNetworkChange();
        mapping();
        setSupportActionBar(toolbar);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(100)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        AdapterDrawer adapter = new AdapterDrawer(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_CARD),
                createItemFor(POS_MY_PROFILE),
                createItemFor(POS_NEARBY_RES),
                createItemFor(POS_SETTING),
                createItemFor(POS_ABOUT_US),
                new SpaceItem(260),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this.onItemSelectedListener);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

//        adapter.setSelected(POS_CARD);

        LOADFRAGMENT(new FragmentHome(cartLoadListener));
        hooks();
        createNotificationChannel();
        getToken();
    }

    protected void registedNetworkChange() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            registerReceiver(broadcastReceiverConnect, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            registerReceiver(broadcastReceiverConnect, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void  unregistedNetwork()
    {
        try {
                unregisterReceiver(broadcastReceiverConnect);
        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistedNetwork();
    }

    @SuppressWarnings("rawtypes")
    private ItemDrawer createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.blue_navy))
                .withSelectedTextTint(color(R.color.blue_navy));
    }

    AdapterDrawer.OnItemSelectedListener onItemSelectedListener = new AdapterDrawer.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int position) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (position) {
                case POS_CLOSE:
                    slidingRootNav.closeMenu();
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case POS_CARD:
                    startActivity(new Intent(ActivityHomePage.this, ActivityCustomer.class).putExtra("Fragment",1));
                    break;
                case POS_MY_PROFILE:
                    startActivity(new Intent(ActivityHomePage.this, ActivityCustomer.class).putExtra("Fragment",0));
                    break;
                case POS_NEARBY_RES:

                    break;
                case POS_SETTING:

                    break;
                case POS_ABOUT_US:

                    break;
                case POS_LOGOUT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomePage.this);
                    builder.setMessage("Bạn muốn thoát chương trình? ")
                            .setNegativeButton("Hủy", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setPositiveButton("Thoát", (dialog1, which) -> {
                                finish();
                                startActivity(new Intent(ActivityHomePage.this,ActivityWelcome.class));
                                COMMON.currentUser = null;
                                COMMON.phone = null;
                            });
                    builder.create();
                    builder.show();
                    break;
            }
        }
    };


    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful())
                {
                    Log.d(TAG, "Failed to get Token");
                }
                String token = task.getResult();
                Log.d(TAG, "onComplete: "+ token);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Receive Firebase Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
//        return  null;
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn thoát chương trình? ")
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Thoát", (dialog1, which) -> {
                    this.finish();
                    startActivity(new Intent(ActivityHomePage.this,ActivityWelcome.class));
                    COMMON.currentUser = null;
                    COMMON.phone = null;
                });
        builder.create();
        builder.show();
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);

    }

    public void LOADFRAGMENT(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    private void mapping() {
        mainLayout = findViewById(R.id.main_layout);
        chipNavigationBar = findViewById(R.id.bottomNav);
    }

    private void hooks() {
        ButterKnife.bind(this);
        cartLoadListener = this;
        COMMON.chipNavigationBar = this.chipNavigationBar;
        chipNavigationBar.setItemSelected(R.id.menu_nav_home,true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.menu_nav_home:
                        LOADFRAGMENT(new FragmentHome());
                        break;
                    case R.id.menu_nav_cart:
                        LOADFRAGMENT(new FragmentCart());
                        break;
                    case R.id.menu_nav_shopping:
                        LOADFRAGMENT(new FragmentShop());
                        break;
                    case R.id.menu_nav_qr:
                        LOADFRAGMENT(new FragmentScan());
                        break;
                }
            }
        });
    }

    @Override
    public void onCartLoadSuccess(List<CART> cartList) {
        int cardCount = 0;
        for (CART c : cartList)
            cardCount += 1;
        if (cardCount > 0)
            chipNavigationBar.showBadge(R.id.menu_nav_cart, cardCount);
    }

    @Override
    public void onCartLoadFailed(String message) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CART> carts = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child(COMMON.phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot c : snapshot.getChildren()) {
                            CART cart = c.getValue(CART.class);
                            cart.setName(c.getKey());
                            carts.add(cart);
                        }
                        cartLoadListener.onCartLoadSuccess(carts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }


//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
}
