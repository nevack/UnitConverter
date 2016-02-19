package org.nevack.unitconverter.model.converter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Currency;
import org.nevack.unitconverter.model.NBRBCurrencyExchangeParser;
import org.nevack.unitconverter.model.Unit;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
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

        String url = NBRBCurrencyExchangeParser.NBRB_URL + new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable()) {
            List<Currency> currencies = new DownloadXmlTask().doInBackground(url);

            for (Currency currency : currencies)
                unitList.add(new Unit(currency.name, Double.parseDouble(currency.rate), currency.charCode));
            try {
                URL xmlurl = new URL(NBRBCurrencyExchangeParser.NBRB_URL);
                HttpURLConnection conn = (HttpURLConnection) xmlurl.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.connect();

                File file = new File(context.getFilesDir(), "currency.xml");
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = conn.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            URL url = new URL(urlString);
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
