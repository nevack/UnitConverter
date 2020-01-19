package org.nevack.unitconverter.model;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.converter.*;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.ic_weight, R.color.material_red_500, MassConverter::new),
    VOLUME(R.string.volume, R.drawable.ic_volume, R.color.material_green_accent_700, VolumeConverter::new),
    TEMPERATURE(R.string.temperature, R.drawable.ic_temperature, R.color.material_purple_500, TemperatureConverter::new),
    SPEED(R.string.speed, R.drawable.ic_speed, R.color.material_indigo_500, SpeedConverter::new),
    LENGTH(R.string.length, R.drawable.ic_ruler, R.color.material_bluegrey_500, LengthConverter::new),
    AREA(R.string.area, R.drawable.ic_area, R.color.material_teal_500, AreaConverter::new),
    MEMORY(R.string.memory, R.drawable.ic_memory, R.color.material_blue_500, MemoryConverter::new),
    TIME(R.string.time, R.drawable.ic_timer, R.color.material_orange_500, TimeConverter::new),
    CURRENCY(R.string.currency, R.drawable.ic_currency_usd, R.color.material_green_800, CurrencyConverter::new),
    OTHER(R.string.other, R.drawable.ic_other, R.color.material_deep_purple_500, OtherConverter::new);

    @StringRes private final int name;
    @DrawableRes private final int icon;
    @ColorRes private final int color;
    private final Creator<Converter> creator;

    EUnitCategory(int name, int icon, int color, Creator<Converter> converterCreator) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.creator = converterCreator;
    }

    public @StringRes int getName() {
        return name;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }

    public @ColorRes int getColor() {
        return color;
    }

    public Converter getConverter(Context context) {
        return creator.create(context);
    }
}