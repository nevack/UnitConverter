package dev.nevack.unitconverter.history

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HistoryCategory(
    val id: String,
    @param:StringRes val nameRes: Int,
    @param:DrawableRes val iconRes: Int,
    @param:ColorRes val colorRes: Int,
)
