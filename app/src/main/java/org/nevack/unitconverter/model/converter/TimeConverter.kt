package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class TimeConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.time

    init {
        units.add(
            Unit(
                context.getString(R.string.microsecond),
                0.000001,
                context.getString(R.string.microsecondsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.millisecond),
                0.001,
                context.getString(R.string.millisecondsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.second),
                1.0,
                context.getString(R.string.secondsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.minute),
                60.0,
                context.getString(R.string.minutesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.hour),
                3600.0,
                context.getString(R.string.hoursymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.day),
                86400.0,
                context.getString(R.string.daysymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.week),
                604800.0,
                context.getString(R.string.weeksymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.year),
                31557600.0,
                context.getString(R.string.yearsymbol)
            )
        )
    }
}