package dev.nevack.unitconverter.history

import dev.nevack.unitconverter.history.db.HistoryDao
import dev.nevack.unitconverter.history.db.toEntity
import dev.nevack.unitconverter.history.db.toRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomHistoryRepository(
    private val dao: HistoryDao,
) : HistoryRepository {
    override suspend fun getAll(): List<HistoryRecord> =
        withContext(Dispatchers.IO) {
            dao.getAll().map { it.toRecord() }
        }

    override suspend fun save(record: HistoryRecord) {
        withContext(Dispatchers.IO) {
            dao.insertAll(record.toEntity())
        }
    }

    override suspend fun delete(record: HistoryRecord) {
        withContext(Dispatchers.IO) {
            dao.delete(record.toEntity())
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}
