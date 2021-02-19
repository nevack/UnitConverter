package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class VolumeConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.litre),
                0.001,
                context.getString(R.string.litresymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.cubicmetre),
                1.0,
                context.getString(R.string.metresymbol) + CUBIC_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.millilitre),
                0.000001,
                context.getString(R.string.millilitresymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.gallon),
                0.00378541,
                context.getString(R.string.gallonsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.barrel),
                0.158988,
                context.getString(R.string.barrelsymbol)
            )
        )
    }
}
