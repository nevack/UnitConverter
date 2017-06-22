package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class LengthConverter extends Converter{

    public LengthConverter(Context context) {
        units.add(new Unit(context.getString(R.string.metre), 1d, context.getString(R.string.metresymbol)));
        units.add(new Unit(context.getString(R.string.millimetre), 0.001d, context.getString(R.string.millimetresymbol)));
        units.add(new Unit(context.getString(R.string.centimetre), 0.01d, context.getString(R.string.centimetresymbol)));
        units.add(new Unit(context.getString(R.string.decimetre), 0.1d, context.getString(R.string.decimetresymbol)));
        units.add(new Unit(context.getString(R.string.kilometre), 1000d, context.getString(R.string.kilometresymbol)));
        units.add(new Unit(context.getString(R.string.inch), 0.0254d, context.getString(R.string.inchsymbol)));
        units.add(new Unit(context.getString(R.string.mile), 1609.344d, context.getString(R.string.milesymbol)));
        units.add(new Unit(context.getString(R.string.foot), 0.3048d, context.getString(R.string.footsymbol)));
        units.add(new Unit(context.getString(R.string.yard), 0.9144d, context.getString(R.string.yardsymbol)));
        units.add(new Unit(context.getString(R.string.astronomicalunit), 149597870700d, context.getString(R.string.astronomicalunitsymbol)));
        units.add(new Unit(context.getString(R.string.parsec), 3.0856776e16, context.getString(R.string.parsecsymbol)));
        units.add(new Unit(context.getString(R.string.lightyear), 9460730472580800d, context.getString(R.string.lightyearsymbol)));
    }

    public int getName() {
        return R.string.length;
    }
}
