package dev.nevack.unitconverter.model.nbrb

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.nevack.unitconverter.NBRBService
import dev.nevack.unitconverter.model.ConversionUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Locale

class NBRBRepository(
    private val locale: Locale,
    fileProvider: (String) -> File,
    private var service: NBRBService,
    moshi: Moshi,
) {
    private val currenciesFile = fileProvider("currencies.json")
    private val ratesFile = fileProvider("rates.json")
    private val ratesAdapter: JsonAdapter<List<NBRBRate>> = moshi.adapter(RATES_TYPE)
    private val currenciesAdapter: JsonAdapter<List<NBRBCurrency>> = moshi.adapter(CURRENCIES_TYPE)

    suspend fun getUnits(): List<ConversionUnit> = withContext(Dispatchers.IO) {
        val currenciesAsync = async {
            loadWithCache(currenciesAdapter, currenciesFile) { allCurrencies() }
        }
        val ratesAsync = async {
            loadWithCache(ratesAdapter, ratesFile) { allRatesForToday() }
        }
        val currencies = currenciesAsync.await().associateBy { it.curID }
        val rates = ratesAsync.await()
        rates.filter { currencies.containsKey(it.curID) }
            .map { it.toUnitLocalized(currencies[it.curID]!!.getLocalizedName(locale)) }
    }

    private suspend fun <T> loadWithCache(
        adapter: JsonAdapter<T>,
        file: File,
        block: suspend NBRBService.() -> T,
    ): T = withContext(Dispatchers.IO) {
        if (file.exists()) {
            val calendar = Calendar.getInstance().apply { timeInMillis = file.lastModified() }
            if (Calendar.getInstance()[Calendar.DAY_OF_YEAR] == calendar[Calendar.DAY_OF_YEAR]) {
                val cached = adapter.load(file)
                if (cached != null) {
                    return@withContext cached
                }
            }
        }
        loadFromWeb(adapter, file, block)
    }

    private suspend fun <T> loadFromWeb(
        adapter: JsonAdapter<T>,
        cache: File,
        block: suspend NBRBService.() -> T,
    ): T = withContext(Dispatchers.IO) {
        val result = service.block()
        adapter.save(result to cache)
        result
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun <T> JsonAdapter<T>.save(what: Pair<T, File>) = withContext(Dispatchers.IO) {
        try {
            what.second.sink().buffer().use { sink -> toJson(sink, what.first) }
        } catch (ignored: IOException) {
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun <T> JsonAdapter<T>.load(from: File): T? = withContext(Dispatchers.IO) {
        try {
            from.source().buffer().use { source -> fromJson(source) }
        } catch (e: IOException) {
            null
        }
    }

    companion object {
        private val RATES_TYPE = Types.newParameterizedType(List::class.java, NBRBRate::class.java)
        private val CURRENCIES_TYPE =
            Types.newParameterizedType(List::class.java, NBRBCurrency::class.java)
    }
}
