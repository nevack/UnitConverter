package dev.nevack.unitconverter.converter

fun interface ConverterResultSaver {
    suspend fun save(record: ConverterHistoryRecord)
}
