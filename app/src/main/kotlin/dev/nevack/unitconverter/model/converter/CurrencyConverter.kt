package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit
import dev.nevack.unitconverter.nbrb.NBRBRepository

class CurrencyConverter(
    context: Context,
    private val repository: NBRBRepository,
) : Converter() {
    init {
        registerUnit(getBYNLocalized(context))
    }

    override suspend fun load() {
        repository.getUnits().forEach { unit ->
            registerUnit(ConversionUnit(unit.name, unit.factor, unit.symbol))
        }
        sortUnitsWith { a, b -> a.name.compareTo(b.name, ignoreCase = true) }
    }

    companion object {
        private fun getBYNLocalized(context: Context): ConversionUnit =
            ConversionUnit(context.getString(R.string.currency_byn_name), 1.0, "BYN")
    }
}
