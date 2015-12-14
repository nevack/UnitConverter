package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class MassConverter extends Converter{

    public MassConverter(Context context) {
        this.context = context;
        unitList.add(new Unit(context.getString(R.string.kilogram), 1d));
        unitList.add(new Unit(context.getString(R.string.gram), 0.001d));
        unitList.add(new Unit(context.getString(R.string.milligram), 0.0000001d));
        unitList.add(new Unit(context.getString(R.string.ton), 1000d));
    }

    public String getTitle() {
        return context.getString(R.string.mass);
    }
}
