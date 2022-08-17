package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class TimeConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.microsecond),
                0.000001,
                context.getString(R.string.microsecondsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.millisecond),
                0.001,
                context.getString(R.string.millisecondsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.second),
                1.0,
                context.getString(R.string.secondsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.minute),
                60.0,
                context.getString(R.string.minutesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.hour),
                3600.0,
                context.getString(R.string.hoursymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.day),
                86400.0,
                context.getString(R.string.daysymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.week),
                604800.0,
                context.getString(R.string.weeksymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.year),
                31557600.0,
                context.getString(R.string.yearsymbol)
            )
        )
    }
}
