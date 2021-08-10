package com.example.quanlysieuthi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class FragmentShopping extends Fragment {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    ImageView back_img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping, container, false);

        mapping(v);
        hooks();
        return v;

    }

    private void mapping (View v) {
        spinner = v.findViewById(R.id.spinner1);
        adapter  = ArrayAdapter.createFromResource(getContext(),
                R.array.chinhanh, R.layout.item_chinhanh);
//        back_img = v.findViewById(R.id.back);
    }

    private void  hooks ()
    {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        //back
//        Glide.with(getContext()).load("https://www.englishclub.com/images/vocabulary/food/vegetables/vegetables.jpg").into(back_img);
    }

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}