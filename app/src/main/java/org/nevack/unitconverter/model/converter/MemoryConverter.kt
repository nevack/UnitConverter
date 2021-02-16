package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class MemoryConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.memory

    init {
        units.add(
            Unit(
                context.getString(R.string.bit),
                0.125,
                context.getString(R.string.bit_symbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.memorybyte),
                1.0,
                context.getString(R.string.memorybytesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.kilobyte),
                1000.0,
                context.getString(R.string.kilobytesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.megabyte),
                1000000.0,
                context.getString(R.string.megabytesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.gigabyte),
                1000000000.0,
                context.getString(R.string.gigabytesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.terabyte),
                1000000000000.0,
                context.getString(R.string.terabytesymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.petabyte),
                1000000000000000.0,
                context.getString(R.string.petabytesymbol)
            )
        )
    }
}