package org.nevack.unitconverter.model;

public class HistoryItem {
    private final int category;
    private final String valueFrom;
    private final String valueTo;
    private final String unitFrom;
    private final String unitTo;

    public HistoryItem
            (int category, String valueFrom, String valueTo, String unitFrom, String unitTo) {
        this.category = category;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
        this.unitFrom = unitFrom;
        this.unitTo = unitTo;
    }

    public int getCategory() {
        return category;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public String getValueTo() {
        return valueTo;
    }

    public String getUnitFrom() {
        return unitFrom;
    }

    public String getUnitTo() {
        return unitTo;
    }
}
