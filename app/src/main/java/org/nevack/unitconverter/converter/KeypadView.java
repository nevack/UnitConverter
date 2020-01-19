package org.nevack.unitconverter.converter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import org.nevack.unitconverter.R;

public class KeypadView extends LinearLayoutCompat {

    public interface ActionListener {
        void longClick();
        void singleClick();
    }

    public interface NumberListener {
        void click(int number);
    }

    private static final SparseIntArray mapper = new SparseIntArray(10);

    static {
        mapper.append(R.id.button0, 0);
        mapper.append(R.id.button1, 1);
        mapper.append(R.id.button2, 2);
        mapper.append(R.id.button3, 3);
        mapper.append(R.id.button4, 4);
        mapper.append(R.id.button5, 5);
        mapper.append(R.id.button6, 6);
        mapper.append(R.id.button7, 7);
        mapper.append(R.id.button8, 8);
        mapper.append(R.id.button9, 9);
    }

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

    private final ImageButton mButtonCopy;
    private final ImageButton mButtonPaste;

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

    public void setOnCopyListeners(ActionListener listener) {
        setActionListener(mButtonCopy, listener);
    }

    public void setOnPasteListener(OnClickListener listener) {
        mButtonPaste.setOnClickListener(listener);
    }

    public void setBackspaceListeners(ActionListener listener) {
        setActionListener(mButtonBackspace, listener);
    }

    public void setNumericListener(NumberListener listener) {
        final View.OnClickListener clickListener = v -> {
            final int id = v.getId();
            final int value = mapper.get(id, -1);
            if (value != -1) {
                listener.click(value);
            }
        };

        mButton1.setOnClickListener(clickListener);
        mButton2.setOnClickListener(clickListener);
        mButton3.setOnClickListener(clickListener);
        mButton4.setOnClickListener(clickListener);
        mButton5.setOnClickListener(clickListener);
        mButton6.setOnClickListener(clickListener);
        mButton7.setOnClickListener(clickListener);
        mButton8.setOnClickListener(clickListener);
        mButton9.setOnClickListener(clickListener);
        mButton0.setOnClickListener(clickListener);
        mButtonDot.setOnClickListener(clickListener);
        mButtonMinus.setOnClickListener(clickListener);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
            getChildAt(index).dispatchApplyWindowInsets(insets);

        return insets;
    }

    private static void setActionListener(View view, ActionListener listener) {
        view.setOnClickListener(v -> listener.singleClick());
        view.setOnLongClickListener(v -> {
            listener.longClick();
            return true;
        });
    }
}
