package org.nevack.unitconverter.converter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.nevack.unitconverter.R;
import org.nevack.unitconverter.converter.ConverterContract.ConvertData;
import org.nevack.unitconverter.model.Unit;

import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class ConverterDisplayView extends LinearLayout {

    private final Spinner sourceSpinner;
    private final EditText sourceEditText;
    private final TextView sourceTextView;
    private final EditText resultEditText;
    private final TextView resultTextView;
    private final Spinner resultSpinner;
    private final FloatingActionButton fab;

    private static List<Unit> units;

    public ConverterDisplayView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        inflate(context, R.layout.display, this);

        fab = findViewById(R.id.fab);

        sourceTextView = findViewById(R.id.source_value_symbol);
        resultTextView = findViewById(R.id.result_value_symbol);

        sourceSpinner = findViewById(R.id.source_spinner);
        resultSpinner = findViewById(R.id.result_spinner);

        sourceEditText = findViewById(R.id.source_value);
        resultEditText = findViewById(R.id.result_value);

        fab.setOnClickListener(v -> {
            String temp = sourceEditText.getText().toString();
            sourceEditText.setText(resultEditText.getText().toString());
            resultEditText.setText(temp);
            int position = sourceSpinner.getSelectedItemPosition();
            sourceSpinner.setSelection(resultSpinner.getSelectedItemPosition());
            resultSpinner.setSelection(position);
        });

        sourceTextView.setOnClickListener(v -> sourceSpinner.performClick());

        resultTextView.setOnClickListener(v -> resultSpinner.performClick());
    }

    public void setTextWatcher(TextWatcher watcher) {
        sourceEditText.addTextChangedListener(watcher);
    }

    public void showResult(String result) {
        resultEditText.setText(result);
    }

    public ConvertData getConvertData() {
        return new ConvertData(
                sourceEditText.getText().toString(),
                resultEditText.getText().toString(),
                sourceSpinner.getSelectedItemPosition(),
                resultSpinner.getSelectedItemPosition()
        );
    }

    public String getCopyResult(boolean withUnitSymbols) {
        return resultEditText.getText().toString()
                + (withUnitSymbols ? resultTextView.getText().toString() : "");
    }

    public void showError() {
        resultEditText.setText(R.string.message_error);
    }

    private void setSpinnerAdapter(SpinnerAdapter adapter) {
        sourceSpinner.setAdapter(adapter);
        resultSpinner.setAdapter(adapter);
    }

    public void setUnits(List<Unit> units) {
        ConverterDisplayView.units = units;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        for (Unit unit : ConverterDisplayView.units) {
            adapter.add(unit.getName());
        }

        setSpinnerAdapter(adapter);
    }

    public void erase() {
        ViewGroupOverlay groupOverlay;
        View revealView = new View(getContext());
        Animator revealAnimator;

        final Rect displayRect = new Rect();
        this.getGlobalVisibleRect(displayRect);

        // Make reveal cover the display and status bar.
        revealView.setBottom(displayRect.bottom);
        revealView.setLeft(displayRect.left);
        revealView.setRight(displayRect.right);
        revealView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        groupOverlay = (ViewGroupOverlay) ((Activity) getContext()).getWindow().getDecorView().getOverlay();
        groupOverlay.add(revealView);

        final int[] clearLocation = new int[2];
        this.getLocationInWindow(clearLocation);
        clearLocation[0] += this.getWidth();
        clearLocation[1] += this.getHeight();

        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();

        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        revealAnimator = ViewAnimationUtils.createCircularReveal(revealView,
                revealCenterX, revealCenterY, 0.0f, revealRadius);

        revealAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_longAnimTime));

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                clear();
            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();

        //Play Reveal Animation if Lollipop or higher, and only alpha animation for others
        animatorSet.play(revealAnimator).before(alphaAnimator);

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        final ViewGroupOverlay finalGroupOverlay = groupOverlay;
        final View finalRevealView = revealView;

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                finalGroupOverlay.remove(finalRevealView);
            }
        });

        animatorSet.start();
    }

    public void clear() {
        resultEditText.getEditableText().clear();
        sourceEditText.getEditableText().clear();
    }

    public void appendText(String text) {
        sourceEditText.append(text);
    }

    public void removeLastDigit() {
        String textString = sourceEditText.getText().toString();
        if (textString.length() > 0) {
            sourceEditText.setText(textString.substring(0, textString.length() - 1));
            sourceEditText.setSelection(sourceEditText.getText().length());
        }
    }

    public void setSpinnersCallback(SpinnerListener.Callback callback) {
        resultSpinner.setOnItemSelectedListener(new SpinnerListener(resultTextView, callback));
        sourceSpinner.setOnItemSelectedListener(new SpinnerListener(sourceTextView, callback));
    }

    public void setConvertData(ConvertData convertData) {
        sourceEditText.setText(convertData.getValue());
        resultEditText.setText(convertData.getResult());
        sourceSpinner.setSelection(convertData.getFrom());
        resultSpinner.setSelection(convertData.getTo());
    }

    static class SpinnerListener implements Spinner.OnItemSelectedListener {

        interface Callback {
            void convert();
        }

        private final Callback callback;

        private final TextView textView;

        SpinnerListener(TextView textView, Callback callback) {
            this.callback = callback;
            this.textView = textView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView childAt = (TextView) parent.getChildAt(0);
            if (childAt != null) childAt.setTextColor(Color.WHITE);

            String html = units.get(parent.getSelectedItemPosition()).getUnitSymbol();
            Spanned spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY);

            textView.setText(spanned);
            callback.convert();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            callback.convert();
        }
    }
}
