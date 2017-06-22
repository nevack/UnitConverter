package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class AreaConverter extends Converter {
    public AreaConverter(Context context) {
        units.add(new Unit(context.getString(R.string.squaremetre), 1d, context.getString(R.string.metresymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squarecentimetre), 0.0001, context.getString(R.string.centimetresymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squaremillimetre), 0.000001d, context.getString(R.string.millimetresymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squarekilometre), 1000000d, context.getString(R.string.kilometresymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squarefoot), 0.09290304d, context.getString(R.string.footsymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squareyard), 0.83612736d, context.getString(R.string.yardsymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squaremile), 2589988d, context.getString(R.string.milesymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.squareinch), 0.00064516d, context.getString(R.string.inchsymbol) + SQUARE_POSTFIX));
        units.add(new Unit(context.getString(R.string.hectare), 10000d, context.getString(R.string.hectaresymbol)));
        units.add(new Unit(context.getString(R.string.are), 100d, context.getString(R.string.aresymbol)));
        units.add(new Unit(context.getString(R.string.acre), 4.047d, context.getString(R.string.acresymbol)));
    }

    @Override
    public int getName() {
        return R.string.area;
    }
}
