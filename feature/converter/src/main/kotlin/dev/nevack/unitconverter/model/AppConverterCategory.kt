package dev.nevack.unitconverter.model

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AppConverterCategory(
    val category: ConverterCategory,
    @param:StringRes val categoryName: Int,
    @param:DrawableRes val icon: Int,
    @param:ColorInt val color: Int,
) {
    val id: String
        get() = category.id
}
