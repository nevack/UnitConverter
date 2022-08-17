package dev.nevack.unitconverter.history.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "unit_from") val unitFrom: String,
    @ColumnInfo(name = "unit_to") val unitTo: String,
    @ColumnInfo(name = "value_from") val valueFrom: String,
    @ColumnInfo(name = "value_to") val valueTo: String,
    @ColumnInfo(name = "category") val category: Int,
)
