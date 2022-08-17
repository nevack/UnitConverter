package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class MemoryConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.bit),
                0.125,
                context.getString(R.string.bit_symbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.memorybyte),
                1.0,
                context.getString(R.string.memorybytesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kilobyte),
                1000.0,
                context.getString(R.string.kilobytesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.megabyte),
                1000000.0,
                context.getString(R.string.megabytesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.gigabyte),
                1000000000.0,
                context.getString(R.string.gigabytesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.terabyte),
                1000000000000.0,
                context.getString(R.string.terabytesymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.petabyte),
                1000000000000000.0,
                context.getString(R.string.petabytesymbol)
            )
        )
    }
}
