package dev.nevack.unitconverter.model.converter

import dev.nevack.unitconverter.model.ConversionUnit

class CurrencyConverter(
    bynUnit: ConversionUnit,
    private val loadUnits: suspend () -> List<ConversionUnit>,
) : Converter() {
    init {
        registerUnit(bynUnit)
    }

    override suspend fun load() {
        loadUnits().forEach(::registerUnit)
        sortUnitsWith { a, b -> a.name.compareTo(b.name, ignoreCase = true) }
    }
}
