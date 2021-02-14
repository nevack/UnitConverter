package org.nevack.unitconverter.model.converter

import org.nevack.unitconverter.model.Unit
import java.math.BigDecimal
import java.util.*

abstract class Converter {
    @JvmField
    val units: MutableList<Unit> = ArrayList()
    @Throws(ArithmeticException::class)
    open fun convert(inputValue: String?, inputValueType: Int, outputValueType: Int): String? {
        val source = BigDecimal(inputValue)
        val sourceFactor = BigDecimal.valueOf(units[inputValueType].factor)
        val resultFactor = BigDecimal.valueOf(units[outputValueType].factor)
        val result = source
            .multiply(sourceFactor)
            .divide(resultFactor, SCALE, BigDecimal.ROUND_HALF_UP)
        return result.stripTrailingZeros().toPlainString()
    }

    abstract val name: Int

    companion object {
        const val SCALE = 9
        const val CUBIC_POSTFIX = "<sup><small>3</small></sup>"
        const val SQUARE_POSTFIX = "<sup><small>2</small></sup>"
    }
}