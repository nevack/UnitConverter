package dev.nevack.unitconverter.history.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): Flow<List<HistoryItem>>

    @Query("SELECT * FROM history WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<HistoryItem>

    @Insert
    suspend fun insertAll(vararg items: HistoryItem)

    @Delete
    suspend fun delete(item: HistoryItem)

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}
