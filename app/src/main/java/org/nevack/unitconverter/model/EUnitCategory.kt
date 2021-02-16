package org.nevack.unitconverter.model

import android.content.Context
import org.nevack.unitconverter.R
import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.annotation.ColorRes
import org.nevack.unitconverter.model.converter.*

enum class EUnitCategory(
    @StringRes val categoryName: Int,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int,
    private val creator: (Context) -> Converter
) {
    MASS(
        R.string.mass,
        R.drawable.ic_weight,
        R.color.material_red_500,
        ::MassConverter
    ),
    VOLUME(
        R.string.volume,
        R.drawable.ic_volume,
        R.color.material_green_accent_700,
        ::VolumeConverter
    ),
    TEMPERATURE(
        R.string.temperature,
        R.drawable.ic_temperature,
        R.color.material_purple_500,
        ::TemperatureConverter
    ),
    SPEED(
        R.string.speed,
        R.drawable.ic_speed,
        R.color.material_indigo_500,
        ::SpeedConverter
    ),
    LENGTH(
        R.string.length,
        R.drawable.ic_ruler,
        R.color.material_bluegrey_500,
        ::LengthConverter
    ),
    AREA(
        R.string.area,
        R.drawable.ic_area,
        R.color.material_teal_500,
        ::AreaConverter
    ),
    MEMORY(
        R.string.memory,
        R.drawable.ic_memory,
        R.color.material_blue_500,
        ::MemoryConverter
    ),
    TIME(
        R.string.time,
        R.drawable.ic_timer,
        R.color.material_orange_500,
        ::TimeConverter
    ),
    CURRENCY(
        R.string.currency,
        R.drawable.ic_currency_usd,
        R.color.material_green_800,
        ::CurrencyConverter
    ),
    OTHER(
        R.string.other,
        R.drawable.ic_other,
        R.color.material_deep_purple_500,
        ::OtherConverter
    );

    fun getConverter(context: Context): Converter = creator(context)
}