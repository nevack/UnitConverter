package org.nevack.unitconverter.model.converter;

import android.content.Context;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.model.Unit;

import java.math.BigDecimal;

public class TemperatureConverter extends Converter {
    public TemperatureConverter(Context context) {
        super(context);

        units.add(new Unit(context.getString(R.string.kelvin), 1d, context.getString(R.string.kelvinsymbol)));
        units.add(new Unit(context.getString(R.string.celsius), 1d, context.getString(R.string.celsiussymbol)));
        units.add(new Unit(context.getString(R.string.fahrenheit), 1d, context.getString(R.string.fahrenheitsymbol)));
    }

    @Override
    public String convert(String inputValue, int inputValueType, int outputValueType) {

        if (inputValueType == outputValueType) return inputValue;

        BigDecimal sourceValue = new BigDecimal(inputValue);
        String resultValue;

        switch (inputValueType) {
            case 0:
                if (sourceValue.compareTo(BigDecimal.ZERO) == -1) resultValue =  "Error!";
                else {
                    if (outputValueType == 1) {
                        resultValue = sourceValue.add(new BigDecimal("-273"))
                                .stripTrailingZeros()
                                .toPlainString();
                    } else {
                        resultValue = sourceValue.multiply(new BigDecimal("1.8"))
                                .add(new BigDecimal("-459.67"))
                                .stripTrailingZeros()
                                .toPlainString();
                    }
                }
                break;
            case 1:
                if (sourceValue.compareTo(new BigDecimal("-273")) == -1) resultValue = "Error!";
                else {
                    if (outputValueType == 0) {
                        resultValue = sourceValue.add(new BigDecimal("273"))
                                .stripTrailingZeros()
                                .toPlainString();
                    } else {
                        resultValue = sourceValue.multiply(new BigDecimal("1.8"))
                                .add(new BigDecimal("32"))
                                .stripTrailingZeros()
                                .toPlainString();
                    }
                }
                break;
            case 2:
                if (sourceValue.compareTo(new BigDecimal("-459.67")) == -1) resultValue = "Error!";
                else {
                    if (outputValueType == 0) {
                        resultValue = sourceValue.add(new BigDecimal("459.67"))
                                .multiply(new BigDecimal("5"))
                                .divide(new BigDecimal("9"), 3, BigDecimal.ROUND_HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString();
                    } else {
                        resultValue = sourceValue.add(new BigDecimal("-32"))
                                .divide(new BigDecimal("1.8"), 3, BigDecimal.ROUND_HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString();
                    }
                }
                break;
            default:
                resultValue = "Error!";
                break;
        }

        return resultValue;
    }

    public String getTitle() {
        return context.getString(R.string.temperature);
    }
}
