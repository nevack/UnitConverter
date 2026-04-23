package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class AreaConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("squaremetre", 1.0),
                UnitDefinition("squarecentimetre", 0.0001),
                UnitDefinition("squaremillimetre", 0.000001),
                UnitDefinition("squarekilometre", 1000000.0),
                UnitDefinition("squarefoot", 0.09290304),
                UnitDefinition("squareyard", 0.83612736),
                UnitDefinition("squaremile", 2589988.0),
                UnitDefinition("squareinch", 0.00064516),
                UnitDefinition("hectare", 10000.0),
                UnitDefinition("are", 100.0),
                UnitDefinition("acre", 4.047),
            )
    }
}
