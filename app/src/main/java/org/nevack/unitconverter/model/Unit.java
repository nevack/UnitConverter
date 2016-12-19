package org.nevack.unitconverter.model;

public class Unit {
    private final String name;
    private final double factor;
    private final String unitSymbol;

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
}
