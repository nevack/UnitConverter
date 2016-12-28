package org.nevack.unitconverter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
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

import org.nevack.unitconverter.model.Unit;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;
import static org.nevack.unitconverter.R.layout.display;


public class ConverterDisplayView extends LinearLayout {

    private static final String COPY_RESULT = "converter_result";

    private Spinner sourceSpinner;
    private EditText sourceEditText;
    private TextView sourceTextView;
    private EditText resultEditText;
    private TextView resultTextView;
    private Spinner resultSpinner;
    private FloatingActionButton fab;

    private List<Unit> units;

    interface DisplayCallback {
        String convert(String value, int from, int to);
    }

    private DisplayCallback callback;

    public ConverterDisplayView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        callback = (DisplayCallback) context;

        setOrientation(VERTICAL);

        inflate(context, display, this);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        sourceTextView = (TextView) findViewById(R.id.sourcevaluesymbol);
        resultTextView = (TextView) findViewById(R.id.resultvaluesymbol);

        sourceSpinner = (Spinner) findViewById(R.id.sourcespinner);
        sourceSpinner.setOnItemSelectedListener(new SpinnerListener(sourceTextView));

        resultSpinner = (Spinner) findViewById(R.id.resultspinner);
        resultSpinner.setOnItemSelectedListener(new SpinnerListener(resultTextView));

        sourceEditText = (EditText) findViewById(R.id.sourcevalue);
        sourceEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (sourceEditText.getText().toString().equals(".")) {
                    sourceEditText.setText("0.");
                    sourceEditText.setSelection(sourceEditText.getText().toString().length());
                }

                if (sourceEditText.getText().toString().equals("-.")) {
                    sourceEditText.setText("-0.");
                    sourceEditText.setSelection(sourceEditText.getText().toString().length());
                }

                if (!sourceEditText.getText().toString().equals("-")) convert();
            }
        });
        resultEditText = (EditText) findViewById(R.id.resultvalue);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = sourceEditText.getText().toString();
                sourceEditText.setText(resultEditText.getText().toString());
                resultEditText.setText(temp);
                int position = sourceSpinner.getSelectedItemPosition();
                sourceSpinner.setSelection(resultSpinner.getSelectedItemPosition());
                resultSpinner.setSelection(position);
                convert();
            }
        });

        sourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { sourceSpinner.performClick(); }
        });

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { resultSpinner.performClick(); }
        });
    }

    private void setSpinnerAdapter(SpinnerAdapter adapter) {
        sourceSpinner.setAdapter(adapter);
        resultSpinner.setAdapter(adapter);
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        for (Unit unit : this.units) {
            adapter.add(unit.getName());
        }

        setSpinnerAdapter(adapter);
    }

    public void erase() {
        ViewGroupOverlay groupOverlay = null;
        View revealView = new View(getContext());
        Animator revealAnimator = null;

        final Rect displayRect = new Rect();
        this.getGlobalVisibleRect(displayRect);

        // Make reveal cover the display and status bar.
        revealView.setBottom(displayRect.bottom);
        revealView.setLeft(displayRect.left);
        revealView.setRight(displayRect.right);
        revealView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            groupOverlay = (ViewGroupOverlay) ((Activity) getContext()).getWindow().getDecorView().getOverlay();
            groupOverlay.add(revealView);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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
        }

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                resultEditText.getEditableText().clear();
                sourceEditText.getEditableText().clear();
            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();

        //Play Reveal Animation if Lollipop or higher, and only alpha animation for others
        if (revealAnimator != null) {
            animatorSet.play(revealAnimator).before(alphaAnimator);
        } else animatorSet.play(alphaAnimator);

        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        final ViewGroupOverlay finalGroupOverlay = groupOverlay;
        final View finalRevealView = revealView;

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (finalGroupOverlay != null) {
                        finalGroupOverlay.remove(finalRevealView);
                    }
                }
            }
        });

        animatorSet.start();
    }

    private void convert() {
        resultEditText.setText(
                callback.convert(sourceEditText.getText().toString(),
                        sourceSpinner.getSelectedItemPosition(),
                        resultSpinner.getSelectedItemPosition())
        );
    }

    public void copyResultToClipboard(boolean withUnitSymbol) {
        String textToCopy = "";
        if (!resultEditText.getText().toString().equals("")) {
            textToCopy = resultEditText.getText().toString();
        }
        if (withUnitSymbol)
        {
            textToCopy += resultTextView.getText().toString();
        }
        ClipboardManager clipboard = (ClipboardManager)
                getContext().getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(COPY_RESULT, textToCopy));
        Snackbar.make(this, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show();
    }

    public void setupWithKeypad(KeypadView view) {
        view.setEditText(sourceEditText);

        view.setCopyListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyResultToClipboard(false);
                    }
                },
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        copyResultToClipboard(true);
                        return true;
                    }
                }
        );

        view.setBackspaceLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                erase();
                return true;
            }
        });
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {

        private TextView textView;

        SpinnerListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            textView.setText(Html.fromHtml(units.get(parent.getSelectedItemPosition()).getUnitSymbol()));
            convert();
        }

        @Override public void onNothingSelected(AdapterView<?> parent) { convert(); }
    }
}
