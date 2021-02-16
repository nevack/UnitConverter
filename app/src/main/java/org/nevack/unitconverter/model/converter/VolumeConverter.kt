package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit

class VolumeConverter(context: Context) : Converter() {
    override val name: Int
        get() = R.string.volume

    init {
        units.add(
            Unit(
                context.getString(R.string.litre),
                0.001,
                context.getString(R.string.litresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.cubicmetre),
                1.0,
                context.getString(R.string.metresymbol) + CUBIC_POSTFIX
            )
        )
        units.add(
            Unit(
                context.getString(R.string.millilitre),
                0.000001,
                context.getString(R.string.millilitresymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.gallon),
                0.00378541,
                context.getString(R.string.gallonsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.barrel),
                0.158988,
                context.getString(R.string.barrelsymbol)
            )
        )
    }
}