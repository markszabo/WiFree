package com.github.markszabo.wifree;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CrackListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_list);

        WifiNetwork[] crackList = CrackList.getListFromDb(getApplicationContext());

        String ssids = "";
        for(int i=0; i < crackList.length; i++) {
            ssids += crackList[i].SSID + " - " + crackList[i].BSSID + " + " + crackList[i].getPossiblePasswordsAsString() +
                    " + " + crackList[i].serialNumber + "\n";
        }

        TextView tvSSIDS = (TextView) findViewById(R.id.tvSSIDS);
        tvSSIDS.setText(ssids);

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
                CrackList.addToListInDb(getApplicationContext(), new WifiNetwork("538420", "e4:48:c7:88:7f:58"));
                //reload the activity to clear the list
                finish();
                startActivity(getIntent());
            }
        });

        Button testBtn = (Button) findViewById(R.id.test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrackList.updateListInDb(getApplicationContext(), "e4:48:c7:88:7f:58", 200000000, "testpass1;testpass2;");
                //reload the activity to clear the list
                finish();
                startActivity(getIntent());
            }
        });
    }
}
