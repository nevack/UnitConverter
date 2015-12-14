package org.nevack.unitconverter.model;

public class UnitCategory {
    private String name;
    private int icon;
    private int color;

    public UnitCategory(String name, int icon, int color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }
}
