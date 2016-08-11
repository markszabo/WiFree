package com.github.markszabo.wifree;

import android.provider.BaseColumns;

public final class CrackListContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public CrackListContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "cracklist";
        public static final String COLUMN_NAME_SSID = "SSID";
        public static final String COLUMN_NAME_BSSID = "BSSID";
        public static final String COLUMN_NAME_SERIAL_NUMBER = "serialnumber";
        public static final String COLUMN_NAME_POSSIBLE_PASSWORD = "possiblepasswords";
    }
}

