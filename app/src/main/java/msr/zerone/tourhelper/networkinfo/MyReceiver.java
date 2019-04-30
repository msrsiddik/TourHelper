package msr.zerone.tourhelper.networkinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import msr.zerone.tourhelper.FragmentInter;


public class MyReceiver extends BroadcastReceiver {
    public static FragmentInter networkListenerStatus;
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.isEmpty()){
            status="No Internet Connection";
        }
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        networkListenerStatus = (FragmentInter) context;
        networkListenerStatus.onNetworkConnectionChanged(status);
    }
}


