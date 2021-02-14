package org.nevack.unitconverter.model.converter

import android.content.Context
import com.squareup.moshi.*
import okio.buffer
import okio.sink
import okio.source
import org.nevack.unitconverter.NBRBService
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Rate
import org.nevack.unitconverter.model.Unit
import retrofit2.Call
import java.io.*
import java.util.*

class CurrencyConverter(context: Context) : Converter() {
    internal var service: NBRBService? = null
    internal var moshi: Moshi? = null
    private val adapter: JsonAdapter<List<Rate>> by lazy(LazyThreadSafetyMode.NONE) {
        requireNotNull(moshi).adapter(TYPE)
    }

    private val file: File = File(context.filesDir, FILE)

    override val name = R.string.currency

    internal fun load() {
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
        units.add(BYN)
        units.sortWith { lhs: Unit, rhs: Unit -> lhs.name.compareTo(rhs.name, ignoreCase = true) }
    }

    @Throws(IOException::class)
    private fun loadUnitsFromFile() {
        val rates = adapter.fromJson(file.source().buffer())
        rates?.forEach { units.add(it.toUnit()) }
    }

    @Throws(IOException::class)
    private fun loadUnitsFromWeb() {
        val service = requireNotNull(service) { "Service is not set!" }
        val call: Call<List<Rate>> = service.allRatesForToday
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val rates = response.body()
                rates?.forEach { units.add(it.toUnit()) }
                adapter.toJson(file.sink().buffer(), rates)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            loadUnitsFromFile()
        }
    }

    companion object {
        private val TYPE = Types.newParameterizedType(List::class.java, Rate::class.java)
        private const val FILE = "rates.json"
        private val BYN = Unit("Белорусский рубль", 1.0, "BYN")
    }
}