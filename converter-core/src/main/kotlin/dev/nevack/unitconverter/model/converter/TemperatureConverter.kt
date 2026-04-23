package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit
import java.lang.ArithmeticException
import java.math.BigDecimal

class TemperatureConverter(
    units: List<ConversionUnit>,
) : Converter() {
    private val unitsToRegister = units

    override suspend fun load() {
        unitsToRegister.forEach(::registerUnit)
    }

    @Throws(ArithmeticException::class)
    override fun convert(
        inputValue: String,
        inputValueType: Int,
        outputValueType: Int,
    ): String {
        val sourceValue = BigDecimal(inputValue, MC)
        return inputValueType
            .temperatureUnit()
            .convertTo(outputValueType.temperatureUnit(), sourceValue)
            .stripTrailingZeros()
            .toPlainString()
    }

    private fun Int.temperatureUnit(): TemperatureUnit =
        when (this) {
            TemperatureUnit.Kelvin.index -> TemperatureUnit.Kelvin
            TemperatureUnit.Celsius.index -> TemperatureUnit.Celsius
            TemperatureUnit.Fahrenheit.index -> TemperatureUnit.Fahrenheit
            else -> throw ArithmeticException()
        }

    private sealed class TemperatureUnit(
        val index: Int,
    ) {
        abstract fun convertTo(
            unit: TemperatureUnit,
            x: BigDecimal,
        ): BigDecimal

        data object Kelvin : TemperatureUnit(0) {
            override fun convertTo(
                unit: TemperatureUnit,
                x: BigDecimal,
            ): BigDecimal =
                when (unit) {
                    Kelvin -> x
                    Celsius -> x + MIN_KELVIN
                    Fahrenheit -> Celsius.convertTo(Fahrenheit, x + MIN_KELVIN)
                }
        }

        data object Celsius : TemperatureUnit(1) {
            override fun convertTo(
                unit: TemperatureUnit,
                x: BigDecimal,
            ): BigDecimal =
                when (unit) {
                    Kelvin -> x - MIN_KELVIN
                    Celsius -> x
                    Fahrenheit -> x * FAHRENHEIT_MULTIPLIER + FAHRENHEIT_OFFSET
                }
        }

        data object Fahrenheit : TemperatureUnit(2) {
            override fun convertTo(
                unit: TemperatureUnit,
                x: BigDecimal,
            ): BigDecimal =
                when (unit) {
                    Kelvin -> convertTo(Celsius, x) - MIN_KELVIN
                    Celsius -> (x - FAHRENHEIT_OFFSET) / FAHRENHEIT_MULTIPLIER
                    Fahrenheit -> x
                }
        }

        companion object {
            private val MIN_KELVIN = BigDecimal("-273.15", MC)
            private val FAHRENHEIT_OFFSET = BigDecimal(32, MC)
            private val FAHRENHEIT_MULTIPLIER = BigDecimal(1.8, MC)
        }
    }

    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("kelvin", 1.0),
                UnitDefinition("celsius", 1.0),
                UnitDefinition("fahrenheit", 1.0),
            )
    }
}
