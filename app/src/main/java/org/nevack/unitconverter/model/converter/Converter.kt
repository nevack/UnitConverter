package org.nevack.unitconverter.model.converter

import androidx.annotation.StringRes
import org.nevack.unitconverter.model.ConversionUnit
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*

abstract class Converter(@StringRes val nameRes: Int) {
    private val conversionUnits: MutableList<ConversionUnit> = ArrayList()

    fun registerUnit(unit: ConversionUnit) {
        conversionUnits += unit
    }

    fun sortUnitsWith(comparator: Comparator<ConversionUnit>) {
        conversionUnits.sortWith(comparator)
    }

    operator fun get(index: Int): ConversionUnit = conversionUnits[index]

    abstract suspend fun load()

    @Throws(ArithmeticException::class)
    open fun convert(inputValue: String, inputValueType: Int, outputValueType: Int): String {
        val source = BigDecimal(inputValue, MC)
        val sourceFactor = conversionUnits[inputValueType].factor.toBigDecimal()
        val resultFactor = conversionUnits[outputValueType].factor.toBigDecimal()
        val result = source * sourceFactor / resultFactor
        return result.stripTrailingZeros().toPlainString()
    }

    companion object {
        val MC = MathContext(9, RoundingMode.HALF_UP)
        const val CUBIC_POSTFIX = "<sup><small>3</small></sup>"
        const val SQUARE_POSTFIX = "<sup><small>2</small></sup>"
    }
}