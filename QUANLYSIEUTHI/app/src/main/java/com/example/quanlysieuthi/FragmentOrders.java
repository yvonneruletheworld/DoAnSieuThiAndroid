package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentOrders extends Fragment {

    View back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);

        back = v.findViewById(R.id.od_back_icon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getFragmentManager().beginTransaction().remove(FragmentOrders.this).commit();
                LOADFRAGMENT(R.id.frame_main_cus, new FragmentCustomer());
            }
        });
        LOADFRAGMENT(R.id.frame_main_order,new FragmentNothingOrder() );
        return v;
    }
    private void LOADFRAGMENT (int frame, Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(frame, fragment);
        transaction.commit();
    }



}