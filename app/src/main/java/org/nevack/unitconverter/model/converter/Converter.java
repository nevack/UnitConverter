package org.nevack.unitconverter.model.converter;

import org.nevack.unitconverter.model.Unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class Converter {
    static final String CUBIC_POSTFIX = "<sup><small>3</small></sup>";
    static final String SQUARE_POSTFIX = "<sup><small>2</small></sup>";

    List<Unit> units = new ArrayList<>();

    public String convert(String inputValue, int inputValueType, int outputValueType) {
        BigDecimal source = new BigDecimal(inputValue);
        BigDecimal sourceFactor = BigDecimal.valueOf(units.get(inputValueType).getFactor());
        BigDecimal resultFactor = BigDecimal.valueOf(units.get(outputValueType).getFactor());
        BigDecimal result = source.multiply(sourceFactor).divide(resultFactor, 9, BigDecimal.ROUND_HALF_UP);
        return result.stripTrailingZeros().toPlainString();
    }

    public abstract int getTitle();

    public List<Unit> getUnits() {
        return units;
    }

    public String getUnitSymbol(int position) {
        return units.get(position).getUnitSymbol();
    }
}
