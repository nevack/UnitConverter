package dev.nevack.unitconverter.history

import dev.nevack.unitconverter.history.db.HistoryDao
import dev.nevack.unitconverter.history.db.toEntity
import dev.nevack.unitconverter.history.db.toRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomHistoryRepository(
    private val dao: HistoryDao,
) : HistoryRepository {
    override fun getAll(): Flow<List<HistoryRecord>> = dao.getAll().map { items -> items.map { it.toRecord() } }

    override suspend fun save(record: HistoryRecord) {
        dao.insertAll(record.toEntity())
    }

    override suspend fun delete(record: HistoryRecord) {
        dao.delete(record.toEntity())
    }

    override suspend fun deleteAll() = dao.deleteAll()
}
