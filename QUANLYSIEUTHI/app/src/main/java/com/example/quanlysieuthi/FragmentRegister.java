package com.example.quanlysieuthi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragmentRegister extends Fragment {

    private static final int NOTIFICATION_ID = 1;
    EditText phone_txt, op1, op2, op3, op4;
    CheckBox cb_save;
    Button btnDk;
    SharedPreferences sharedPreferences;
    TextView forgot_pass, title, back;
    Animation btnAnimation;
    LibraryControl controlLibrary = new LibraryControl();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // mapping

        phone_txt = v.findViewById(R.id.register_sdt_txt);
        op1 = v.findViewById(R.id.otp_1);
        op2 = v.findViewById(R.id.otp_2);
        op3 = v.findViewById(R.id.otp_3);
        op4 = v.findViewById(R.id.otp_4);
        btnDk = v.findViewById(R.id.register_btn);
        title =v.findViewById(R.id.frm_rg_title);
        back =v.findViewById(R.id.back_otp);

        btnAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.button_animation);

        EditText edit [] = {op1,op2,op3,op4};

        phone_txt.addTextChangedListener(this.textWatcher_phone);
        op1.addTextChangedListener(new GenericTextWatcher(edit, op1));
        op2.addTextChangedListener(new GenericTextWatcher(edit, op2));
        op3.addTextChangedListener(new GenericTextWatcher(edit, op3));
        op4.addTextChangedListener(new GenericTextWatcher(edit, op4));
       btnDk.setOnTouchListener(this.onTouchListener);
       back.setOnTouchListener(this.onTouchListener);
       btnDk.setEnabled(false);

       title.setAnimation(btnAnimation);
        return v;
    }

    TextWatcher textWatcher_phone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Pattern pattern = Pattern.compile("^0[0-9]{10}$");
            Matcher matcher = pattern.matcher(phone_txt.getText().toString().replace(" ",""));
            if(!matcher.matches() && s.length() >=10) {
                op1.setEnabled(true);
                op2.setEnabled(true);
                op3.setEnabled(true);
                op4.setEnabled(true);
//                pushNotification();
                btnDk.setEnabled(true);
                Random rand = new Random();
                double value = rand.nextInt(100000);
            char [] ar = String.valueOf((int)(value)).toCharArray();
            op1.setText(ar[0]+"");
            op2.setText(ar[1]+"");
            op3.setText(ar[2]+"");
            op4.setText(ar[3]+"");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

//    private void pushNotification() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_baseline_account_circle_24)
//                .setContentTitle("Siêu thị YVONNE - Gửi Mã Xác Nhận Đăng Ký")
//                .setContentText(" Mã Xác Nhận Của Bạn là: ")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(value + ""))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
////        Notification notification = new Notification.Builder(getContext())
////                .setContentTitle("Siêu thị YVONNE - Gửi Mã Xác Nhận Đăng Ký")
////                .setContentText(" Mã Xác Nhận Của Bạn là: "+ value)
////                .setSmallIcon(R.drawable.ic_baseline_account_circle_24)
////                .setLargeIcon(bitmap)
////                .setColor(getResources().getColor(R.color.yellow_bg))
////                .build();
//        NotificationManager notificationManager =
//                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        if(notificationManager != null)
//        {
//            notificationManager.notify(NOTIFICATION_ID,notification);

//        }
//    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            if(id == R.id.register_btn)
            {
                controlLibrary.startAnimation(btnDk,event,getContext());

                //action

                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Đợi trong giây lát...");
                dialog.show();

                initFireBase(dialog);

            }
            else
            {
                LOADFRAGMENT(new FragmentLogin());
            }
            return true;
        }
    };

    private void LOADFRAGMENT (Fragment cur)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, cur);
        transaction.commit();
    }

    private void initFireBase(ProgressDialog dialog)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(phone_txt.getText().toString()).exists())
                {
                    try {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Số điện thoại đã tồn tại", Toast.LENGTH_LONG).show();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone_txt.getText().toString().trim());
                    FragmentRegister_Info_1 register_info_1Fragment = new FragmentRegister_Info_1();

                    register_info_1Fragment.setArguments(bundle);

                    LOADFRAGMENT(register_info_1Fragment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}