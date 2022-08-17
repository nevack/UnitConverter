package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class MassConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kilogram),
                1.0,
                context.getString(R.string.kilogramsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.gram),
                0.001,
                context.getString(R.string.gramsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.milligram),
                0.0000001,
                context.getString(R.string.milligramsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.hundredweight),
                100.0,
                context.getString(R.string.hundredweightsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.tonne),
                1000.0,
                context.getString(R.string.tonnesymbol)
            )
        )
    }
}
