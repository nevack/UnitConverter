package org.nevack.unitconverter.converter;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

class ConverterLoader extends AsyncTaskLoader<Converter> {

    private Converter converter;
    private EUnitCategory category;

    public ConverterLoader(Context context) {
        super(context);
    }

    @Override
    public Converter loadInBackground() {

        converter = category.getConverter(getContext());

        // Done!
        return converter;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();

        if (converter != null) {
            // Use cached data
            deliverResult(converter);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Converter converter) {
        // We’ll save the data for later retrieval
        this.converter = converter;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(converter);
    }

    void setCategory(EUnitCategory category) {
        this.category = category;
        onContentChanged();
    }
}
