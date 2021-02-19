package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.ArrayList
import java.util.Comparator

abstract class Converter {
    private val _conversionUnits: MutableList<ConversionUnit> = ArrayList()
    val units: List<ConversionUnit>
        get() = _conversionUnits

    fun registerUnit(unit: ConversionUnit) {
        _conversionUnits += unit
    }

    fun sortUnitsWith(comparator: Comparator<ConversionUnit>) {
        _conversionUnits.sortWith(comparator)
    }

    operator fun get(index: Int): ConversionUnit = _conversionUnits[index]

    abstract suspend fun load()

    @Throws(ArithmeticException::class)
    open fun convert(inputValue: String, inputValueType: Int, outputValueType: Int): String {
        val source = BigDecimal(inputValue, MC)
        val sourceFactor = _conversionUnits[inputValueType].factor.toBigDecimal()
        val resultFactor = _conversionUnits[outputValueType].factor.toBigDecimal()
        val result = source * sourceFactor / resultFactor
        return result.stripTrailingZeros().toPlainString()
    }

    companion object {
        val MC = MathContext(9, RoundingMode.HALF_UP)
        const val SQUARE_POSTFIX = "²"
        const val CUBIC_POSTFIX = "³"
    }
}
