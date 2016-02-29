package org.nevack.unitconverter.model;

import org.nevack.unitconverter.R;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.weight, R.color.unitRed, R.drawable.ic_weight),
    VOLUME(R.string.volume, R.drawable.volume, R.color.unitGreen, R.drawable.ic_volume),
    TEMPERATURE(R.string.temperature, R.drawable.temperature, R.color.unitPurple, R.drawable.ic_temperature),
    SPEED(R.string.speed, R.drawable.speed, R.color.unitDBlue, R.drawable.ic_speed),
    LENGTH(R.string.length, R.drawable.ruler, R.color.unitGrey, R.drawable.ic_ruler),
    AREA(R.string.area, R.drawable.area, R.color.unitRed, R.drawable.ic_area),
    MEMORY(R.string.memory, R.drawable.memory, R.color.unitLBlue, R.drawable.ic_memory),
    TIME(R.string.time, R.drawable.timer, R.color.unitOrange, R.drawable.ic_timer),
    CURRENCY(R.string.currency, R.drawable.currency, R.color.unitDGreen, R.drawable.ic_currency),
    OTHER(R.string.other, R.drawable.other, R.color.unitRed, R.drawable.ic_other);

    private int unitNameResID;
    private int unitIconResID;
    private int unitColorResID;
    private int unitNavIconResID;

    EUnitCategory(int unitNameResID, int unitIconResID, int unitColorResID, int unitNavIconResID) {
        this.unitNameResID = unitNameResID;
        this.unitIconResID = unitIconResID;
        this.unitColorResID = unitColorResID;
        this.unitNavIconResID = unitNavIconResID;
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
}
