package org.nevack.unitconverter.model;

public class Currency {
    public final String charCode;
    public final String name;
    public final String rate;

    public Currency(String charCode, String name, String rate) {
        this.charCode = charCode;
        this.name = name;
        this.rate = rate;
    }
}
