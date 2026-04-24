package dev.nevack.unitconverter.model

interface CurrencyUnitsRepository {
    suspend fun getUnits(): List<ConversionUnit>
}
