package org.nevack.unitconverter.categories;

import android.content.Context;

import org.nevack.unitconverter.converter.ConverterActivity;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

public class CategoriesPresenter implements CategoriesContract.Presenter {

    private final Context mContext;

    private final CategoriesContract.View mView;

    public CategoriesPresenter(Context context, CategoriesContract.View view){
        mContext = context;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void openConverter(EUnitCategory converterCategory) {
        mContext.startActivity(ConverterActivity.getIntent(mContext, converterCategory.ordinal()));
    }

    @Override
    public void moveConverter(Converter converter, int position) {

    }
}
