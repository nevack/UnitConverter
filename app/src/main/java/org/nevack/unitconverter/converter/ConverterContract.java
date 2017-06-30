package org.nevack.unitconverter.converter;

import android.support.annotation.ColorRes;

import org.nevack.unitconverter.BasePresenter;
import org.nevack.unitconverter.BaseView;
import org.nevack.unitconverter.model.EUnitCategory;
import org.nevack.unitconverter.model.Unit;

import java.util.List;

public interface ConverterContract {

    interface View extends BaseView<Presenter> {

        void setTitle(int title);

        void setUnits(List<Unit> units);

        ConvertData getConvertData();

        void setConvertData(ConvertData data);

        void appendText(String digit);

        void showResult(String result);

        void clear();

        void showError();

        void setBackgroundColor(@ColorRes int color);
    }

    interface Presenter extends BasePresenter {

        void setConverter(EUnitCategory converter);

        void convert(ConvertData data);

        //String convert(String data, int input, int output);

        void copyResultToClipboard(String result);

        void pasteFromClipboard();

        void saveResultToHistory();

    }

    class ConvertData{
        private final String value;
        private final String result;
        private final int from;
        private final int to;

        public ConvertData(String value, String result, int from, int to) {
            this.value = value;
            this.result = result;
            this.from = from;
            this.to = to;
        }

        public String getValue() {
            return value;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public String getResult() {
            return result;
        }
    }
}
