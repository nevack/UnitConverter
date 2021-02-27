package dev.nevack.unitconverter.model.converter

import android.content.Context
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.model.ConversionUnit
import dev.nevack.unitconverter.model.nbrb.NBRBRepository

class CurrencyConverter(context: Context) : Converter() {
    internal var repository: NBRBRepository? = null

    init {
        registerUnit(getBYNLocalized(context))
    }

    override suspend fun load() {
        val repository = requireNotNull(repository) { "Repository must not be null!" }
        repository.getUnits().forEach { unit -> registerUnit(unit) }
        sortUnitsWith { a, b -> a.name.compareTo(b.name, ignoreCase = true) }
    }

    companion object {
        private fun getBYNLocalized(context: Context): ConversionUnit {
            return ConversionUnit(context.getString(R.string.currency_byn_name), 1.0, "BYN")
        }
    }
}
