package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class OtherConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.other

    init {
        units.add(
            Unit(
                context.getString(R.string.pico),
                0.000000000001,
                "×10<sup><small>-12</small></sup>"
            )
        )
        units.add(
            Unit(
                context.getString(R.string.nano),
                0.000000001,
                "×10<sup><small>-9</small></sup>"
            )
        )
        units.add(
            Unit(
                context.getString(R.string.micro),
                0.000001,
                "×10<sup><small>-6</small></sup>"
            )
        )
        units.add(Unit(context.getString(R.string.milli), 0.001, "×10<sup><small>-3</small></sup>"))
        units.add(Unit("", 1.0, ""))
        units.add(Unit(context.getString(R.string.kilo), 1000.0, "×10<sup><small>3</small></sup>"))
        units.add(
            Unit(
                context.getString(R.string.mega),
                1000000.0,
                "×10<sup><small>6</small></sup>"
            )
        )
        units.add(
            Unit(
                context.getString(R.string.giga),
                1000000000.0,
                "×10<sup><small>9</small></sup>"
            )
        )
        units.add(
            Unit(
                context.getString(R.string.tera),
                1000000000000.0,
                "×10<sup><small>12</small></sup>"
            )
        )
    }
}