package org.nevack.unitconverter.converter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.nevack.unitconverter.converter.ConverterContract.ConvertData;
import org.nevack.unitconverter.history.HistoryContract;
import org.nevack.unitconverter.history.HistoryDatabaseHelper;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterPresenter implements ConverterContract.Presenter{

    private static final String COPY_RESULT = "converter_result";

    private Converter currentConverter;
    private ConvertData data;

    private final ConverterContract.View view;

    private final Context context;
    private final SQLiteDatabase db;

    public ConverterPresenter(Context context, ConverterContract.View view) {
        this.view = view;
        this.context = context;

        HistoryDatabaseHelper helper = new HistoryDatabaseHelper(context);
        db = helper.getWritableDatabase();

        view.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void setConverter(EUnitCategory category) {
        currentConverter = category.getConverter(context);
        view.setUnits(currentConverter.getUnits());
        view.setBackgroundColor(category.getColor());

        if (data != null) view.setConvertData(data);
    }

    @Override
    public void convert(ConvertData data) {
        this.data = data;

        String result;
        switch (data.getValue()) {
            case ".":
                view.clear();
                view.appendText("0.");
            case "":
            case "-":
            case "0":
            case "-.":
            case "-0":
            case "-0.":
            case "0.":
                view.showResult("0");
                return;
        }
        try {
            result = currentConverter.convert(data.getValue(), data.getFrom(), data.getTo());
            view.showResult(result);
            this.data = view.getConvertData();
        } catch (ArithmeticException ex) {
            view.showError();
        }
    }

    @Override
    public void copyResultToClipboard(String result) {
        if (!result.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, result));
        }
    }

    @Override
    public void pasteFromClipboard() {
        double source = 0d;
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if ((clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription()
                .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))) {
            String pasteData = clipboard.getPrimaryClip().getItemAt(0)
                    .getText().toString();
            if (!pasteData.isEmpty()) source = Double.parseDouble(pasteData);
        }

        if (source != 0d) {
            view.clear();
            view.appendText(String.valueOf(source));
        }
    }

    @Override
    public void saveResultToHistory() {
        ContentValues values = new ContentValues();
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_CATEGORY, context.getString(currentConverter.getName()));
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_FROM, currentConverter.getUnits().get(data.getFrom()).getName());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_TO, currentConverter.getUnits().get(data.getTo()).getName());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_FROM, data.getValue());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_TO, data.getResult());
        db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values);
    }
}
