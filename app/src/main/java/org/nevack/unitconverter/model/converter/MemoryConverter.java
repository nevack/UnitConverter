package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class MemoryConverter extends Converter {

    public MemoryConverter(Context context) {
        units.add(new Unit(context.getString(R.string.bit), 0.125d, context.getString(R.string.bit_symbol)));
        units.add(new Unit(context.getString(R.string.memorybyte), 1d, context.getString(R.string.memorybytesymbol)));
        units.add(new Unit(context.getString(R.string.kilobyte), 1000d, context.getString(R.string.kilobytesymbol)));
        units.add(new Unit(context.getString(R.string.megabyte), 1000000d, context.getString(R.string.megabytesymbol)));
        units.add(new Unit(context.getString(R.string.gigabyte), 1000000000d, context.getString(R.string.gigabytesymbol)));
        units.add(new Unit(context.getString(R.string.terabyte), 1000000000000d, context.getString(R.string.terabytesymbol)));
        units.add(new Unit(context.getString(R.string.petabyte), 1000000000000000d, context.getString(R.string.petabytesymbol)));
    }
    public int getName() {
        return R.string.memory;
    }
}
