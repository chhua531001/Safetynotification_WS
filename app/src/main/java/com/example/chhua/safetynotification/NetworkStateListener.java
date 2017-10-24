package com.example.chhua.safetynotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by user on 2017/10/19.
 */

public class NetworkStateListener extends BroadcastReceiver {

    Tools tools = new Tools();
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Receive Change.");
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Toast.makeText(context, "AAWifi Connected!", Toast.LENGTH_SHORT).show();
            System.out.println("Receive Change Connected.");
        } else {
            Toast.makeText(context, "AAWifi Not Connected!", Toast.LENGTH_SHORT).show();
            System.out.println("Receive Change Not Connected.");
            wifi.setWifiEnabled(true);
//            tools.delayMS(10000);
        }
    }
}
