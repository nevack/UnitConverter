package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class OtherConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.pico),
                0.000000000001,
                "-12".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.nano),
                0.000000001,
                "-9".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.micro),
                0.000001,
                "-6".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.milli),
                0.001,
                "-3".toExp()
            )
        )
        registerUnit(ConversionUnit.EMPTY)
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kilo),
                1000.0,
                "3".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.mega),
                1000000.0,
                "6".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.giga),
                1000000000.0,
                "9".toExp()
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.tera),
                1000000000000.0,
                "12".toExp()
            )
        )
    }

    private fun String.toExp(): String = "×10<sup><small>$this</small></sup>"
}
