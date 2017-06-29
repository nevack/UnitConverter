package org.nevack.unitconverter.converter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import org.nevack.unitconverter.converter.ConverterContract.ConvertData;
import org.nevack.unitconverter.history.HistoryContract;
import org.nevack.unitconverter.history.HistoryDatabaseHelper;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class ConverterPresenter implements ConverterContract.Presenter,
        LoaderManager.LoaderCallbacks<Converter> {

    private final static String COPY_RESULT = "converter_result";
    private final static int LOADER_CONVERTER = 1;

    private Converter currentConverter;
    private ConvertData data;

    private final ConverterContract.View view;
    private final LoaderManager loaderManager;
    private final ConverterLoader loader;

    private final Context context;
    private final SQLiteDatabase db;

    public ConverterPresenter(Context context, ConverterContract.View view, LoaderManager loaderManager) {
        this.view = view;
        this.context = context;
        this.loaderManager = loaderManager;

        loader = new ConverterLoader(context);

        HistoryDatabaseHelper helper = new HistoryDatabaseHelper(context);
        db = helper.getWritableDatabase();

        view.setPresenter(this);
    }

    @Override
    public void start() {
//        Bundle args = new Bundle();
//        args.putSerializable("KEY", EUnitCategory.AREA);
//        loaderManager.initLoader(LOADER_CONVERTER, null, this);
        loaderManager.initLoader(LOADER_CONVERTER, null, this);
    }

    @Override
    public void setConverter(EUnitCategory category) {
        view.setBackgroundColor(category.getColor());

        //loaderManager.destroyLoader(LOADER_CONVERTER);
        loader.setCategory(category);
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

    @Override
    public Loader<Converter> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Converter> loader, Converter converter) {
        currentConverter = converter;

        view.setUnits(currentConverter.getUnits());

        if (data != null) view.setConvertData(data);
    }

    @Override
    public void onLoaderReset(Loader<Converter> loader) {

    }
}
