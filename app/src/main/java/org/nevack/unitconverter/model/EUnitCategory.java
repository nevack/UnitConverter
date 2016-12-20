package org.nevack.unitconverter.model;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.converter.*;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.ic_weight, R.color.unitRed, MassConverter.class),
    VOLUME(R.string.volume, R.drawable.ic_volume, R.color.unitGreen, VolumeConverter.class),
    TEMPERATURE(R.string.temperature, R.drawable.ic_temperature, R.color.unitPurple, TemperatureConverter.class),
    SPEED(R.string.speed, R.drawable.ic_speed, R.color.unitDBlue, SpeedConverter.class),
    LENGTH(R.string.length, R.drawable.ic_ruler, R.color.unitGrey, LengthConverter.class),
    AREA(R.string.area, R.drawable.ic_area, R.color.unitRed, AreaConverter.class),
    MEMORY(R.string.memory, R.drawable.ic_memory, R.color.unitLBlue, MemoryConverter.class),
    TIME(R.string.time, R.drawable.ic_timer, R.color.unitOrange, TimeConverter.class),
    CURRENCY(R.string.currency, R.drawable.ic_currency_usd, R.color.unitDGreen, NewCurrencyConverter.class),
    OTHER(R.string.other, R.drawable.ic_other, R.color.unitRed, OtherConverter.class);

    private final int unitNameResID;
    private final int unitIconResID;
    private final int unitColorResID;
    private final Class<? extends Converter> converter;

    EUnitCategory(int unitNameResID, int unitIconResID, int unitColorResID, Class<? extends Converter> converter) {
        this.unitNameResID = unitNameResID;
        this.unitIconResID = unitIconResID;
        this.unitColorResID = unitColorResID;
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

    public Converter getConverter(Context context) {
        try {
            return converter.getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
            return new OtherConverter(context);
        }
    }
}
