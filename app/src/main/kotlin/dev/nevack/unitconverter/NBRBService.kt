package dev.nevack.unitconverter

import dev.nevack.unitconverter.model.nbrb.NBRBCurrency
import dev.nevack.unitconverter.model.nbrb.NBRBRate
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service for [NBRB API](https://www.nbrb.by/apihelp/exrates).
 */
@Suppress("unused")
interface NBRBService {
    @GET("currencies")
    suspend fun allCurrencies(): List<NBRBCurrency>

    @GET("currencies/{id}")
    suspend fun currencyById(@Path("id") currencyId: String): List<NBRBCurrency>

    @GET("rates?periodicity=0")
    suspend fun allRatesForToday(): List<NBRBRate>

    @GET("rates?periodicity=0")
    suspend fun allRatesForDate(@Query("onDate") date: String): List<NBRBRate>
}
