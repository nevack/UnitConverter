package org.nevack.unitconverter.model;

import android.content.Context;

import org.nevack.unitconverter.model.converter.Converter;

public interface Creator<T extends Converter> {
    T create(Context context);
}
