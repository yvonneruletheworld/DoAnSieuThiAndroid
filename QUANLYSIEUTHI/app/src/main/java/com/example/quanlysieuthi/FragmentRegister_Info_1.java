package com.example.quanlysieuthi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class FragmentRegister_Info_1 extends Fragment {


    TextInputEditText txt_ngaysinh, txt_mk, txt_hoten, txt_diachi, txt_phone;
    RadioButton rd_gt_0,rd_gt_1;
    Button btn;
    LibraryControl controlLibrary = new LibraryControl();
    Bundle bundle;
    USER newUser;
    TextView back;
    boolean rs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentRegister_Info_1()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  =inflater.inflate(R.layout.fragment_register__info_1, container, false);

        //mapping
        txt_ngaysinh = v.findViewById(R.id.register_ngaysinh_txt);
        txt_mk = v.findViewById(R.id.register_newpass_txt);
        txt_phone = v.findViewById(R.id.register_username_txt);
        txt_hoten = v.findViewById(R.id.register_username2_txt);
        txt_diachi = v.findViewById(R.id.register_address_txt);
        rd_gt_0 = v.findViewById(R.id.info_rdo_nu);
        rd_gt_1 = v.findViewById(R.id.info_rdo_nam);
        btn = v.findViewById(R.id.register_info_btn);
        back = v.findViewById(R.id.back_rginfo);
        bundle = this.getArguments();
//        btn.setEnabled(false);
        // hooks

//        txt_ngaysinh.setOnClickListener(this.onClickListener);
        txt_ngaysinh.setOnTouchListener(this.calenderTouch);
        btn.setOnTouchListener(this.onTouchListener);
        back.setOnTouchListener(this.onTouchListener);
        txt_phone.setText(bundle.getString("phone").toString());
        txt_phone.setEnabled(false);

        return v;
    }
    View.OnTouchListener calenderTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , onDateSetListener, year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            return true;
        }
    };

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month +=1;
            Log.d(TAG, "onDateSet: dd/mm/yy: "+ dayOfMonth+ "/" +month+"/" +year);

            if((2021 - year ) > 10)
            {
                String date = dayOfMonth+ "/" +month+"/" +year;
                txt_ngaysinh.setText(date);
            }
            else
            {
                Toast.makeText(getContext(),"Ngày sinh không hợp lệ", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(v.getId() == R.id.register_info_btn)
            {
                //animation
                controlLibrary.startAnimation(btn,event,getContext());

                //action
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Xin đợi...");
                dialog.show();
                if(initFireBase_info(dialog) == true)
                {
                    //startActivity(new Intent(getActivity(), ActivityEmpty.class));
//                getActivity().finish();
                    Bundle bundleRG = new Bundle();
                    bundleRG.putString("phonename",txt_phone.getText().toString().trim());
                    bundleRG.putString("passname",txt_mk.getText().toString().trim());
                    FragmentLogin l = new FragmentLogin();
//                    l.setArguments(bundleRG);
//                    LOADFRAGMENT(l);
                    FragmentRegister_Info_1.this.LOADFRAGMENT(l);
                }
            }
            else
            {
                Bundle bundleRG = new Bundle();
                bundleRG.putString("phonename",txt_phone.getText().toString().trim());
                bundleRG.putString("passname",txt_mk.getText().toString().trim());
                FragmentLogin l = new FragmentLogin();
//                    l.setArguments(bundleRG);
//                    LOADFRAGMENT(l);
                FragmentRegister_Info_1.this.LOADFRAGMENT(l);
            }
            return true;
        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(controlLibrary.validationPass(txt_mk.getText().toString()))
                btn.setEnabled(true);
            else
                Toast.makeText(getContext(),"Mật khẩu phải bao gồm ký tự đặt biệt, số và chữ hoa", Toast.LENGTH_LONG).show();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean initFireBase_info (ProgressDialog dialog)
    {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("User");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(rd_gt_0.isChecked()) {
                    newUser = controlLibrary.LogIn(txt_mk, txt_hoten,txt_phone, txt_diachi, txt_ngaysinh, rd_gt_0);
                }
                else
                    newUser = controlLibrary.LogIn(txt_mk, txt_hoten,txt_phone, txt_diachi, txt_ngaysinh, rd_gt_1);
                if(newUser != null)
                {
                    reference.child(String.valueOf(txt_phone.getText())).setValue(newUser);
                    dialog.dismiss();
                    Toast.makeText(getContext(),"Đăng kí thành công", Toast.LENGTH_LONG).show();
                    bundle = null;
                    rs = true;

                }
                else
                {
                    dialog.dismiss();
                    rs = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rs;
    }

    private void LOADFRAGMENT (Fragment cur)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, cur);
        transaction.commit();
    }


}