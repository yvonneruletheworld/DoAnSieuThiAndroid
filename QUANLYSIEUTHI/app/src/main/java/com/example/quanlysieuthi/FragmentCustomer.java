package com.example.quanlysieuthi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FragmentCustomer extends Fragment {

    //Variour
    TextView name, sdt, nsinh, diachi, change, txt_ndphone;
    View gen, ndphone, btn, view;
    ImageView ava;

    EditText pname, username, pass, phone;

    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer, container, false);
        mapping(view);
        hooks();
        init();
        return view;
    }

    private void LOADFRAGMENT (Fragment fragment)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_cus, fragment);
        transaction.commit();
    }

    private void mapping (View v)
    {
        //
        name = v.findViewById(R.id.cus_name);
        nsinh = v.findViewById(R.id.cus_dob);
        diachi = v.findViewById(R.id.cus_addr);
        sdt = v.findViewById(R.id.cus_phone);
        ava = v.findViewById(R.id.dt_cus_avatar);
        gen = v.findViewById(R.id.cus_gender);
        change = v.findViewById(R.id.change);
        pname = v.findViewById(R.id.editTextTextPersonName);
        pass = v.findViewById(R.id.editTextUserPass);
        phone = v.findViewById(R.id.editTextTextPhone);
        username = v.findViewById(R.id.editTextUserName);
        ndphone = v.findViewById(R.id.cus_ndphone);
        txt_ndphone = v.findViewById(R.id.cus_ndphonetxt);
        btn = v.findViewById(R.id.cus_btn);
    }

    private void hooks ()
    {
        name.setText(COMMON.currentUser.getUserName());
        nsinh.setText(COMMON.currentUser.getDoB());
        String dc = COMMON.currentUser.getAddress();
        String [] dcChinh = dc.split("/");
        diachi.setText(dcChinh[0]);
        String nd = COMMON.currentUser.getNdphone();
        if(nd != "no")
        txt_ndphone.setText("Liên lạc tin cây: " + nd);
        Glide.with(getContext()).load(COMMON.currentUser.getHinh()).into(ava);
        if(COMMON.currentUser.getGender() == 1 )
            gen.setBackgroundColor(getResources().getColor(R.color.teal_700));

        pname.setEnabled(false);
        username.setEnabled(false);
        pass.setEnabled(false);
        phone.setEnabled(false);
    }
    private void init ()
    {
        change.setOnClickListener(onClickListener);
        btn.setOnClickListener(onClickListener);
        ndphone.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.change )
            {
                if(change.getText().toString().contains("Chỉnh sửa"))
                {
                    pname.setEnabled(true);
                    username.setEnabled(true);
                    pass.setEnabled(true);
                    phone.setEnabled(true);

                    pname.setText(COMMON.currentUser.getUserName());
                    username.setText(COMMON.currentUser.getAddress().toString());
                    pass.setText(COMMON.currentUser.getPass());
                    phone.setText(COMMON.phone);

                    change.setText("Hủy");
                    change.setTextColor(getResources().getColor(R.color.grey_bg));
                }
                else
                {
                    change.setText("Chỉnh sửa");
                    change.setTextColor(getResources().getColor(R.color.peach_bg_1));

                    pname.setEnabled(false);
                    username.setEnabled(false);
                    pass.setEnabled(false);
                    phone.setEnabled(false);

                    pname.setText("");
                    username.setText("");
                    pass.setText("");
                    phone.setText("");
                }
            }


            if(id == R.id.cus_btn)
            {
                USER user = COMMON.currentUser;
                user.ndphone = txt_ndphone.getText().toString();
                user.UserName = pname.getText().toString();
                user.Pass = pass.getText().toString().trim();
                user.Address = username.getText().toString().trim();
                try {
                    FirebaseDatabase.getInstance().getReference()
                            .child("User").child(COMMON.phone).setValue(user);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setMessage("Vui lòng đăng nhập lại")
                            .setCancelable(false)
                            .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(),ActivityWelcome.class));
                                }
                            }).create();
                    dialog.show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            if(id == R.id.cus_ndphone)
            {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View sheetView = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.bottom_sheet,(ViewGroup) view.findViewById(R.id.bottomSheetContainer));

                TextView st = sheetView.findViewById(R.id.sheet_title);
                RecyclerView rv = sheetView.findViewById(R.id.sheet_rv);

                st.setText("DANH BẠ CỦA BẠN");
                getDanhBa (rv,bottomSheetDialog);

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        }
    };

    private void getDanhBa(RecyclerView rv, BottomSheetDialog bottomSheetDialog) {
        if(getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED)
        {
            getActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            getDanhBa(rv, bottomSheetDialog);
        }
        else
        {
            ContentResolver resolver = getActivity().getContentResolver();

            //tạo query select name, id from CONTACT where name like '%' order by id

//        resolver.query(ContactsContract.Contacts.CONTENT_URI, //from
//                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID}, // select
//                "name LIKE ?",new String[]{"%h%"},"id"); //where

            // all
            Cursor cursor =  resolver.query(ContactsContract.Contacts.CONTENT_URI, //from
                    new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID}, // select
                    null,null,null); //where

            List<CONTACT> lst = new ArrayList<>();
            while (cursor.moveToNext())
            {
                CONTACT contact = new CONTACT();
                contact.fullName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(contact.fullName != null)
                    contact.fn = String.valueOf(contact.fullName.charAt(0)).toUpperCase();

                long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor c = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        new String[]{String.valueOf(id)}, null);
                String phone = "", pt ="";
                while (c.moveToNext())
                {
                    phone += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ", ";
                }
                contact.phone = phone;
                contact.type = pt;

                c.close();
                lst.add(contact);
            }
            cursor.close();
            AdapterFavorite adapter = new AdapterFavorite(getContext(), lst, new PhoneItemClickListener() {
                @Override
                public void onContactsItemClick(String phone) {
                    txt_ndphone.setText("Liên lạc tin cây: " + phone);
                    bottomSheetDialog.dismiss();
                }
            });
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }

    }


}