package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class OtherConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("pico", 0.000000000001),
                UnitDefinition("nano", 0.000000001),
                UnitDefinition("micro", 0.000001),
                UnitDefinition("milli", 0.001),
                UnitDefinition("unit", 1.0),
                UnitDefinition("kilo", 1000.0),
                UnitDefinition("mega", 1000000.0),
                UnitDefinition("giga", 1000000000.0),
                UnitDefinition("tera", 1000000000000.0),
            )
    }
}
