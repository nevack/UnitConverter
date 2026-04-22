package dev.nevack.unitconverter.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ConverterCategory(
    val id: String,
    @param:StringRes val categoryName: Int,
    @param:DrawableRes val icon: Int,
    @param:ColorRes val color: Int,
    val order: Int,
)
