package org.nevack.unitconverter.converter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageButton;

import org.nevack.unitconverter.R;

public class KeypadView extends LinearLayoutCompat {

    private final Button mButton1;
    private final Button mButton2;
    private final Button mButton3;
    private final Button mButton4;
    private final Button mButton5;
    private final Button mButton6;
    private final Button mButton7;
    private final Button mButton8;
    private final Button mButton9;
    private final Button mButton0;
    private final Button mButtonDot;
    private final Button mButtonMinus;
    private final Button mButtonBackspace;

    private ImageButton mButtonCopy;
    private ImageButton mButtonPaste;

    public KeypadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        inflate(context, R.layout.keypad, this);

        mButton1 = findViewById(R.id.button1);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mButton5 = findViewById(R.id.button5);
        mButton6 = findViewById(R.id.button6);
        mButton7 = findViewById(R.id.button7);
        mButton8 = findViewById(R.id.button8);
        mButton9 = findViewById(R.id.button9);
        mButton0 = findViewById(R.id.button0);
        mButtonDot = findViewById(R.id.button_dot);
        mButtonMinus = findViewById(R.id.button_minus);
        mButtonBackspace = findViewById(R.id.button_backspace);
        mButtonCopy = findViewById(R.id.button_copy);
        mButtonPaste = findViewById(R.id.button_paste);
    }

    public void setOnCopyListeners(OnClickListener click, OnLongClickListener longClick) {
        mButtonCopy.setOnClickListener(click);
        mButtonCopy.setOnLongClickListener(longClick);
    }

    public void setOnPasteListener(OnClickListener listener) {
        mButtonPaste.setOnClickListener(listener);
    }

    public void setBackspaceListeners(OnClickListener click, OnLongClickListener longClick) {
        mButtonBackspace.setOnClickListener(click);
        mButtonBackspace.setOnLongClickListener(longClick);
    }

    public void setNumericListener(OnClickListener listener) {
        mButton1.setOnClickListener(listener);
        mButton2.setOnClickListener(listener);
        mButton3.setOnClickListener(listener);
        mButton4.setOnClickListener(listener);
        mButton5.setOnClickListener(listener);
        mButton6.setOnClickListener(listener);
        mButton7.setOnClickListener(listener);
        mButton8.setOnClickListener(listener);
        mButton9.setOnClickListener(listener);
        mButton0.setOnClickListener(listener);
        mButtonDot.setOnClickListener(listener);
        mButtonMinus.setOnClickListener(listener);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
            getChildAt(index).dispatchApplyWindowInsets(insets);

        return insets;
    }
}
