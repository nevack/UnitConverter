package org.nevack.unitconverter.categories;

import android.content.Context;

import org.nevack.unitconverter.converter.ConverterActivity;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class CategoriesPresenter implements CategoriesContract.Presenter {

    private final Context context;

    private final CategoriesContract.View view;

    CategoriesPresenter(Context context, CategoriesContract.View view) {
        this.context = context;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        view.showCategories();
    }

    @Override
    public void openConverter(EUnitCategory converterCategory) {
        context.startActivity(ConverterActivity.getIntent(context, converterCategory.ordinal()));
    }

    @Override
    public void moveConverter(Converter converter, int position) {

    }
}
