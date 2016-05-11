package org.nevack.unitconverter.model;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.converter.*;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.weight, R.color.unitRed, R.drawable.ic_weight, MassConverter.class),
    VOLUME(R.string.volume, R.drawable.volume, R.color.unitGreen, R.drawable.ic_volume, VolumeConverter.class),
    TEMPERATURE(R.string.temperature, R.drawable.temperature, R.color.unitPurple, R.drawable.ic_temperature, TemperatureConverter.class),
    SPEED(R.string.speed, R.drawable.speed, R.color.unitDBlue, R.drawable.ic_speed, SpeedConverter.class),
    LENGTH(R.string.length, R.drawable.ruler, R.color.unitGrey, R.drawable.ic_ruler, LengthConverter.class),
    AREA(R.string.area, R.drawable.area, R.color.unitRed, R.drawable.ic_area, AreaConverter.class),
    MEMORY(R.string.memory, R.drawable.memory, R.color.unitLBlue, R.drawable.ic_memory, MemoryConverter.class),
    TIME(R.string.time, R.drawable.timer, R.color.unitOrange, R.drawable.ic_timer, TimeConverter.class),
    CURRENCY(R.string.currency, R.drawable.currency, R.color.unitDGreen, R.drawable.ic_currency, CurrencyConverter.class),
    OTHER(R.string.other, R.drawable.other, R.color.unitRed, R.drawable.ic_other, OtherConverter.class);

    private int unitNameResID;
    private int unitIconResID;
    private int unitColorResID;
    private int unitNavIconResID;
    private Class<? extends Converter> converter;

    EUnitCategory(int unitNameResID, int unitIconResID, int unitColorResID, int unitNavIconResID, Class<? extends Converter> converter) {
        this.unitNameResID = unitNameResID;
        this.unitIconResID = unitIconResID;
        this.unitColorResID = unitColorResID;
        this.unitNavIconResID = unitNavIconResID;
        this.converter = converter;
    }

    public int getNameResID() {
        return unitNameResID;
    }

    public int getIconResID() {
        return unitIconResID;
    }

    public int getColorResID() {
        return unitColorResID;
    }

    public int getNavIconResID() {
        return unitNavIconResID;
    }

    public Converter getConverter(Context context) {
        try {
            return converter.getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            return new OtherConverter(context);
        }
    }
}
