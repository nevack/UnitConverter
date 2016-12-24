package org.nevack.unitconverter.model;

import java.io.Serializable;

public class HistoryItem implements Serializable {
    private EUnitCategory category;
    private Unit from;
    private Unit to;
    private double fromValue;
    private double toValue;

    public HistoryItem(EUnitCategory category, Unit from, Unit to, double fromValue, double toValue) {
        this.category = category;
        this.from = from;
        this.to = to;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public EUnitCategory getCategory() {
        return category;
    }

    public Unit getFrom() {
        return from;
    }

    public Unit getTo() {
        return to;
    }

    public double getFromValue() {
        return fromValue;
    }

    public double getToValue() {
        return toValue;
    }
}
