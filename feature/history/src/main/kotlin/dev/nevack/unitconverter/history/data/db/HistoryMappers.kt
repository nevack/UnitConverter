package dev.nevack.unitconverter.history.data.db

import dev.nevack.unitconverter.history.HistoryRecord

fun HistoryItem.toRecord(): HistoryRecord =
    HistoryRecord(
        id = id,
        unitFrom = unitFrom,
        unitTo = unitTo,
        valueFrom = valueFrom,
        valueTo = valueTo,
        categoryId = categoryId,
    )

fun HistoryRecord.toEntity(): HistoryItem =
    HistoryItem(
        id = id,
        unitFrom = unitFrom,
        unitTo = unitTo,
        valueFrom = valueFrom,
        valueTo = valueTo,
        categoryId = categoryId,
    )
