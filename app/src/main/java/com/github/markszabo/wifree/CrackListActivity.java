package com.github.markszabo.wifree;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class CrackListActivity extends AppCompatActivity {
    private CrackedWifiAdapter adapter;
    private ConnectToWifi con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_list);

        Button clearCrackList = (Button) findViewById(R.id.clearCrackList);
        clearCrackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrackListDbHelper mDbHelper = CrackListDbHelper.getInstance(getApplicationContext()); //create the database

                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                db.delete(CrackListContract.FeedEntry.TABLE_NAME, null, null);
                //reload the activity to clear the list
                finish();
                startActivity(getIntent());
            }
        });

        Button addTestNetwork = (Button) findViewById(R.id.addTestToCrackList);
        addTestNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrackList.addToListInDb(getApplicationContext(), new WifiNetwork("UPC538420", "e4:48:c7:88:7f:58"));
                //reload the activity to clear the list
                finish();
                startActivity(getIntent());
            }
        });

        Button testBtn = (Button) findViewById(R.id.test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Test button",Toast.LENGTH_SHORT).show();
                //do some testing here
                /*//reload the activity to clear the list
                finish();
                startActivity(getIntent());*/
            }
        });

        // Construct the data source
        ArrayList<WifiNetwork>  arrayOfNetworks = new ArrayList<>();
        // Create the adapter to convert the array to views
        adapter = new CrackedWifiAdapter(this, arrayOfNetworks);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvCrackList);
        listView.setAdapter(adapter);
        adapter.addAll(CrackList.getListFromDb(getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiNetwork network = (WifiNetwork) parent.getItemAtPosition(position);
                TextView status = (TextView) view.findViewById(R.id.tvStatus);
                Log.d("","Network clicked:" + String.valueOf(network));
                //start password trial if crack finished
                if(network.serialNumber == 260000001) {
                    Toast.makeText(getApplicationContext(),"Trying passwords for " + network.SSID + ". Please stay in wifi range!",Toast.LENGTH_SHORT).show();
                    Log.d("","Connection trials started for " + network.SSID + " with passwords: " + network.getPossiblePasswordsAsString());
                    con = new ConnectToWifi(getApplicationContext(), network, status);
                    con.end();
                    con.start();
                }
            }
        });

    }
}
