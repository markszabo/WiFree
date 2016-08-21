package com.github.markszabo.wifree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CrackList {
    private static CrackListDbHelper mDbHelper;
    private static SQLiteDatabase dbReadable;
    private static SQLiteDatabase dbWritable;


    public static WifiNetwork[] getListFromDb(Context context){
        if(mDbHelper == null) {
            mDbHelper = new CrackListDbHelper(context); //create the database
        }

        if(dbReadable == null) {
            dbReadable = mDbHelper.getReadableDatabase();
        }
        String[] projection = {
                CrackListContract.FeedEntry._ID,
                CrackListContract.FeedEntry.COLUMN_NAME_SSID,
                CrackListContract.FeedEntry.COLUMN_NAME_BSSID,
                CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER,
                CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD
        };
        String sortOrder =
                CrackListContract.FeedEntry._ID + " ASC";
        Cursor c = dbReadable.query(
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
                            new ArrayList<String>(Arrays.asList(c.getString(c.getColumnIndexOrThrow(CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD)).split(";"))));
            c.moveToNext();
        }
        return ret;
    }

    public static void updateListInDb(Context context, String BSSID, int serialNumber, String possiblePasswords) {
        if(mDbHelper == null) {
            mDbHelper = new CrackListDbHelper(context);
        }

        if(dbWritable == null) {
            dbWritable = mDbHelper.getWritableDatabase();
        }
        String sql = "UPDATE " + CrackListContract.FeedEntry.TABLE_NAME +
                " SET " + CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER + "=" + String.valueOf(serialNumber) + ", " +
                CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD + "=\"" + possiblePasswords +
                "\" WHERE " + CrackListContract.FeedEntry.COLUMN_NAME_BSSID + "=\"" + BSSID + "\"";
        Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();
        dbWritable.execSQL(sql);
    }

    public static void addToListInDb(Context context, WifiNetwork network) {
        if(mDbHelper == null) {
            mDbHelper = new CrackListDbHelper(context);
        }
        if(dbWritable == null) {
            dbWritable = mDbHelper.getWritableDatabase();
        }
        //create new record
        ContentValues values = new ContentValues();
        values.put(CrackListContract.FeedEntry.COLUMN_NAME_SSID, network.SSID);
        values.put(CrackListContract.FeedEntry.COLUMN_NAME_BSSID, network.BSSID);

        if(network.serialNumber == 0) {
            values.put(CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER, 200000000); //start serial number from cisco_psk.py
        } else {
            values.put(CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER, network.serialNumber);
        }

        if(network.possiblePasswords == null) {
            values.put(CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD, "nulladik;elso;masodik");
        } else {
            values.put(CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD, network.getPossiblePasswordsAsString());
        }

        //insert record
        dbWritable.insert(
                CrackListContract.FeedEntry.TABLE_NAME,
                null, //no field can be null
                values);

    }
}
