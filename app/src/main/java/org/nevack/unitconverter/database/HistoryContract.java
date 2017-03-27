package org.nevack.unitconverter.database;

import android.provider.BaseColumns;

public class HistoryContract {

    private HistoryContract() {}

    /* Inner class that defines the table contents */
    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_UNIT_FROM = "unitfrom";
        public static final String COLUMN_NAME_UNIT_TO = "unitto";
        public static final String COLUMN_NAME_VALUE_TO = "valuefrom";
        public static final String COLUMN_NAME_VALUE_FROM = "valueto";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}