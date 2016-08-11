package com.github.markszabo.wifree;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrackListDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CrackListContract.FeedEntry.TABLE_NAME + " (" +
                    CrackListContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    CrackListContract.FeedEntry.COLUMN_NAME_SSID + TEXT_TYPE + COMMA_SEP +
                    CrackListContract.FeedEntry.COLUMN_NAME_BSSID + TEXT_TYPE + COMMA_SEP +
                    CrackListContract.FeedEntry.COLUMN_NAME_SERIAL_NUMBER + INT_TYPE + COMMA_SEP +
                    CrackListContract.FeedEntry.COLUMN_NAME_POSSIBLE_PASSWORD + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CrackListContract.FeedEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CrackList.db";

    public CrackListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database probably won't change anyway
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}