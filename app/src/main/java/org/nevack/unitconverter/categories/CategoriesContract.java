package org.nevack.unitconverter.categories;

import org.nevack.unitconverter.BasePresenter;
import org.nevack.unitconverter.BaseView;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.converter.Converter;

interface CategoriesContract {

    interface View extends BaseView<Presenter> {

        void showCategories();

    }

    interface Presenter extends BasePresenter {

        void openConverter(EUnitCategory converter);

        void moveConverter(Converter converter, int position);

    }

}
