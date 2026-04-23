package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class LengthConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("metre", 1.0),
                UnitDefinition("millimetre", 0.001),
                UnitDefinition("centimetre", 0.01),
                UnitDefinition("decimetre", 0.1),
                UnitDefinition("kilometre", 1000.0),
                UnitDefinition("inch", 0.0254),
                UnitDefinition("mile", 1609.344),
                UnitDefinition("foot", 0.3048),
                UnitDefinition("yard", 0.9144),
                UnitDefinition("astronomicalunit", 149597870700.0),
                UnitDefinition("parsec", 3.0856776e16),
                UnitDefinition("lightyear", 9460730472580800.0),
            )
    }
}
