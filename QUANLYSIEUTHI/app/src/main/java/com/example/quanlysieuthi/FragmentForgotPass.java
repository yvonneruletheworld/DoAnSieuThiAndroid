package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragmentForgotPass extends Fragment {

    EditText pass_txt, repass_txt, phone_txt;
    TextView back;
    Button btn;
    boolean istouched;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        // mapping

        pass_txt = v.findViewById(R.id.repass_pass_txt);
        repass_txt = v.findViewById(R.id.repass_repass_txt);
        phone_txt = v.findViewById(R.id.repass_sdt_txt);
        back = v.findViewById(R.id.back_txt);
        btn = v.findViewById(R.id.repass_btn);

        // define
        btn.setEnabled(false);
        pass_txt.setEnabled(false);
        repass_txt.setEnabled(false);

        istouched = false;


        //edit text validation
        pass_txt.addTextChangedListener(this.textWatcher);
        phone_txt.addTextChangedListener(this.textWatcher_phone);
        repass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADFRAGMENT(new FragmentLogin());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repass_txt.getText().toString().trim().equals(pass_txt.getText().toString().trim()))
                {
                    Toast.makeText(getContext(),"OK", Toast.LENGTH_LONG);
                }
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (istouched == false) {
                    btn.setBackgroundResource(R.drawable.gradient_background);
                    btn.setTextColor(getResources().getColor(R.color.white));
                    istouched = true;
                }
                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOADFRAGMENT(new FragmentLogin());
            }
        });
        // return
        return v;


    }

    TextWatcher textWatcher_phone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Pattern pattern = Pattern.compile("^0[0-9]{8}$");
            Matcher matcher = pattern.matcher(phone_txt.getText().toString().replace(" ",""));
            if(matcher.matches()) {
                repass_txt.setEnabled(true);
                pass_txt.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher textWatcher = new TextWatcher() {
//        int id = getActivity().getTaskId();
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            switch (id)
//            {
//                case R.id.repass_pass_txt:
                    if(passValidation(pass_txt.getText().toString().trim()) == true)
                    {
                        if(repass_txt.length() >8)
                        {
                            btn.setEnabled(true);
                        }
//                    }
//                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private boolean passValidation (String pass)
    {
        final String PWD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        Pattern pattern =  Pattern.compile(PWD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        if(matcher.matches())
            return true;
        return false;
    }

    public void LOADFRAGMENT(Fragment currentFragment) {
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, currentFragment);
        transaction.commit();
    }
}