package dev.nevack.unitconverter.converter

data class ConverterHistoryRecord(
    val unitFrom: String,
    val unitTo: String,
    val valueFrom: String,
    val valueTo: String,
    val categoryId: String,
)
