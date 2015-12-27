package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class SpeedConverter extends Converter {
    public SpeedConverter(Context context) {
        this.context = context;
        unitList.add(new Unit(context.getString(R.string.metrespersecond), 3.6d, context.getString(R.string.mpssymbol)));
        unitList.add(new Unit(context.getString(R.string.kilometresperhour), 1d, context.getString(R.string.kmpssymbol)));
        unitList.add(new Unit(context.getString(R.string.milesperhour), 1.609344d, context.getString(R.string.miphsymbol)));
        unitList.add(new Unit(context.getString(R.string.footspersecond), 1.09728d, context.getString(R.string.ftpssymbol)));
        unitList.add(new Unit(context.getString(R.string.knots), 1.852d, context.getString(R.string.knotssymbol)));
    }

    public String getTitle() {
        return context.getString(R.string.speed);
    }
}
