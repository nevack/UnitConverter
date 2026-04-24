package dev.nevack.unitconverter.history.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryItem::class], version = 2, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun dao(): HistoryDao
}
