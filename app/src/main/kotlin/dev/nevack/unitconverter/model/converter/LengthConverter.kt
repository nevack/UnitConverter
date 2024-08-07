package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class LengthConverter(
    private val context: Context,
) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.metre),
                1.0,
                context.getString(R.string.metresymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.millimetre),
                0.001,
                context.getString(R.string.millimetresymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.centimetre),
                0.01,
                context.getString(R.string.centimetresymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.decimetre),
                0.1,
                context.getString(R.string.decimetresymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kilometre),
                1000.0,
                context.getString(R.string.kilometresymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.inch),
                0.0254,
                context.getString(R.string.inchsymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.mile),
                1609.344,
                context.getString(R.string.milesymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.foot),
                0.3048,
                context.getString(R.string.footsymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.yard),
                0.9144,
                context.getString(R.string.yardsymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.astronomicalunit),
                149597870700.0,
                context.getString(R.string.astronomicalunitsymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.parsec),
                3.0856776e16,
                context.getString(R.string.parsecsymbol),
            ),
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.lightyear),
                9460730472580800.0,
                context.getString(R.string.lightyearsymbol),
            ),
        )
    }
}
