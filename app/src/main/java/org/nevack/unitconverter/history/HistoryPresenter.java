package org.nevack.unitconverter.history;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import org.nevack.unitconverter.history.HistoryContract.HistoryEntry;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    private final List<HistoryItem> items;

    private final HistoryContract.View view;

    private final Context context;
    private final HistoryDatabaseHelper helper;
    private final SQLiteDatabase db;

    HistoryPresenter(Context context, HistoryContract.View view) {
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

    @Override
    public void filterItems(int mask) {
        List<HistoryItem> items = new ArrayList<>();
        for (HistoryItem item : this.items) {
            if (item.getCategory() == mask) items.add(item);
        }

        if (items.isEmpty()) {
            view.showNoItems();
            return;
        }

        view.showHistoryItems(items);
    }

    @Override
    public void shareItem(HistoryItem item) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        String message = String.format("%s %s = %s %s",
                item.getValueFrom(),
                item.getUnitFrom(),
                item.getValueTo(),
                item.getUnitTo()
        );

        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        context.startActivity(sendIntent);
    }

    private void fetch() {
        items.clear();
        items.addAll(helper.getAllItems(db));

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
        String[] selectionArgs = { item.getValueFrom(), item.getValueTo() };
        db.delete(HistoryEntry.TABLE_NAME, selection, selectionArgs);
    }
}
