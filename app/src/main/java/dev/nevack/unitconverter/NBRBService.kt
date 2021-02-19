package dev.nevack.unitconverter

import dev.nevack.unitconverter.model.Currency
import dev.nevack.unitconverter.model.Rate
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NBRBService {
    @GET("Currencies")
    suspend fun listAllCurrencies(): List<Currency>

    @GET("Currencies/{id}")
    suspend fun getCurrencyById(@Path("id") currencyId: String): List<Currency>

    @GET("Rates?Periodicity=0")
    suspend fun allRatesForToday(): List<Rate>

    @GET("Rates?Periodicity=0")
    suspend fun getAllRatesForDate(@Query("onDate") date: String): List<Rate>

    @GET("Rates?Periodicity=1")
    suspend fun allRatesForThisMonth(): List<Rate>
}
