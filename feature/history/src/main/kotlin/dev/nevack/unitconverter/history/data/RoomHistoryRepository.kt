package dev.nevack.unitconverter.history.data

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import dev.nevack.unitconverter.history.data.db.HistoryDao
import dev.nevack.unitconverter.history.data.db.toEntity
import dev.nevack.unitconverter.history.data.db.toRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomHistoryRepository(
    private val dao: HistoryDao,
) : HistoryRepository {
    override fun observeAll(): Flow<List<HistoryRecord>> = dao.observeAll().map { items -> items.map { it.toRecord() } }

    override suspend fun save(record: HistoryRecord) {
        dao.insertAll(record.toEntity())
    }

    override suspend fun delete(record: HistoryRecord) {
        dao.delete(record.toEntity())
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
