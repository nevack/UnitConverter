package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class VolumeConverter extends Converter{

    public VolumeConverter(Context context) {
        units.add(new Unit(context.getString(R.string.litre), 0.001d, context.getString(R.string.litresymbol)));
        units.add(new Unit(context.getString(R.string.cubicmetre), 1d, context.getString(R.string.metresymbol) + CUBIC_POSTFIX));
        units.add(new Unit(context.getString(R.string.millilitre), 0.000001d, context.getString(R.string.millilitresymbol)));
        units.add(new Unit(context.getString(R.string.gallon), 0.00378541d, context.getString(R.string.gallonsymbol)));
        units.add(new Unit(context.getString(R.string.barrel), 0.158988d, context.getString(R.string.barrelsymbol)));
    }

    public int getName() {
        return R.string.volume;
    }
}
