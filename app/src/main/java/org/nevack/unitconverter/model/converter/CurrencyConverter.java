package org.nevack.unitconverter.model.converter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.nevack.unitconverter.R;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrencyConverter extends Converter{

    private static final String FILE_NAME = "currency.xml";

    public CurrencyConverter(Context context) {
        super(context);

        String url = NBRBCurrencyExchangeParser.NBRB_URL + new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable()) {
            try {
                Log.d(TAG, "doInBackground: Internet is available!");
                InputStream inputStream = downloadUrl(url);
                Log.d(TAG, "doInBackground: Fetched data");
                File file = new File(context.getFilesDir(), FILE_NAME);
                FileOutputStream fileOutput = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();

                Log.d(TAG, "doInBackground: New data has been written to the local file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Log.d(TAG, "doInBackground: Reading from local file");
            unitList = loadXmlFromFile();
        } catch (IOException | XmlPullParserException e) {
            unitList = new ArrayList<>();
        }

        unitList.add(new Unit("Белорусский рубль", 1d, "BYR"));

        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit lhs, Unit rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.currency);
    }

    private List<Unit> loadXmlFromFile() throws XmlPullParserException, IOException {
        InputStream stream = null;
        NBRBCurrencyExchangeParser nbrbCurrencyExchangeParser = new NBRBCurrencyExchangeParser();

        List<Unit> units = null;
        try {
            stream = context.openFileInput(CurrencyConverter.FILE_NAME);
            units = nbrbCurrencyExchangeParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return units;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        return conn.getInputStream();
    }
}
