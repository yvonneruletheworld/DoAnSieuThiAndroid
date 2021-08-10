package com.example.quanlysieuthi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragmentLogin extends Fragment {

    TextInputEditText email_txt, pass_txt;
    CheckBox cb_save;
    Button btnDk;
    SharedPreferences sharedPreferences;
    TextView forgot_pass, register;
    GradientTextView login_title;
    Animation btnAnimation;
    LibraryControl controlLibrary = new LibraryControl();
    boolean istouched = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void general(String s) {
        if (isPhone(s)) {
            btnDk.setEnabled(true);
        }

    }

    private boolean isPhone(String phone) {
        if (phone.equals(null))
            return false;
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches())
            return true;
        return false;
    }

    public void LOADFRAGMENT(Fragment currentFragment) {
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, currentFragment);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        if(!isConnect(getActivity()))
        {
            showConnectRequiered();
        }
        email_txt = v.findViewById(R.id.login_phone_txt);
        pass_txt = v.findViewById(R.id.login_pass_txt);
        cb_save = v.findViewById(R.id.login_cb);
        btnDk = v.findViewById(R.id.login_login_btn);
        sharedPreferences = this.getActivity().getSharedPreferences("SaveLogin", getContext().MODE_PRIVATE);
        forgot_pass = v.findViewById(R.id.login_forget_txt);
        register = v.findViewById(R.id.login_register_txt);
        login_title = v.findViewById(R.id.login_title);

//        Animation btnAnimation;
        btnAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.button_animation);
        boolean saveLogin = sharedPreferences.getBoolean("idSave", false);
        // get state of checkbox Save, if value1 is invalid get value2 as a default

        if (saveLogin) {
            // Checkbox Save was checked last time
            email_txt.setText(sharedPreferences.getString("email", ""));
            pass_txt.setText(sharedPreferences.getString("pass", ""));
        }

        Bundle b = getArguments();
        if(b != null)
        {
            email_txt.setText(b.getString("phonename"));
            pass_txt.setText(b.getString("passname"));
        }
        btnDk.setAnimation(btnAnimation);
        btnDk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                controlLibrary.startAnimation(btnDk,event, getContext());

                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Đợi trong giây lát...");
                dialog.show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (cb_save.isChecked()) {
                        editor.putString("email", email_txt.getText().toString());
                        editor.putString("pass", pass_txt.getText().toString());
                    }
                    editor.putBoolean("idSave", cb_save.isChecked());
                    editor.commit();
                LogIn(dialog);
                return true;
            }
        });

        if(email_txt.getText().toString() != "" && pass_txt.getText().toString() != "")
            btnDk.setEnabled(true);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADFRAGMENT(new FragmentForgotPass());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADFRAGMENT(new FragmentRegister());
            }
        });
        email_txt.addTextChangedListener(this.textWatcher);
        btnDk.setEnabled(false);
        login_title.setColor(login_title,getResources().getColor(R.color.peach_bg_1), getResources().getColor(R.color.blue));

        return v;
    }

    private void showConnectRequiered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Vui lòng kết nối thiết bị của bạn với Internet")
                .setCancelable(false)
                .setPositiveButton("Kết nối", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity().getApplicationContext(), ActivityWelcome.class));
                        getActivity().finish();
                    }
                }).create();
        builder.show();
    }

    private boolean isConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiCon != null && wifiCon.isConnected())
        || (mobileCon != null && mobileCon.isConnected()))
            return true;
        else return false;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = email_txt.getText().toString();
            //Toast.makeText(LogInActivity.this, str, Toast.LENGTH_LONG).show();
            general(str);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private void LogIn (ProgressDialog dialog)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            //bắt sự kiện để kiểm tra chuỗi số đt
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Kiểm tra người dùng có tồn tại hay không
                 if(snapshot.child(email_txt.getText().toString()).exists()) {
                     dialog.dismiss();
                     USER user = snapshot.child(email_txt.getText().toString()).getValue(USER.class);
                     // lấy root lưu là sđt để kiểm tra, lấy cái user ở root khớp sđt
                     // model user để nhận giá trị
                     // user nhận giá trị là một USER ở root có giá trị mk đúng
                     if (user.getPass().equals(pass_txt.getText().toString())) {
                         // kiểm tra cái giá trị txt với các giá trị đã được lấy lên từ database và khớp
                         // vào model user
                         Intent intent = new Intent(getActivity(), ActivityEmpty.class);
                         COMMON.currentUser = user;
                         COMMON.phone = email_txt.getText().toString().trim();
                         startActivity(intent);
                     }
                     else
                         {
                             dialog.dismiss();
                         Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
//                         email_txt.setText("");
//                         pass_txt.setText("");
                         email_txt.requestFocus();
                             return;

                         }
                 }else {
                     dialog.dismiss();
                     Toast.makeText(getContext(), "Người dùng không tồn tại", Toast.LENGTH_LONG).show();
//                    email_txt.setText("");
//                    pass_txt.setText("");
                    email_txt.requestFocus();
                     return;

                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}