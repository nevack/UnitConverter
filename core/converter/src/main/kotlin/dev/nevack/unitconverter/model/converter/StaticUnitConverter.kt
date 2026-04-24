package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

abstract class StaticUnitConverter(
    private val unitsToRegister: List<ConversionUnit>,
) : Converter() {
    final override suspend fun load() {
        unitsToRegister.forEach(::registerUnit)
    }
}
