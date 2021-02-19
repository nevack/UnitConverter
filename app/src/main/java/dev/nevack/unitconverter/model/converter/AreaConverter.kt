package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit

class AreaConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squaremetre),
                1.0,
                context.getString(R.string.metresymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squarecentimetre),
                0.0001,
                context.getString(R.string.centimetresymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squaremillimetre),
                0.000001,
                context.getString(R.string.millimetresymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squarekilometre),
                1000000.0,
                context.getString(R.string.kilometresymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squarefoot),
                0.09290304,
                context.getString(R.string.footsymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squareyard),
                0.83612736,
                context.getString(R.string.yardsymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squaremile),
                2589988.0,
                context.getString(R.string.milesymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.squareinch),
                0.00064516,
                context.getString(R.string.inchsymbol) + SQUARE_POSTFIX
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.hectare),
                10000.0,
                context.getString(R.string.hectaresymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.are),
                100.0,
                context.getString(R.string.aresymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.acre),
                4.047,
                context.getString(R.string.acresymbol)
            )
        )
    }
}
