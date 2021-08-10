package com.example.quanlysieuthi;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

public class FragmentScan extends Fragment {

    ImageView imgpd;
    private CodeScanner mCodeScanner;
    TextView txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);
        txt = v.findViewById(R.id.txt_view);
        imgpd = v.findViewById(R.id.img);
        CodeScannerView scannerView = v.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(),scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show();

                        loadSanPham(String.valueOf(result.getText()));
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return v ;
    }

    private void loadSanPham(String s) {
        FirebaseDatabase.getInstance().getReference("Product").child(s)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
//                            try {
                                PRODUCT prd = snapshot.getValue(PRODUCT.class);
                                if(prd != null)
                                {
                                    txt.setText(String.valueOf(snapshot.getKey()));
                                    txt.setTextSize(14);
                                    Glide.with(getContext()).load(prd.getPrd_hinh()).into(imgpd);
                                    txt.setText(prd.getPrd_ten());

                                    imgpd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), ActivityDetails.class);
                                            intent.putExtra("product",prd);

                                            Pair[] paints = new Pair[1];
                                            paints[0] = new Pair<View, String>(imgpd,"image");
                                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),paints);
                                            getActivity().startActivity(intent, activityOptions.toBundle());
                                        }
                                    });
                                    txt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), ActivityDetails.class);
                                            intent.putExtra("product",prd);

                                            Pair[] paints = new Pair[1];
                                            paints[0] = new Pair<View, String>(imgpd,"image");
                                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),paints);
                                            getActivity().startActivity(intent, activityOptions.toBundle());
                                        }
                                    });
                                }
//                            }
//                            catch (Exception e)
//                            {
//                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == getActivity().RESULT_OK)
//        {
//            Bitmap b = (Bitmap) data.getExtras().get("data");
//            screen.setImageBitmap(b);
//        }
//    }
}