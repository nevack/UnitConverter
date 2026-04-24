package dev.nevack.unitconverter.history

data class HistoryRecord(
    val id: Int = 0,
    val unitFrom: String,
    val unitTo: String,
    val valueFrom: String,
    val valueTo: String,
    val categoryId: String,
)
