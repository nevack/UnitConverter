package org.nevack.unitconverter.history;

import android.provider.BaseColumns;

import org.nevack.unitconverter.BasePresenter;
import org.nevack.unitconverter.BaseView;
import org.nevack.unitconverter.model.HistoryItem;

import java.util.List;

public interface HistoryContract {

    interface View extends BaseView<HistoryContract.Presenter> {

        void showHistoryItems(List<HistoryItem> items);

        void showNoItems();

    }

    interface Presenter extends BasePresenter {

        void clearItems();

        void removeItem(HistoryItem item);

        void filterItems(int mask);

        void shareItem(HistoryItem item);

    }

    /* Inner class that defines the table contents */
    class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_UNIT_FROM = "unit_from";
        public static final String COLUMN_NAME_UNIT_TO = "unit_to";
        public static final String COLUMN_NAME_VALUE_TO = "value_to";
        public static final String COLUMN_NAME_VALUE_FROM = "value_from";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

}