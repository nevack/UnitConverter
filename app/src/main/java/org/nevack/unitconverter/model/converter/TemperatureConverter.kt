package org.nevack.unitconverter.model.converter

import android.content.Context
import java.lang.ArithmeticException
import org.nevack.unitconverter.R
import org.nevack.unitconverter.model.Unit
import java.math.BigDecimal

class TemperatureConverter(context: Context) : Converter() {
    @Throws(ArithmeticException::class)
    override fun convert(inputValue: String?, inputValueType: Int, outputValueType: Int): String? {
        if (inputValueType == outputValueType) return inputValue
        val sourceValue = BigDecimal(inputValue)
        return when (inputValueType) {
            0 -> if (sourceValue < BigDecimal.ZERO) throw ArithmeticException() else {
                if (outputValueType == 1) {
                    sourceValue.add(BigDecimal("-273"))
                        .stripTrailingZeros()
                        .toPlainString()
                } else {
                    sourceValue.multiply(BigDecimal("1.8"))
                        .add(BigDecimal("-459.67"))
                        .stripTrailingZeros()
                        .toPlainString()
                }
            }
            1 -> if (sourceValue < BigDecimal("-273")) throw ArithmeticException() else {
                if (outputValueType == 0) {
                    sourceValue.add(BigDecimal("273"))
                        .stripTrailingZeros()
                        .toPlainString()
                } else {
                    sourceValue.multiply(BigDecimal("1.8"))
                        .add(BigDecimal("32"))
                        .stripTrailingZeros()
                        .toPlainString()
                }
            }
            2 -> if (sourceValue < BigDecimal("-459.67")) throw ArithmeticException() else {
                if (outputValueType == 0) {
                    sourceValue.add(BigDecimal("459.67"))
                        .multiply(BigDecimal("5"))
                        .divide(
                            BigDecimal("9"),
                            SCALE,
                            BigDecimal.ROUND_HALF_UP
                        )
                        .stripTrailingZeros()
                        .toPlainString()
                } else {
                    sourceValue.add(BigDecimal("-32"))
                        .divide(
                            BigDecimal("1.8"),
                            SCALE,
                            BigDecimal.ROUND_HALF_UP
                        )
                        .stripTrailingZeros()
                        .toPlainString()
                }
            }
            else -> throw ArithmeticException()
        }
    }

    override val name: Int
        get() = R.string.temperature

    init {
        units.add(
            Unit(
                context.getString(R.string.kelvin),
                1.0,
                context.getString(R.string.kelvinsymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.celsius),
                1.0,
                context.getString(R.string.celsiussymbol)
            )
        )
        units.add(
            Unit(
                context.getString(R.string.fahrenheit),
                1.0,
                context.getString(R.string.fahrenheitsymbol)
            )
        )
    }
}