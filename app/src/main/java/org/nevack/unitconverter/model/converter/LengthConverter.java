package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class LengthConverter extends Converter{

    public LengthConverter(Context context) {
        this.context = context;
        unitList.add(new Unit(context.getString(R.string.metre), 1d));
        unitList.add(new Unit(context.getString(R.string.millimetre), 0.001d));
        unitList.add(new Unit(context.getString(R.string.centimetre), 0.01d));
        unitList.add(new Unit(context.getString(R.string.decimetre), 0.1d));
        unitList.add(new Unit(context.getString(R.string.kilometre), 1000d));
        unitList.add(new Unit(context.getString(R.string.inch), 0.0254d));
        unitList.add(new Unit(context.getString(R.string.mile), 1609.344d));
        unitList.add(new Unit(context.getString(R.string.foot), 0.3048d));
        unitList.add(new Unit(context.getString(R.string.yard), 0.9144d));
    }

    public String getTitle() {
        return context.getString(R.string.length);
    }
}
