package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.NBRBService;
import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Rate;
import org.nevack.unitconverter.model.Unit;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyConverter extends Converter {

    private static final Unit BYN = new Unit("Белорусский рубль", 1d, "BYN");

    public CurrencyConverter(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.nbrb.by/API/ExRates/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        NBRBService service = retrofit.create(NBRBService.class);
        Call<List<Rate>> call = service.getAllRatesForToday();

        try {
            Response<List<Rate>> response = call.execute();
            if (response.isSuccessful()) {
                for (Rate rate : response.body()) {
                    units.add(rate.toUnit());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        units.add(BYN);

        Collections.sort(units, new Comparator<Unit>() {
            @Override
            public int compare(Unit lhs, Unit rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
    }

    @Override
    public int getTitle() {
        return R.string.currency;
    }
}
