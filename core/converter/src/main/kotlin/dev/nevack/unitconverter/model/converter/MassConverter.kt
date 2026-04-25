package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class MassConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("kilogram", 1.0),
                UnitDefinition("gram", 0.001),
                UnitDefinition("milligram", 0.000001),
                UnitDefinition("hundredweight", 100.0),
                UnitDefinition("tonne", 1000.0),
            )
    }
}
