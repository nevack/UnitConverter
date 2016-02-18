package org.nevack.unitconverter.model.converter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Currency;
import org.nevack.unitconverter.model.NBRBCurrencyExchangeParser;
import org.nevack.unitconverter.model.Unit;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrencyConverter extends Converter{

    public CurrencyConverter(Context context) {
        this.context = context;

        String fullurl = NBRBCurrencyExchangeParser.NBRB_URL + new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable()) {
            List<Currency> currencies = new DownloadXmlTask().doInBackground(fullurl);

            for (Currency currency : currencies)
                unitList.add(new Unit(currency.name, Double.parseDouble(currency.rate), currency.charCode));
        }
        unitList.add(new Unit("Белорусский рубль", 1d, "BYR"));
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.memory);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List<Currency>> {
        @Override
        protected List<Currency> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return new ArrayList<>();
            } catch (XmlPullParserException e) {
                return new ArrayList<>();
            }
        }

        private List<Currency> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            NBRBCurrencyExchangeParser nbrbCurrencyExchangeParser = new NBRBCurrencyExchangeParser();
            List<Currency> currencies = null;
            try {
                stream = downloadUrl(urlString);
                currencies = nbrbCurrencyExchangeParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return currencies;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL("http://www.nbrb.by/Services/XmlExRates.aspx?ondate=02/18/2016");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}
