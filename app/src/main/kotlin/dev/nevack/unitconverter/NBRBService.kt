package dev.nevack.unitconverter

import dev.nevack.unitconverter.model.nbrb.NBRBCurrency
import dev.nevack.unitconverter.model.nbrb.NBRBRate
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("unused")
interface NBRBService {
    @GET("Currencies")
    suspend fun allCurrencies(): List<NBRBCurrency>

    @GET("Currencies/{id}")
    suspend fun currencyById(@Path("id") currencyId: String): List<NBRBCurrency>

    @GET("Rates?Periodicity=0")
    suspend fun allRatesForToday(): List<NBRBRate>

    @GET("Rates?Periodicity=0")
    suspend fun allRatesForDate(@Query("onDate") date: String): List<NBRBRate>
}
