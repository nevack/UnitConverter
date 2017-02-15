package org.nevack.unitconverter.model;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.converter.*;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.ic_weight, R.color.material_red_500, MassConverter.class),
    VOLUME(R.string.volume, R.drawable.ic_volume, R.color.material_green_accent_700, VolumeConverter.class),
    TEMPERATURE(R.string.temperature, R.drawable.ic_temperature, R.color.material_purple_500, TemperatureConverter.class),
    SPEED(R.string.speed, R.drawable.ic_speed, R.color.material_indigo_500, SpeedConverter.class),
    LENGTH(R.string.length, R.drawable.ic_ruler, R.color.material_bluegrey_500, LengthConverter.class),
    AREA(R.string.area, R.drawable.ic_area, R.color.material_teal_500, AreaConverter.class),
    MEMORY(R.string.memory, R.drawable.ic_memory, R.color.material_blue_500, MemoryConverter.class),
    TIME(R.string.time, R.drawable.ic_timer, R.color.material_orange_500, TimeConverter.class),
    CURRENCY(R.string.currency, R.drawable.ic_currency_usd, R.color.material_green_800, CurrencyConverter.class),
    OTHER(R.string.other, R.drawable.ic_other, R.color.material_deep_purple_500, OtherConverter.class);

    private final int name;
    private final int icon;
    private final int color;
    private final Class<? extends Converter> converter;

    EUnitCategory(int name, int icon, int unitColorResID, Class<? extends Converter> converter) {
        this.name = name;
        this.icon = icon;
        this.color = unitColorResID;
        this.converter = converter;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    public Converter getConverter(Context context) {
        try {
            return converter.getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            return new OtherConverter(context);
        }
    }
}
