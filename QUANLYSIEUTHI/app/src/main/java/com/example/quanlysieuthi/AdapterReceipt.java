package com.example.quanlysieuthi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterReceipt extends RecyclerView.Adapter<AdapterReceipt.VIEWHOLDER> {

    Context context;
    ArrayList<ORDER> orders;
    ArrayList<String> maDonHangs;


    public AdapterReceipt(Context context, ArrayList<ORDER> orders, ArrayList<String> maDonHangs) {
        this.context = context;
        this.orders = orders;
        this.maDonHangs = maDonHangs;
    }



    @NonNull
    @Override
    public VIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt,parent,false);
        return new VIEWHOLDER(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEWHOLDER holder, int position) {
        ORDER ord = orders.get(position);
        Calendar calendar = Calendar.getInstance();
        String currentDay = new SimpleDateFormat("dd MMM, yyyy")
                .format(calendar.getTime());

        holder.pttp.setText("Thanh toán bằng hình thức: " + ord.getOrd_payment());
        holder.tgnh.setText("Nhận hàng vào ngày: "+ ord.getOrd_reiceve());
        holder.state.setText(ord.getOrd_state());
        if(!ord.getOrd_reiceve().equals("no"))
        {
            holder.state.setTextColor(context.getResources().getColor(R.color.purple_700));
            holder.btn_huy.setEnabled(true);
            holder.btn_huy.setText("In");

        }
//        }
//        else
//            holder.state.setText("Đã giao hàng");

        //user
        holder.tkh.setText(COMMON.currentUser.getUserName());
        holder.sdt.setText(COMMON.phone);
        holder.dchi.setText(ord.getOrd_address());
        holder.madonhang.setText(maDonHangs.get(position));
        holder.tgd.setText(ord.getOrd_date() + " " + ord.getOrd_time());
        holder.tongcong.setText(String.format("%,.0f",Double.parseDouble(ord.getOrd_total())) + " đ");
        holder.giamgia.setText(String.format("%,.0f",Double.parseDouble(ord.getOrd_discount())) + " đ");
        holder.ship.setText(String.format("%,.0f",Double.parseDouble(ord.getOrd_delivery())) + " đ");

        double tongtt = Double.parseDouble(ord.getOrd_total()) - Double.parseDouble(ord.getOrd_discount())
                - Double.parseDouble(ord.getOrd_delivery());

        holder.tongtien.setText(String.format("%,.0f",tongtt) + " đ");
        //rv
        String [] productName = ord.getOrd_product().split("/");
        String [] productQty = ord.getOrd_quality().split(",");
        String [] productImg= ord.getOrd_img().split(",");
        //
        //
        List<PRODUCT> prds = new ArrayList<>();

        for (int i = 0 ; i < productName.length; i++)
        {
            PRODUCT p = new PRODUCT();
            p.setPrd_ten(productName[i]);
            p.setPrd_soton(productQty[i]);
            p.setPrd_hinh(productImg[i]);
            prds.add(p);
        }
        holder.rv_cart.setAdapter(new AdapterCash(context, prds,1));
        holder.rv_cart.setLayoutManager(new LinearLayoutManager(context));

        holder.btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isEnabled() == true && holder.btn_huy.getText().toString().contains("Hủy"))
                {
                    xoaHoaDon(maDonHangs.get(position), holder);
                }
                else if(v.isEnabled() == true && holder.btn_huy.getText().toString().contains("In"))
                {
                    xuatHoaDon(maDonHangs.get(position), holder);
                }
                else
                {
                    muaLaiDonHang(productName, productQty);
                }
            }
        });
    }

    private void xuatHoaDon(String mahoadon, VIEWHOLDER holder) {
        try {
            createPDF(holder.madonhang.getText().toString(),holder.tgd.getText().toString(), holder.tkh.getText().toString(), holder.sdt.getText().toString(), holder.tongcong.getText().toString()
            ,holder.tongtien.getText().toString(), holder.giamgia.getText().toString(), holder.dchi.getText().toString(),holder.pttp.getText().toString());
        }catch (Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void createPDF(String mahd, String ngaydat, String tenkhac, String sdt, String tongcong, String tongtien, String giamgia, String dchi, String pt) {
//        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        File file = new File(pdfPath, "myPDF.pdf");
        try {
//            OutputStream outputStream = new FileOutputStream(file);
//
//            PdfWriter writer = new PdfWriter(file);
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument);
//            float colWidth [] = {140,140,140,140};
//            Table table = new Table(colWidth);
//
//            table.addCell(new Cell(3,1).add(new Paragraph("")));
//            table.addCell(new Cell().add(new Paragraph("")));
//            table.addCell(new Cell(1,2).add(new Paragraph("Hóa Đơn Bán Lẻ")));
////            table.addCell(new Cell().add(new Paragraph("")));
//
//            table.addCell(new )

            Paint forLinePaint = new Paint();
            android.graphics.pdf.PdfDocument myPdfDoc = new android.graphics.pdf.PdfDocument();
            Paint paint = new Paint();
            android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(250,350,1).create();
            android.graphics.pdf.PdfDocument.Page  myInfo = myPdfDoc.startPage(pageInfo);

            Canvas canvas = myInfo.getCanvas();

            paint.setTextSize(7.5f);
            paint.setColor(Color.rgb(0,50,250));
            canvas.drawText("Trung Tam Mua Sam Sieu Thi YVONNE", 20, 20,paint);

            paint.setTextSize(15.5f);
            paint.setColor(Color.rgb(0,50,250));
            canvas.drawText("Hoa Don Ban Le", 20, 20,paint);
            paint.setTextSize(8.5f);
            canvas.drawText("No: " +mahd , 20, 40,paint);
            canvas.drawText("Ngay Lap: " + ngaydat  , 20, 55,paint);

            forLinePaint.setStyle(Paint.Style.STROKE);
            forLinePaint.setPathEffect(new DashPathEffect(new float[] {5,5}, 0));
            forLinePaint.setStrokeWidth(2);
            canvas.drawLine(20,65,230,65, forLinePaint);

            canvas.drawText("Ten Khach hang: "+ tenkhac, 20,80,paint);
            canvas.drawText("Dia chi: " + dchi, 20, 90,paint );
            canvas.drawLine(20,100,230,100,forLinePaint);

            canvas.drawText("Thanh Toan: ", 20, 115,paint );


            canvas.drawText("Tong cong: ", 20, 135,paint );
            canvas.drawText(tongcong, 140, 135,paint );
            canvas.drawText("Giam Gia: ", 20, 145,paint );
            canvas.drawText(giamgia , 140, 145,paint );

            paint.setTextSize(10f);
            canvas.drawText("Thanh Tien : ", 20, 165,paint );
            canvas.drawText(tongcong , 140, 165,paint );

            canvas.drawLine(20,175,230,175,forLinePaint);

            canvas.drawText("PTTT : ", 20, 185,paint );
            canvas.drawText( pt, 140, 185,paint );

            canvas.drawText("Tran Trong Cam Ơn", canvas.getWidth() /2, 230, paint);

            myPdfDoc.finishPage(myInfo);
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File file = new File(path,"HoaDon.pdf");

            try {
                myPdfDoc.writeTo(new FileOutputStream(file));
                Toast.makeText(context,"PDF",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            myPdfDoc.close();
//            Paragraph paragraph = new Paragraph("Helllo");

//            document.add(paragraph);
//            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void muaLaiDonHang(String[] productName, String[] productQty) {
        List<PRODUCT> productList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Product")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot item : snapshot.getChildren())
                            {
                                PRODUCT p = item.getValue(PRODUCT.class);
                                for(int pos = 0; pos < productName.length; pos ++)
                                {
                                    if(p.getPrd_ten().equals(productName[pos]))
                                    {
                                        p.setPrd_soton(productQty[pos]);
                                        productList.add(p);
                                    }
                                }
                                if(productList.size() == productName.length)
                                    break;
                            }
                            addToCart(productList);
                            AlertDialog alertDialog = new AlertDialog.Builder(context)
                                    .setTitle("Mua lại")
                                    .setMessage("Đã thêm vào giỏ hàng. Vui lòng kiểm tra")
                                    .setPositiveButton("OK", (dialog2, which) ->{
                                        context.startActivity(new Intent(context, ActivityHomePage.class));
                                        ((Activity) context).finish();
                                    }).create();
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addToCart(List<PRODUCT> productList) {
        String currentTime, currentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM ,yyyy");
        currentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        currentTime = timeFormat.format(calendar.getTime());
        for (PRODUCT product : productList)
        {
            int sl = Integer.parseInt(product.getPrd_soton());
            Float total = Float.parseFloat(product.getPrd_dongia()) * sl;

            CART c = new CART(product.getPrd_ten(),product.getPrd_hinh(),product.getPrd_gia(),
                    currentDate,currentTime,product.getPrd_dongia(),sl,total);
            FirebaseDatabase.getInstance().getReference("Cart").child(COMMON.phone)
                    .child(product.getPrd_ten()).setValue(c);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void xoaHoaDon(String s, VIEWHOLDER holder) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Xóa đơn hàng")
                .setMessage("Xóa đơn hàng " + s + "?")
                .setNegativeButton("HỦY", ((dialog, which) -> dialog.dismiss()))
                .setPositiveButton("XÓA", (dialog2, which) ->
                {
                   try { FirebaseDatabase.getInstance().getReference("Orders")
                            .child(s).removeValue();
                    Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                       holder.state.setText("Đã hủy");
                       holder.state.setTextColor(context.getResources().getColor(R.color.red));
                       holder.btn_huy.setText("Mua lại");
                       holder.btn_huy.setBackgroundResource(R.drawable.gradient_background);
                   }
                   catch (Exception e)
                   {
                   }
                }).create();
        alertDialog.show();
    }

    public class VIEWHOLDER extends RecyclerView.ViewHolder {
        private TextView state, pttp, tgnh, tkh, sdt, dchi, madonhang, tgd, tongtien, giamgia, ship, tongcong;
        private RecyclerView rv_cart;
        private Button btn_huy;

        public VIEWHOLDER(View v) {
            super(v);
            state = v.findViewById(R.id.rc_itemTitle);
            pttp = v.findViewById(R.id.rc_pttt);
            tgnh = v.findViewById(R.id.rc_tgnh);
            tkh = v.findViewById(R.id.rc_CusName);
            sdt = v.findViewById(R.id.rc_CusPhone);
            dchi = v.findViewById(R.id.rc_CusAddress);
            madonhang = v.findViewById(R.id.rc_madonhang);
            tgd = v.findViewById(R.id.rc_thoigian);
            tongcong = v.findViewById(R.id.txt_tongcong);
            tongtien = v.findViewById(R.id.txt_tongtienhangtt);
            giamgia = v.findViewById(R.id.txt_giamgia);
            ship = v.findViewById(R.id.txt_phigiaohang);
            rv_cart = v.findViewById(R.id.rc_recyclerView);
            btn_huy = v.findViewById(R.id.btn_huy);
            //init
        }
    }
}
