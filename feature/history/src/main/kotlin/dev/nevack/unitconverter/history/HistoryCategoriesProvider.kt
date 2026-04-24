package dev.nevack.unitconverter.history

fun interface HistoryCategoriesProvider {
    operator fun invoke(): List<HistoryCategory>
}
