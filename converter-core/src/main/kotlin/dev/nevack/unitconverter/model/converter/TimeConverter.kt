package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class TimeConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("microsecond", 0.000001),
                UnitDefinition("millisecond", 0.001),
                UnitDefinition("second", 1.0),
                UnitDefinition("minute", 60.0),
                UnitDefinition("hour", 3600.0),
                UnitDefinition("day", 86400.0),
                UnitDefinition("week", 604800.0),
                UnitDefinition("year", 31557600.0),
            )
    }
}
