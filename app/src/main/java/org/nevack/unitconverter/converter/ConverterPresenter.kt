package org.nevack.unitconverter.converter

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.nevack.unitconverter.converter.ConverterContract.ConvertData
import org.nevack.unitconverter.history.db.HistoryDao
import org.nevack.unitconverter.history.db.HistoryItem
import org.nevack.unitconverter.model.ConverterCategory
import org.nevack.unitconverter.model.converter.Converter
import java.util.*

class ConverterPresenter(
    private val context: Context,
    private val view: ConverterContract.View,
    private val db: HistoryDao,
) : ConverterContract.Presenter {
    private lateinit var currentConverter: Converter
    private var data: ConvertData? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

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
            val result = currentConverter.convert(data.value, data.from, data.to)
            view.showResult(result)
            this.data = view.getConvertData()
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
        val name = ConverterCategory.valueOf(
            currentConverter.javaClass.simpleName.replace("Converter", "")
                .toUpperCase(Locale.ROOT).split(" ").toTypedArray()[0]
        ).ordinal
        val item = HistoryItem(
            0,
            currentConverter[data!!.from].name,
            currentConverter[data!!.to].name,
            data!!.value,
            data!!.result,
            name
        )
        scope.launch(Dispatchers.IO) {
            db.insertAll(item)
        }
    }

//    override fun onLoadFinished(loader: Loader<Converter>, converter: Converter) {
//        currentConverter = converter
//        if (converter is CurrencyConverter) {
//            converter.service = service
//            converter.moshi = moshi
//            scope.launch(Dispatchers.IO) {
//                converter.load()
//                withContext(Dispatchers.Main) {
//                    view.setUnits(currentConverter.conversionUnits)
//                }
//            }
//        } else {
//            view.setUnits(currentConverter.conversionUnits)
//        }
//        if (data != null) {
//            view.convertData = data
//        }
//    }

    companion object {
        private const val COPY_RESULT = "converter_result"
    }
}