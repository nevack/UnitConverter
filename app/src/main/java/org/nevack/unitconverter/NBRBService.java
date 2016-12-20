package org.nevack.unitconverter;

import org.nevack.unitconverter.model.Currency;
import org.nevack.unitconverter.model.Rate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NBRBService {
    @GET("Currencies")
    Call<List<Currency>> listAllCurrencies();

    @GET("Currencies/{id}")
    Call<List<Currency>> getCurrencyById(@Path("id") String currencyId);

    @GET("Rates?Periodicity=0")
    Call<List<Rate>> getAllRatesForToday();

    @GET("Rates?Periodicity=0")
    Call<List<Rate>> getAllRatesForDate(@Query("onDate") String date);

    @GET("Rates?Periodicity=1")
    Call<List<Rate>> getAllRatesForThisMonth();
}

