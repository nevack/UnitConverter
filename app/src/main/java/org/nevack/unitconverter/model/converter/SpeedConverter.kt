package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class SpeedConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.speed

    init {
        units.add(
            Unit(
                context.getString(R.string.metrespersecond),
                3.6,
                context.getString(R.string.mpssymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.kilometresperhour),
                1.0,
                context.getString(R.string.kmpssymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.milesperhour),
                1.609344,
                context.getString(R.string.miphsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.footspersecond),
                1.09728,
                context.getString(R.string.ftpssymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.knots),
                1.852,
                context.getString(R.string.knotssymbol)
            )
        )
    }
}