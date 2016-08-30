package com.github.markszabo.wifree;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ConnectToWifi {
    private Context context;
    private String ssid;
    private List<String> psks;
    private int pskid;
    private boolean mStateChangedReceiverRegistered;

    private WifiManager mWifiManager;

    ConnectToWifi(Context context, String ssid) {
        this.context = context;
        this.ssid = ssid;
        this.psks = new ArrayList<>();
        this.mWifiManager = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
    }

    private final BroadcastReceiver mStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState supl_state= intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                switch(supl_state){
                    case COMPLETED:
                        Log.i("", "Connected, the password is " + psks.get(pskid));
                        end();
                        break;
                    default:
                        break;

                }
                int supl_error=intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(supl_error == WifiManager.ERROR_AUTHENTICATING){
                    Log.i("", "Incorrect password, trying next one.");
                    connectToNext();
                }
            }
        }
    };

    public void addPsk(String psk) {
        psks.add(psk);
    }

    public void connectToNext() {
        pskid++;
        if(psks.size() > pskid) {
            String psk = psks.get(pskid);
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", ssid);
            wifiConfig.preSharedKey = String.format("\"%s\"", psk);

            //remember id
            int netId = mWifiManager.addNetwork(wifiConfig);
            mWifiManager.disconnect();
            mWifiManager.enableNetwork(netId, true);
            mWifiManager.reconnect();
        }
    }

    public void start() {
        if(!mStateChangedReceiverRegistered) {
            this.context.registerReceiver(mStateChangedReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
            mStateChangedReceiverRegistered = true;
        }
        this.pskid = -1;

        connectToNext();
    }

    public void end() {
        if(mStateChangedReceiverRegistered) {
            context.unregisterReceiver(mStateChangedReceiver);
        }
    }
}
