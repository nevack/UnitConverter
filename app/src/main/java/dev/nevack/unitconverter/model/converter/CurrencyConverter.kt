package dev.nevack.unitconverter.model.converter

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.nevack.unitconverter.NBRBService
import dev.nevack.unitconverter.model.ConversionUnit
import dev.nevack.unitconverter.model.Rate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.util.Calendar

class CurrencyConverter(context: Context) : Converter() {
    internal var service: NBRBService? = null
    internal var moshi: Moshi? = null
    private val adapter: JsonAdapter<List<Rate>> by lazy(LazyThreadSafetyMode.NONE) {
        requireNotNull(moshi).adapter(TYPE)
    }

    private val file: File = File(context.filesDir, FILE)

    override suspend fun load() {
        registerUnit(BYN)
        if (file.exists()) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = file.lastModified()
            if (Calendar.getInstance()[Calendar.DAY_OF_YEAR] == calendar[Calendar.DAY_OF_YEAR]) {
                loadUnitsFromFile()
            } else {
                loadUnitsFromWeb()
            }
        } else {
            loadUnitsFromWeb()
        }
        sortUnitsWith { a, b -> a.name.compareTo(b.name, ignoreCase = true) }
    }

    private suspend fun loadUnitsFromFile() {
        val rates = withContext(Dispatchers.IO) {
            adapter.fromJson(file.source().buffer())
        }
        rates?.forEach { registerUnit(it.toUnit()) }
    }

    private suspend fun loadUnitsFromWeb() {
        val service = requireNotNull(service) { "Service is not set!" }
        val rates = service.allRatesForToday()
        rates.forEach { registerUnit(it.toUnit()) }
        withContext(Dispatchers.IO) {
            file.sink().buffer().use { sink ->
                sink.writeUtf8(adapter.toJson(rates))
            }
        }
    }

    companion object {
        private val TYPE = Types.newParameterizedType(List::class.java, Rate::class.java)
        private const val FILE = "rates.json"
        private val BYN = ConversionUnit("Белорусский рубль", 1.0, "BYN")
    }
}
