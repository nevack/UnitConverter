package org.nevack.unitconverter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nevack.unitconverter.database.HistoryContract;
import org.nevack.unitconverter.database.HistoryDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final List<HistoryItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        HistoryDatabaseHelper helper = new HistoryDatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                HistoryContract.HistoryEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while(cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_CATEGORY));
            String valuefrom = cursor.getString(cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_FROM));
            String valueto = cursor.getString(cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_TO));
            String unitfrom = cursor.getString(cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_FROM));
            String unitto = cursor.getString(cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_TO));

            items.add(new HistoryItem(category, valuefrom, valueto, unitfrom, unitto));
        }
        cursor.close();

        recyclerView.setAdapter(new HistoryAdapter());
    }

    private class HistoryItem {
        String category;
        String valuefrom;
        String valueto;
        String unitfrom;
        String unitto;

        HistoryItem(String category, String valuefrom, String valueto, String unitfrom, String unitto) {
            this.category = category;
            this.valuefrom = valuefrom;
            this.valueto = valueto;
            this.unitfrom = unitfrom;
            this.unitto = unitto;
        }
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView category;
            private TextView valuefrom;
            private TextView valueto;
            private TextView unitfrom;
            private TextView unitto;

            ViewHolder(View itemView) {
                super(itemView);
                category  = (TextView) itemView.findViewById(R.id.category_name);
                valuefrom = (TextView) itemView.findViewById(R.id.valuefrom);
                valueto  = (TextView) itemView.findViewById(R.id.valueto);
                unitfrom = (TextView) itemView.findViewById(R.id.unitnamefrom);
                unitto = (TextView) itemView.findViewById(R.id.unitnameto);
            }
        }

        HistoryAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.category.setText(items.get(position).category);
            holder.valuefrom.setText(items.get(position).valuefrom);
            holder.valueto.setText(items.get(position).valueto);
            holder.unitfrom.setText(items.get(position).unitfrom);
            holder.unitto.setText(items.get(position).unitto);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
