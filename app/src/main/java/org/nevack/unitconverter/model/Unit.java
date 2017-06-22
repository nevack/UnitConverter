package org.nevack.unitconverter.model;

import android.content.Context;

import java.util.Set;

public class Unit {
    private final String name;
    private final double factor;
    private final String unitSymbol;

    private int nameRes;
    private Set<Object> symbolList;

    public Unit(String name, double factor, String unitSymbol) {
        this.name = name;
        this.factor = factor;
        this.unitSymbol = unitSymbol;
    }

    public String getName() {
        return name;
    }

    public double getFactor() {
        return factor;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public String getLocalizedName(Context context) {
        return context.getString(nameRes);
    }

    public String getLocalizedSymbol(Context context) {
        StringBuilder builder = new StringBuilder();
        for (Object object : symbolList) {
            if (object instanceof String) {
                builder.append(object);
                continue;
            }

            if(object instanceof Integer) {
                builder.append(context.getString((Integer) object));
            }
        }

        return builder.toString();
    }
}
