package org.nevack.unitconverter.model;

public class HistoryItem {
    private final String category;
    private final String valuefrom;
    private final String valueto;
    private final String unitfrom;
    private final String unitto;

    public HistoryItem(String category, String valuefrom, String valueto, String unitfrom, String unitto) {
        this.category = category;
        this.valuefrom = valuefrom;
        this.valueto = valueto;
        this.unitfrom = unitfrom;
        this.unitto = unitto;
    }

    public String getCategory() {
        return category;
    }

    public String getValuefrom() {
        return valuefrom;
    }

    public String getValueto() {
        return valueto;
    }

    public String getUnitfrom() {
        return unitfrom;
    }

    public String getUnitto() {
        return unitto;
    }
}
