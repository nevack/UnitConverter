package org.nevack.unitconverter.converter

import android.content.*
import android.os.Bundle
import androidx.core.content.getSystemService
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.nevack.unitconverter.NBRBService
import org.nevack.unitconverter.converter.ConverterContract.ConvertData
import org.nevack.unitconverter.history.db.HistoryDao
import org.nevack.unitconverter.history.db.HistoryItem
import org.nevack.unitconverter.model.EUnitCategory
import org.nevack.unitconverter.model.converter.Converter
import org.nevack.unitconverter.model.converter.CurrencyConverter
import java.util.*

class ConverterPresenter(
    private val context: Context,
    private val view: ConverterContract.View,
    private val loaderManager: LoaderManager,
    private val db: HistoryDao,
    private val moshi: Moshi,
    private val service: NBRBService,
) : ConverterContract.Presenter, LoaderManager.LoaderCallbacks<Converter> {
    private var currentConverter: Converter? = null
    private var data: ConvertData? = null
    private val loader: ConverterLoader = ConverterLoader(context)
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun start() {
        loaderManager.initLoader(LOADER_CONVERTER, null, this)
    }

    override fun setConverter(category: EUnitCategory) {
        view.setBackgroundColor(category.color)
        view.setTitle(category.getName())
        loader.setCategory(category)
    }

    override fun convert(data: ConvertData) {
        this.data = data
        when (data.value) {
            "." -> {
                view.clear()
                view.appendText("0.")
                view.showResult("0")
                return
            }
            "", "-", "0", "-.", "-0", "-0.", "0." -> {
                view.showResult("0")
                return
            }
        }
        try {
            val result = currentConverter!!.convert(data.value, data.from, data.to)
            view.showResult(result)
            this.data = view.convertData
        } catch (ex: ArithmeticException) {
            view.showError()
        }
    }

    override fun copyResultToClipboard(result: String) {
        if (result.isNotEmpty()) {
            val clipboard = context.getSystemService<ClipboardManager>() ?: return
            clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, result))
        }
    }

    override fun pasteFromClipboard() {
        val clipboard = context.getSystemService<ClipboardManager>() ?: return
        val clip = clipboard.primaryClip ?: return
        if (clip.description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            val pasteData = clipboard.primaryClip!!.getItemAt(0)
                .text.toString()
            if (pasteData.isNotEmpty()) {
                try {
                    val source = pasteData.toDouble()
                    view.clear()
                    view.appendText(source.toString())
                } catch (ignored: NumberFormatException) {
                }
            }
        }
    }

    override fun saveResultToHistory() {
        val name = EUnitCategory.valueOf(
            currentConverter!!.javaClass.simpleName.replace("Converter", "")
                .toUpperCase(Locale.ROOT).split(" ").toTypedArray()[0]
        ).ordinal
        val item = HistoryItem(
            -1,
            currentConverter!!.units[data!!.from].name,
            currentConverter!!.units[data!!.to].name,
            data!!.value,
            data!!.result,
            name
        )
        scope.launch(Dispatchers.IO) {
            db.insertAll(item)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Converter> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<Converter>, converter: Converter) {
        if (converter is CurrencyConverter) {
            converter.service = service
            converter.moshi = moshi
        }
        currentConverter = converter
        view.setUnits(currentConverter!!.units)
        if (data != null) {
            view.convertData = data
        }
    }

    override fun onLoaderReset(loader: Loader<Converter>) {}

    companion object {
        private const val COPY_RESULT = "converter_result"
        private const val LOADER_CONVERTER = 1
    }

    init {
        view.setPresenter(this)
    }
}