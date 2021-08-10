package com.example.quanlysieuthi;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BroadcastReceiverConnect extends BroadcastReceiver {

    AlertDialog dialog;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try {
            if (isOnline(context)) {
                if(dialog != null)
                    dialog.dismiss();
//                Toast.makeText(context, "Internet Connect", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View layout = LayoutInflater.from(context).inflate(R.layout.dialog_disconnect, null);
                builder.setView(layout);

                Button btn = layout.findViewById(R.id.button2);

                dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);

                dialog.getWindow().setGravity(Gravity.CENTER);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onReceive(context, intent);
                    }
                });
//                Toast.makeText(context, "Internet Disconnect", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}