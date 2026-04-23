package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class SpeedConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("metrespersecond", 3.6),
                UnitDefinition("kilometresperhour", 1.0),
                UnitDefinition("milesperhour", 1.609344),
                UnitDefinition("footspersecond", 1.09728),
                UnitDefinition("knots", 1.852),
            )
    }
}
