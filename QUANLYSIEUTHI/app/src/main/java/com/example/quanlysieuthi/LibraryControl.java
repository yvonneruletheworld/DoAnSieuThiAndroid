package com.example.quanlysieuthi;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.core.content.ContextCompat.getSystemService;

public class LibraryControl {
    public void startAnimation ( Button btn, MotionEvent event, Context context)
    {
        Animation upAnimation,downAnimation;
        upAnimation = AnimationUtils.loadAnimation( context, R.anim.button_scaleup_animation);
        downAnimation = AnimationUtils.loadAnimation(context, R.anim.button_scaledown_animation);
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            btn.startAnimation(downAnimation);
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            btn.startAnimation(upAnimation);
        }
    }

    public  boolean validationPhone ( String phone)
    {
        Pattern pattern = Pattern.compile("^0[0-9]{8}$");
        Matcher matcher = pattern.matcher(phone.toString().replace(" ",""));
        if(matcher.matches()) {
            return true;
        }
        return false;
    }

    public  boolean validationPass ( String pass)
    {
        final String PWD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern =  Pattern.compile(PWD_PATTERN);
        Matcher matcher = pattern.matcher(pass.replace(" ",""));
        if(matcher.matches())
        {
            return true;
        }
        return false;
    }


    public USER LogIn(TextInputEditText pass,TextInputEditText username,TextInputEditText phone,TextInputEditText address,TextInputEditText dob, RadioButton gender)
    {
        TextInputEditText textInputEditTexts [] = {pass,username,phone,address,dob};
        USER user = new USER();
        String pw, name, d, ad;
        user.setHinh("h");
        user.setNdphone("no");
        Random r = new Random();
        int randomID = r.nextInt();
        user.setUserID("KH"+randomID);
//        user.setPhone(phone);
        if(gender.getId() == R.id.info_rdo_nu)
            user.setGender(0);
        user.setGender(1);
        for (int i = 0; i< textInputEditTexts.length; i++)
        {
            if(textInputEditTexts[i].getText().toString().isEmpty())
            {
                textInputEditTexts[i].setError("Thông tin không được trống!");
                return null;
            }
            else
            {
                int txt_id = textInputEditTexts[i].getId();
                switch (txt_id) {
                    case R.id.register_username2_txt:
                         name = textInputEditTexts[i].getText().toString();
                         user.setUserName(name);
                        break;
                    case R.id.register_address_txt:
                        ad = textInputEditTexts[i].getText().toString();
                            user.setAddress(ad);
                        break;
                    case R.id.register_newpass_txt:
                        pw = textInputEditTexts[i].getText().toString();
                        if(validationPass(pw) == true)
                            user.setPass(pw);
                        else
                        {
                            textInputEditTexts[i].requestFocus();
                            textInputEditTexts[i].setError("Mật Khẩu không hợp lệ");
                            return null;
                        }
                        break;
                    case R.id.register_ngaysinh_txt:
                        d = textInputEditTexts[i].getText().toString();
                        user.setDoB(d);
                        break;
                }

            }

        }

        return user;
    }


}
