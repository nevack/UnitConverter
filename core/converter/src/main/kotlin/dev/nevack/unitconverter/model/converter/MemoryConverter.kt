package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class MemoryConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("bit", 0.125),
                UnitDefinition("memorybyte", 1.0),
                UnitDefinition("kilobyte", 1000.0),
                UnitDefinition("megabyte", 1000000.0),
                UnitDefinition("gigabyte", 1000000000.0),
                UnitDefinition("terabyte", 1000000000000.0),
                UnitDefinition("petabyte", 1000000000000000.0),
            )
    }
}
