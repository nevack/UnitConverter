package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class MassConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.mass

    init {
        units.add(
            Unit(
                context.getString(R.string.kilogram),
                1.0,
                context.getString(R.string.kilogramsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.gram),
                0.001,
                context.getString(R.string.gramsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.milligram),
                0.0000001,
                context.getString(R.string.milligramsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.hundredweight),
                100.0,
                context.getString(R.string.hundredweightsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.tonne),
                1000.0,
                context.getString(R.string.tonnesymbol)
            )
        )
    }
}