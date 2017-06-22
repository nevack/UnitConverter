package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

public class TimeConverter extends Converter
{
    public TimeConverter(Context context) {
        units.add(new Unit(context.getString(R.string.microsecond), 0.000001d, context.getString(R.string.microsecondsymbol)));
        units.add(new Unit(context.getString(R.string.millisecond), 0.001d, context.getString(R.string.millisecondsymbol)));
        units.add(new Unit(context.getString(R.string.second), 1d, context.getString(R.string.secondsymbol)));
        units.add(new Unit(context.getString(R.string.minute), 60d, context.getString(R.string.minutesymbol)));
        units.add(new Unit(context.getString(R.string.hour), 3600d, context.getString(R.string.hoursymbol)));
        units.add(new Unit(context.getString(R.string.day), 86400d, context.getString(R.string.daysymbol)));
        units.add(new Unit(context.getString(R.string.week), 604800d, context.getString(R.string.weeksymbol)));
        units.add(new Unit(context.getString(R.string.year), 31557600d, context.getString(R.string.yearsymbol)));
    }

    public int getName() {
        return R.string.time;
    }
}
