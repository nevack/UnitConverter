package dev.nevack.unitconverter.history

interface HistoryRepository {
    suspend fun getAll(): List<HistoryRecord>

    suspend fun save(record: HistoryRecord)

    suspend fun delete(record: HistoryRecord)

    suspend fun deleteAll()
}
