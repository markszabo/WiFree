package com.github.markszabo.wifree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiManager mWifiManager;
    private boolean firstScan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanBtn = (Button) findViewById(R.id.scanWifi);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(firstScan) {
                    mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    registerReceiver(mWifiScanReceiver,
                            new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                }
                mWifiManager.startScan();
            }
        });
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                StringBuilder sb = new StringBuilder();
                List<ScanResult> wifiList = mWifiManager.getScanResults();
                for (int i = 0; i < wifiList.size(); i++){
                    sb.append(new Integer(i+1).toString() + ".");
                    sb.append((wifiList.get(i)).SSID);
                    sb.append("\t");
                    sb.append(wifiList.get(i).BSSID);
                    sb.append("\n");
                }
                TextView mainText = (TextView) findViewById(R.id.mainText);
                mainText.setText(sb);
            }
        }
    };
}
