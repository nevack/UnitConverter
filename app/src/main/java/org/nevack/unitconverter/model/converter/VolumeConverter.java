package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class VolumeConverter extends Converter{
    public VolumeConverter(Context context) {
        this.context = context;
        unitList.add(new Unit(context.getString(R.string.litre), 0.001d, context.getString(R.string.litresymbol)));
        unitList.add(new Unit(context.getString(R.string.cubicmetre), 1d, context.getString(R.string.metresymbol) + "<sup><small>3</small></sup>"));
        unitList.add(new Unit(context.getString(R.string.millilitre), 0.000001d, context.getString(R.string.millilitresymbol)));
    }

    public String getTitle() {
        return context.getString(R.string.volume);
    }
}
