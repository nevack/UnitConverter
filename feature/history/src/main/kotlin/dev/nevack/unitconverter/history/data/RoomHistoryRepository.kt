package dev.nevack.unitconverter.history.data

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import dev.nevack.unitconverter.history.data.db.HistoryDao
import dev.nevack.unitconverter.history.data.db.toEntity
import dev.nevack.unitconverter.history.data.db.toRecord
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
