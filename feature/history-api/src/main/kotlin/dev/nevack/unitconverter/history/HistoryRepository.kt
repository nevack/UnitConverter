package dev.nevack.unitconverter.history

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeAll(): Flow<List<HistoryRecord>>

    suspend fun save(record: HistoryRecord)

    suspend fun delete(record: HistoryRecord)

    suspend fun deleteAll()
}
