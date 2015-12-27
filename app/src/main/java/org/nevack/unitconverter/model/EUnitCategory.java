package org.nevack.unitconverter.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.nevack.unitconverter.R;

public enum EUnitCategory {
    MASS(R.string.mass, R.drawable.weight, R.color.unitRed), //Done
    VOLUME(R.string.volume, R.drawable.volume, R.color.unitGreen), //Done
    TEMPERATURE(R.string.temperature, R.drawable.temperature, R.color.unitPurple), //Done
    SPEED(R.string.speed, R.drawable.speed, R.color.unitDBlue), //Done
    LENGTH(R.string.length, R.drawable.ruler, R.color.unitGrey), //Done
    MEMORY(R.string.memory, R.drawable.memory, R.color.unitLBlue),
    TIME(R.string.time, R.drawable.timer, R.color.unitRed),
    OTHER(R.string.other, R.drawable.force, R.color.unitOrange);

    private int unitNameResID;
    private int unitIconResID;
    private int unitColorResID;

    EUnitCategory(int unitNameResID, int unitIconResID, int unitColorResID) {
        this.unitNameResID = unitNameResID;
        this.unitIconResID = unitIconResID;
        this.unitColorResID = unitColorResID;
    }

    public String getName(Context context) {
        return context.getString(unitNameResID);
    }

    public int getUnitIconResID() {
        return unitIconResID;
    }

    public int getColor(Context context) {
        return ContextCompat.getColor(context, unitColorResID);
    }
}
