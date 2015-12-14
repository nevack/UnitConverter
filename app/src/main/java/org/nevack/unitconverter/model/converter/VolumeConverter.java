package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class VolumeConverter extends Converter{
    public VolumeConverter(Context context) {
        this.context = context;
        unitList.add(new Unit(context.getString(R.string.litre), 0.001d));
        unitList.add(new Unit(context.getString(R.string.cubicmetre), 1d));
    }

    public String getTitle() {
        return context.getString(R.string.volume);
    }
}
