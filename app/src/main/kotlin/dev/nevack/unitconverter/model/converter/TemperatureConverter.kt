package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit
import java.lang.ArithmeticException
import java.math.BigDecimal

class TemperatureConverter(private val context: Context) : Converter() {
    override suspend fun load() {
        registerUnit(
            ConversionUnit(
                context.getString(R.string.kelvin),
                1.0,
                context.getString(R.string.kelvinsymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.celsius),
                1.0,
                context.getString(R.string.celsiussymbol)
            )
        )
        registerUnit(
            ConversionUnit(
                context.getString(R.string.fahrenheit),
                1.0,
                context.getString(R.string.fahrenheitsymbol)
            )
        )
    }

    @Throws(ArithmeticException::class)
    override fun convert(inputValue: String, inputValueType: Int, outputValueType: Int): String {
        val sourceValue = BigDecimal(inputValue, MC)
        return inputValueType.temperatureUnit()
            .convertTo(outputValueType.temperatureUnit(), sourceValue)
            .stripTrailingZeros()
            .toPlainString()
    }

    private fun Int.temperatureUnit(): TemperatureUnit = when (this) {
        TemperatureUnit.Kelvin.index -> TemperatureUnit.Kelvin
        TemperatureUnit.Celsius.index -> TemperatureUnit.Celsius
        TemperatureUnit.Fahrenheit.index -> TemperatureUnit.Fahrenheit
        else -> throw ArithmeticException()
    }

    private sealed class TemperatureUnit(
        val index: Int,
        val convertTo: (TemperatureUnit, BigDecimal) -> BigDecimal,
    ) {
        object Kelvin : TemperatureUnit(
            0,
            { unit, x ->
                when (unit) {
                    Kelvin -> x
                    Celsius -> x + MIN_KELVIN
                    Fahrenheit -> Celsius.convertTo(Fahrenheit, x + MIN_KELVIN)
                }
            }
        )

        object Celsius : TemperatureUnit(
            1,
            { unit, x ->
                when (unit) {
                    Kelvin -> x - MIN_KELVIN
                    Celsius -> x
                    Fahrenheit -> x * FAHRENHEIT_MULTIPLIER + FAHRENHEIT_OFFSET
                }
            }
        )

        object Fahrenheit : TemperatureUnit(
            2,
            { unit, x ->
                when (unit) {
                    Kelvin -> Fahrenheit.convertTo(Celsius, x) - MIN_KELVIN
                    Celsius -> (x - FAHRENHEIT_OFFSET) / FAHRENHEIT_MULTIPLIER
                    Fahrenheit -> x
                }
            }
        )

        companion object {
            private val MIN_KELVIN = BigDecimal("-273.15", MC)
            private val FAHRENHEIT_OFFSET = BigDecimal(32, MC)
            private val FAHRENHEIT_MULTIPLIER = BigDecimal(1.8, MC)
        }
    }
}
