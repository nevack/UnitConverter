package org.nevack.unitconverter.model.converter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.nevack.unitconverter.NBRBService;
import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Rate;
import org.nevack.unitconverter.model.Unit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyConverter extends Converter {
    private static final String FILE = "rates.json";

    private static final Unit BYN = new Unit("Белорусский рубль", 1d, "BYN");
    private final Gson gson;
    private final File file;

    public CurrencyConverter(Context context) {

        gson = new Gson();

        file = new File(context.getFilesDir(), FILE);
        if (file.exists()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(file.lastModified());
            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                loadUnitsFromFile();
            } else {
                loadUnitsFromWeb();
            }
        } else {
            loadUnitsFromWeb();
        }

        units.add(BYN);

        Collections.sort(units, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
    }

    private void loadUnitsFromFile() {
        try {
            Type type = new TypeToken<List<Rate>>(){}.getType();
            List<Rate> rates = gson.fromJson(new FileReader(file), type);
            for (Rate rate : rates) {
                units.add(rate.toUnit());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void loadUnitsFromWeb() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.nbrb.by/API/ExRates/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        NBRBService service = retrofit.create(NBRBService.class);
        Call<List<Rate>> call = service.getAllRatesForToday();

        try {
            Response<List<Rate>> response = call.execute();
            if (response.isSuccessful()) {
                List<Rate> rates = response.body();
                if (rates != null) {
                    for (Rate rate : rates) {
                        units.add(rate.toUnit());
                    }
                }

                Type type = new TypeToken<List<Rate>>() {}.getType();
                String json = gson.toJson(rates, type);
                FileWriter fw = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(fw);
                out.write(json);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            loadUnitsFromFile();
        }
    }

    @Override
    public int getName() {
        return R.string.currency;
    }
}
