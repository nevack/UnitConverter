package org.nevack.unitconverter.converter

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import org.nevack.unitconverter.model.ConverterCategory
import org.nevack.unitconverter.model.converter.Converter

internal class ConverterLoader(context: Context) : AsyncTaskLoader<Converter>(context) {
    private var converter: Converter? = null
    private var category: ConverterCategory? = null

    override fun loadInBackground(): Converter {
        converter = category!!.getConverter(context)

        // Done!
        return converter!!
    }

    override fun onStartLoading() {
        if (takeContentChanged()) forceLoad()
        if (converter != null) {
            // Use cached data
            deliverResult(converter)
        } else {
            // We have no data, so kick off loading it
            forceLoad()
        }
    }

    override fun deliverResult(converter: Converter?) {
        // We’ll save the data for later retrieval
        this.converter = converter
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(converter)
    }

    fun setCategory(category: ConverterCategory?) {
        this.category = category
        onContentChanged()
    }
}