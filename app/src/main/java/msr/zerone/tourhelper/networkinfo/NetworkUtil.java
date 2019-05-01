package msr.zerone.tourhelper.networkinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import msr.zerone.tourhelper.R;

public class NetworkUtil {

    public static String getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                return status;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                return status;
            }
        }else{
            status = "No internet is available";
            return status;
        }

        return status;
    }

    public static void isOffline(final Context context, ViewGroup viewGroup){

        viewGroup = viewGroup.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.no_internet_layout, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            alertDialog.setCanceledOnTouchOutside(true);
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                alertDialog.dismiss();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                alertDialog.dismiss();
            }
        }
        else {
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            Button data = dialogView.findViewById(R.id.data);
            Button wifi = dialogView.findViewById(R.id.wifi);
            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    context.startActivity(intent);
                }
            });
            wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                    context.startActivity(intent);
                }
            });

        }
    }
}
