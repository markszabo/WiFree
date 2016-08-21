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
    private CrackListDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_list);

        WifiNetwork[] crackList = CrackList.getListFromDb(getApplicationContext());

        String ssids = "";
        for(int i=0; i < crackList.length; i++) {
            ssids += crackList[i].SSID + " - " + crackList[i].BSSID + " + " + crackList[i].possiblePasswords[0] + " + " + crackList[i].possiblePasswords[2] + "\n";
        }

        TextView tvSSIDS = (TextView) findViewById(R.id.tvSSIDS);
        tvSSIDS.setText(ssids);

        Button clearCrackList = (Button) findViewById(R.id.clearCrackList);
        clearCrackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete(CrackListContract.FeedEntry.TABLE_NAME, null, null);
                //reload the activity to clear the list
                finish();
                startActivity(getIntent());
            }
        });
    }
}
