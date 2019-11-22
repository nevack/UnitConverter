package org.nevack.unitconverter.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.nevack.unitconverter.history.HistoryContract.HistoryEntry;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ConverterHistory.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                    HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryEntry.COLUMN_NAME_CATEGORY + " INTEGER," +
                    HistoryEntry.COLUMN_NAME_UNIT_FROM + " TEXT," +
                    HistoryEntry.COLUMN_NAME_UNIT_TO + " TEXT," +
                    HistoryEntry.COLUMN_NAME_VALUE_FROM+ " TEXT," +
                    HistoryEntry.COLUMN_NAME_VALUE_TO + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;

    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public List<HistoryItem> getAllItems(SQLiteDatabase db) {
        List<HistoryItem> items = new ArrayList<>();
        Cursor cursor = db.query(
                HistoryEntry.TABLE_NAME,                 // The table to query
                null,                          // The columns to return
                null,                        // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                     // don't group the rows
                null,                     // don't filter by row groups
                null                    // The sort order
        );

        while(cursor.moveToNext()) {
            int category = cursor.getInt(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_CATEGORY));
            String valueFrom = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_VALUE_FROM));
            String valueTo = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_VALUE_TO));
            String unitFrom = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_UNIT_FROM));
            String unitTo = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_UNIT_TO));

            items.add(new HistoryItem(category, valueFrom, valueTo, unitFrom, unitTo));
        }
        cursor.close();

        return items;
    }
}
