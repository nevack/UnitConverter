package dev.nevack.unitconverter.history.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<HistoryItem>

    @Query("SELECT * FROM history WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<HistoryItem>

    @Insert
    fun insertAll(vararg items: HistoryItem)

    @Delete
    fun delete(item: HistoryItem)

    @Query("DELETE FROM history")
    fun deleteAll()
}
