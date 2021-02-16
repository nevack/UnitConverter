package org.nevack.unitconverter.model.converter

import android.content.Context
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.ConversionUnit

class SpeedConverter(private val context: Context) : Converter(R.string.speed) {

    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.metrespersecond),
                3.6,
                context.getString(R.string.mpssymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kilometresperhour),
                1.0,
                context.getString(R.string.kmpssymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.milesperhour),
                1.609344,
                context.getString(R.string.miphsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.footspersecond),
                1.09728,
                context.getString(R.string.ftpssymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.knots),
                1.852,
                context.getString(R.string.knotssymbol)
            )
        )
    }
}