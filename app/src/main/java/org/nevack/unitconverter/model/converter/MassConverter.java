package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class MassConverter extends Converter{

    public MassConverter(Context context) {
        units.add(new Unit(context.getString(R.string.kilogram), 1d, context.getString(R.string.kilogramsymbol)));
        units.add(new Unit(context.getString(R.string.gram), 0.001d, context.getString(R.string.gramsymbol)));
        units.add(new Unit(context.getString(R.string.milligram), 0.0000001d, context.getString(R.string.milligramsymbol)));
        units.add(new Unit(context.getString(R.string.hundredweight), 100d, context.getString(R.string.hundredweightsymbol)));
        units.add(new Unit(context.getString(R.string.tonne), 1000d, context.getString(R.string.tonnesymbol)));
    }

    public int getName() {
        return R.string.mass;
    }
}
