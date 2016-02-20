package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class MemoryConverter extends Converter {

    public MemoryConverter(Context context) {
        this.context = context;

        unitList.add(new Unit(context.getString(R.string.bit), 0.125d, context.getString(R.string.bit_symbol)));
        unitList.add(new Unit(context.getString(R.string.memorybyte), 1d, context.getString(R.string.memorybytesymbol)));
        unitList.add(new Unit(context.getString(R.string.kilobyte), 1000d, context.getString(R.string.kilobytesymbol)));
        unitList.add(new Unit(context.getString(R.string.megabyte), 1000000d, context.getString(R.string.megabytesymbol)));
        unitList.add(new Unit(context.getString(R.string.gigabyte), 1000000000d, context.getString(R.string.gigabytesymbol)));
        unitList.add(new Unit(context.getString(R.string.terabyte), 1000000000000d, context.getString(R.string.terabytesymbol)));
        unitList.add(new Unit(context.getString(R.string.petabyte), 1000000000000000d, context.getString(R.string.petabytesymbol)));
    }
    public String getTitle() {
        return context.getString(R.string.memory);
    }
}
