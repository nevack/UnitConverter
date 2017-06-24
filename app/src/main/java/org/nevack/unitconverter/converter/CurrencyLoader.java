package org.nevack.unitconverter.converter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.nevack.unitconverter.NBRBService;
import org.nevack.unitconverter.model.Currency;
import org.nevack.unitconverter.model.Rate;
import org.nevack.unitconverter.model.Unit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyLoader extends AsyncTaskLoader<List<Unit>> {

    private static final String FILE = "rates.json";

    private static final Unit BYN = new Unit("Белорусский рубль", 1d, "BYN");
    private final Gson gson;
    private final File file;
    private List<Unit> units;

    public CurrencyLoader(Context context) {
        super(context);

        gson = new Gson();
        file = new File(context.getFilesDir(), FILE);
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override public List<Unit> loadInBackground() {

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

        if (units == null) {
            units = new ArrayList<>();
        }

        units.add(BYN);

        Collections.sort(units, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));

        // Done!
        return units;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(List<Unit> units) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (units != null) {
                onReleaseResources(units);
            }
        }
        List<Unit> oldUnits = this.units;
        this.units = units;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(units);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldUnits != null) {
            onReleaseResources(oldUnits);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (units != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(units);
        }

        if (takeContentChanged() || units == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<Unit> units) {
        super.onCanceled(units);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(units);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (units != null) {
            onReleaseResources(units);
            units = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Unit> units) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
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
                .baseUrl("http://www.nbrb.by/API/ExRates/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        NBRBService service = retrofit.create(NBRBService.class);
        Call<List<Rate>> call = service.getAllRatesForToday();

        try {
            Response<List<Rate>> response = call.execute();
            if (response.isSuccessful()) {
                List<Rate> rates = response.body();
                for (Rate rate : rates) {
                    units.add(rate.toUnit());
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
}
