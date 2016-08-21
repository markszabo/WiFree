package com.github.markszabo.wifree;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public final class CrackList {

    public static WifiNetwork[] getListFromDb(Context context){
        CrackListDbHelper mDbHelper = new CrackListDbHelper(context); //create the database

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                CrackListContract.FeedEntry._ID,
                CrackListContract.FeedEntry.COLUMN_NAME_SSID,
                CrackListContract.FeedEntry.COLUMN_NAME_BSSID,
                CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER,
                CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD
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
        WifiNetwork[] ret = new WifiNetwork[c.getCount()];
        while(!c.isAfterLast()){
            ret[c.getPosition()] =
                    new WifiNetwork(c.getString(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_SSID)),
                            c.getString(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_BSSID)),
                            c.getInt(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER)),
                            c.getString(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD)).split(";"));
            c.moveToNext();
        }
        return ret;
    }

    public static void updateListInDb(Context context, String BSSID, int serialNumber, String possiblePasswords) {
        CrackListDbHelper mDbHelper = new CrackListDbHelper(context);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "UPDATE " + CrackListContract.FeedEntry.TABLE_NAME +
                " SET " + CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER + "=" + String.valueOf(serialNumber) + ", " +
                CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD + "=\"" + possiblePasswords +
                "\" WHERE " + CrackListContract.FeedEntry.COLUMN_NAME_BSSID + "=\"" + BSSID + "\"";
        Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
    }
}
