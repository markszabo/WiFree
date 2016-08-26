package com.github.markszabo.wifree;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiManager mWifiManager;
    private boolean WifiScanReceiverRegistered = false;

    private ArrayList<WifiNetwork> arrayOfNetworks;
    private WifiNetworkAdapter adapter;
    private CrackListDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mDbHelper == null) {
            mDbHelper = CrackListDbHelper.getInstance(getApplicationContext()); //create the database
            Toast.makeText(getApplicationContext(), "Database available", Toast.LENGTH_SHORT).show();
        }

        Button scanBtn = (Button) findViewById(R.id.scanWifi);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!WifiScanReceiverRegistered) {
                    mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    registerReceiver(mWifiScanReceiver,
                            new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                }
                WifiScanReceiverRegistered = true;
                mWifiManager.startScan();
            }
        });

        Button goCrackListBtn = (Button) findViewById(R.id.goToCrackList);
        goCrackListBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, CrackListActivity.class);
                MainActivity.this.

                startActivity(myIntent);
            }
        });

        Button startCrackService = (Button) findViewById(R.id.startCrackService);
        startCrackService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CrackService.startCrackService(MainActivity.this);
            }
        });

        // Construct the data source
        arrayOfNetworks = new ArrayList<WifiNetwork>();
        // Create the adapter to convert the array to views
        adapter = new WifiNetworkAdapter(this, arrayOfNetworks);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvWifiNetworks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiNetwork network = (WifiNetwork) parent.getItemAtPosition(position);
                CrackList.addToListInDb(getApplicationContext(), network);
                Toast.makeText(getApplicationContext(), network.SSID + " added to the cracklist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(WifiScanReceiverRegistered) {
            unregisterReceiver(mWifiScanReceiver);
        }
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> wifiList = mWifiManager.getScanResults();
                adapter.clear(); //remove previous scan results
                for (int i = 0; i < wifiList.size(); i++){
                    WifiNetwork newNetwork = new WifiNetwork((wifiList.get(i)).SSID, (wifiList.get(i)).BSSID);
                    adapter.add(newNetwork);
                }
            }
        }
    };
}
