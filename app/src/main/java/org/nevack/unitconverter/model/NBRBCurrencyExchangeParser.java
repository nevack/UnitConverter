package org.nevack.unitconverter.model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NBRBCurrencyExchangeParser {
    public static final String NBRB_URL = "http://www.nbrb.by/Services/XmlExRates.aspx?ondate=";
    private static final String ns = null;

    public List<Currency> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readDailyExRates(parser);
        } finally {
            in.close();
        }
    }

    private List<Currency> readDailyExRates(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Currency> currencies = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "DailyExRates");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Currency")) {
                currencies.add(readCurrency(parser));
            } else {
                skip(parser);
            }
        }
        return currencies;
    }

    private Currency readCurrency(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Currency");
        String charCode = null;
        String name = null;
        String rate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parsename = parser.getName();
            if (parsename.equals("CharCode")) {
                charCode = readCharCode(parser);
            } else if (parsename.equals("Name")) {
                name = readName(parser);
            } else if (parsename.equals("Rate")) {
                rate = readRate(parser);
            } else {
                skip(parser);
            }
        }
        return new Currency(charCode, name, rate);
    }

    private String readCharCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "CharCode");
        String charCode = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "CharCode");
        return charCode;
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Name");
        return name;
    }

    private String readRate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Rate");
        String rate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Rate");
        return rate;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
