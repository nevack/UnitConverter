package dev.nevack.unitconverter.nbrb

import dev.nevack.unitconverter.model.ConversionUnit
import dev.nevack.unitconverter.model.CurrencyUnitsRepository
import dev.nevack.unitconverter.nbrb.model.NBRBCurrency
import dev.nevack.unitconverter.nbrb.model.NBRBRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Locale

class NBRBRepository(
    private val locale: Locale,
    fileProvider: (String) -> File,
    private val service: NBRBService,
    private val json: Json,
) : CurrencyUnitsRepository {
    private val currenciesFile = fileProvider("currencies.json")
    private val ratesFile = fileProvider("rates.json")
    private val ratesSerializer = ListSerializer(NBRBRate.serializer())
    private val currenciesSerializer = ListSerializer(NBRBCurrency.serializer())

    override suspend fun getUnits(): List<ConversionUnit> =
        withContext(Dispatchers.IO) {
            val currenciesAsync =
                async {
                    loadWithCache(currenciesSerializer, currenciesFile) { allCurrencies() }
                }
            val ratesAsync =
                async {
                    loadWithCache(ratesSerializer, ratesFile) { allRatesForToday() }
                }
            val currencies = currenciesAsync.await().associateBy { it.curID }
            ratesAsync
                .await()
                .filter { currencies.containsKey(it.curID) }
                .map { it.toUnitLocalized(currencies[it.curID]!!.getLocalizedName(locale)) }
        }

    private suspend fun <T> loadWithCache(
        serializer: KSerializer<T>,
        file: File,
        block: suspend NBRBService.() -> T,
    ): T =
        withContext(Dispatchers.IO) {
            if (file.exists()) {
                val calendar = Calendar.getInstance().apply { timeInMillis = file.lastModified() }
                val today = Calendar.getInstance()
                if (today[Calendar.YEAR] == calendar[Calendar.YEAR] &&
                    today[Calendar.DAY_OF_YEAR] == calendar[Calendar.DAY_OF_YEAR]
                ) {
                    val cached = serializer.load(file)
                    if (cached != null) {
                        return@withContext cached
                    }
                }
            }
            loadFromWeb(serializer, file, block)
        }

    private suspend fun <T> loadFromWeb(
        serializer: KSerializer<T>,
        cache: File,
        block: suspend NBRBService.() -> T,
    ): T =
        withContext(Dispatchers.IO) {
            val result = service.block()
            serializer.save(result to cache)
            result
        }

    private suspend fun <T> KSerializer<T>.save(what: Pair<T, File>) =
        withContext(Dispatchers.IO) {
            try {
                what.second.writeText(json.encodeToString(this@save, what.first))
            } catch (ignored: IOException) {
            } catch (ignored: SerializationException) {
            }
        }

    private suspend fun <T> KSerializer<T>.load(from: File): T? =
        withContext(Dispatchers.IO) {
            try {
                json.decodeFromString(this@load, from.readText())
            } catch (ignored: IOException) {
                null
            } catch (ignored: SerializationException) {
                null
            }
        }
}
