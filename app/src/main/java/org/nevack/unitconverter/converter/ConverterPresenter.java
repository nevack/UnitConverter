package org.nevack.unitconverter.converter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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
        loaderManager.initLoader(LOADER_CONVERTER, null, this);
    }

    @Override
    public void setConverter(EUnitCategory category) {
        view.setBackgroundColor(category.getColor());
        view.setTitle(category.getName());
        loader.setCategory(category);
    }

    @Override
    public void convert(ConvertData data) {
        this.data = data;

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
            String result = currentConverter.convert(data.getValue(), data.getFrom(), data.getTo());
            view.showResult(result);
            this.data = view.getConvertData();
        } catch (ArithmeticException ex) {
            view.showError();
        }
    }

    @Override
    public void copyResultToClipboard(String result) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && !result.isEmpty()) {
            clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, result));
        }
    }

    @Override
    public void pasteFromClipboard() {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard == null) {
            return;
        }

        final ClipData clip = clipboard.getPrimaryClip();

        if (clip == null) {
            return;
        }

        if (clip.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            String pasteData = clipboard.getPrimaryClip().getItemAt(0)
                    .getText().toString();
            if (!pasteData.isEmpty()) {
                try {
                    final double source = Double.parseDouble(pasteData);
                    view.clear();
                    view.appendText(String.valueOf(source));
                } catch (NumberFormatException ignored) {}
            }
        }


    }

    @Override
    public void saveResultToHistory() {
        ContentValues values = new ContentValues();

        int name = EUnitCategory.valueOf(
                currentConverter.getClass().getSimpleName().replace("Converter", "")
                        .toUpperCase().split(" ")[0]
        ).ordinal();

        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_CATEGORY, name);
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_FROM, currentConverter.getUnits().get(data.getFrom()).getName());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_UNIT_TO, currentConverter.getUnits().get(data.getTo()).getName());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_FROM, data.getValue());
        values.put(HistoryContract.HistoryEntry.COLUMN_NAME_VALUE_TO, data.getResult());

        db.insert(HistoryContract.HistoryEntry.TABLE_NAME, null, values);
    }

    @NonNull
    @Override
    public Loader<Converter> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Converter> loader, Converter converter) {
        currentConverter = converter;

        view.setUnits(currentConverter.getUnits());

        if (data != null) {
            view.setConvertData(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Converter> loader) {

    }
}
