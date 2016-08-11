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

public class CrackList extends AppCompatActivity {
    private CrackListDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_list);

        if(mDbHelper == null) {
            mDbHelper = new CrackListDbHelper(getApplicationContext()); //create the database
            Toast.makeText(getApplicationContext(), "Database available", Toast.LENGTH_SHORT).show();
        }

        db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CrackListContract.FeedEntry._ID,
                CrackListContract.FeedEntry.COLUMN_NAME_SSID
        };
        String sortOrder =
                CrackListContract.FeedEntry._ID + " ASC";
        Cursor c = db.query(
                CrackListContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        String ssids = "";
        while(!c.isAfterLast()){
            ssids += c.getString(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_SSID)) + "\n";
            c.moveToNext();
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
