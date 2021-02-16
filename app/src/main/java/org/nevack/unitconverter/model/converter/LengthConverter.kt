package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class LengthConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.length

    init {
        units.add(
            Unit(
                context.getString(R.string.metre),
                1.0,
                context.getString(R.string.metresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.millimetre),
                0.001,
                context.getString(R.string.millimetresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.centimetre),
                0.01,
                context.getString(R.string.centimetresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.decimetre),
                0.1,
                context.getString(R.string.decimetresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.kilometre),
                1000.0,
                context.getString(R.string.kilometresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.inch),
                0.0254,
                context.getString(R.string.inchsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.mile),
                1609.344,
                context.getString(R.string.milesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.foot),
                0.3048,
                context.getString(R.string.footsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.yard),
                0.9144,
                context.getString(R.string.yardsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.astronomicalunit),
                149597870700.0,
                context.getString(R.string.astronomicalunitsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.parsec),
                3.0856776e16,
                context.getString(R.string.parsecsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.lightyear),
                9460730472580800.0,
                context.getString(R.string.lightyearsymbol)
            )
        )
    }
}