package org.nevack.unitconverter.model

val EMPTY = ConversionUnit("", 1.0, "")

data class ConversionUnit(
    val name: String,
    val factor: Double,
    val unitSymbol: String
)
