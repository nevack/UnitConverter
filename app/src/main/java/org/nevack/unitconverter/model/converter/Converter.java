package org.nevack.unitconverter.model.converter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.ArrayAdapter;

import org.nevack.unitconverter.model.Unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class Converter {
    protected List<Unit> unitList = new ArrayList<>();
    protected Context context;

    public String convert(String inputValue, int inputValueType, int outputValueType) {
        BigDecimal source = new BigDecimal(inputValue);
        BigDecimal sourceFactor = BigDecimal.valueOf(unitList.get(inputValueType).getFactor());
        BigDecimal resultFactor = BigDecimal.valueOf(unitList.get(outputValueType).getFactor());
        BigDecimal result = source.multiply(sourceFactor).divide(resultFactor, 6, BigDecimal.ROUND_HALF_UP);
        return result.stripTrailingZeros().toPlainString();
    }

    public abstract String getTitle();

    public ArrayAdapter<String> getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        for (Unit unit : unitList) {
            adapter.add(unit.getName());
        }
        return adapter;
    }

    public Spanned getUnitSymbol(int position) {
        return Html.fromHtml(unitList.get(position).getUnitSymbol());
    }
}
