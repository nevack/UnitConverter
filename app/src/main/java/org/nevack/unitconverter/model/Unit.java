package org.nevack.unitconverter.model;

public class Unit {
    private String name;
    private double factor;

    public Unit(String name, double factor) {
        this.name = name;
        this.factor = factor;
    }

    public String getName() {
        return name;
    }

    public double getFactor() {
        return factor;
    }
}
