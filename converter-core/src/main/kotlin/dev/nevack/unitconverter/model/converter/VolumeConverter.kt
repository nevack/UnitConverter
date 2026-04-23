package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class VolumeConverter(
    units: List<ConversionUnit>,
) : StaticUnitConverter(units) {
    companion object {
        val DEFINITIONS =
            listOf(
                UnitDefinition("litre", 0.001),
                UnitDefinition("cubicmetre", 1.0),
                UnitDefinition("millilitre", 0.000001),
                UnitDefinition("gallon", 0.00378541),
                UnitDefinition("barrel", 0.158988),
            )
    }
}
