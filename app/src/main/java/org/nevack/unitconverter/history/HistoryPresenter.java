package org.nevack.unitconverter.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.nevack.unitconverter.history.HistoryContract.HistoryEntry;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    private final List<HistoryItem> items;

    private final HistoryContract.View view;

    private final Context context;
    private HistoryDatabaseHelper helper;
    private SQLiteDatabase db;

    public HistoryPresenter(Context context, HistoryContract.View view) {
        this.view = view;
        this.context = context;

        this.view.setPresenter(this);

        items = new ArrayList<>();

        helper = new HistoryDatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public void start() {
        fetch();
    }

    private void fetch() {
        items.clear();
        Cursor cursor = db.query(
                HistoryEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while(cursor.moveToNext()) {
            int category = cursor.getInt(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_CATEGORY));
            String valuefrom = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_VALUE_FROM));
            String valueto = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_VALUE_TO));
            String unitfrom = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_UNIT_FROM));
            String unitto = cursor.getString(cursor.getColumnIndex(HistoryEntry.COLUMN_NAME_UNIT_TO));

            items.add(new HistoryItem(category, valuefrom, valueto, unitfrom, unitto));
        }
        cursor.close();

        if (items.isEmpty()) {
            view.showNoItems();
            return;
        }

        view.showHistoryItems(items);
    }

    @Override
    public void clearItems() {
        //remove all entries
        db.delete(HistoryEntry.TABLE_NAME, null, null);
        view.showNoItems();
    }

    @Override
    public void removeItem(HistoryItem item) {
        String selection = HistoryEntry.COLUMN_NAME_VALUE_FROM + " = ? AND "
                + HistoryEntry.COLUMN_NAME_VALUE_TO + " = ?";
        String[] selectionArgs = { item.getValuefrom(), item.getValueto() };
        db.delete(HistoryEntry.TABLE_NAME, selection, selectionArgs);
    }
}
