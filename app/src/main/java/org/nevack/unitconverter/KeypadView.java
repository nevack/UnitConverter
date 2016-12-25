package org.nevack.unitconverter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class KeypadView extends LinearLayout {

    private Button mButton2;
    private Button mButton1;
    private Button mButton4;
    private Button mButton3;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton0;
    private Button mButtonDot;
    private Button mButtonMinus;
    private Button mButtonBackspace;

    private ImageButton mButtonCopy;
    private ImageButton mButtonPaste;

    private EditText editText;

    public KeypadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        inflate(context, R.layout.keypad, this);

        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton6 = (Button) findViewById(R.id.button6);
        mButton7 = (Button) findViewById(R.id.button7);
        mButton8 = (Button) findViewById(R.id.button8);
        mButton9 = (Button) findViewById(R.id.button9);
        mButton0 = (Button) findViewById(R.id.button0);
        mButtonDot = (Button) findViewById(R.id.button_dot);
        mButtonMinus = (Button) findViewById(R.id.button_minus);
        mButtonBackspace = (Button) findViewById(R.id.button_backspace);
        mButtonCopy = (ImageButton) findViewById(R.id.button_copy);
        mButtonPaste = (ImageButton) findViewById(R.id.button_paste);

        mButton1.setOnClickListener(new Listener("1"));
        mButton2.setOnClickListener(new Listener("2"));
        mButton3.setOnClickListener(new Listener("3"));
        mButton4.setOnClickListener(new Listener("4"));
        mButton5.setOnClickListener(new Listener("5"));
        mButton6.setOnClickListener(new Listener("6"));
        mButton7.setOnClickListener(new Listener("7"));
        mButton8.setOnClickListener(new Listener("8"));
        mButton9.setOnClickListener(new Listener("9"));
        mButton0.setOnClickListener(new Listener("0"));
        mButtonDot.setOnClickListener(new Listener("."));
        mButtonMinus.setOnClickListener(new Listener("-"));
        mButtonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textString = editText.getText().toString();
                if( textString.length() > 0 ) {
                    editText.setText(textString.substring(0, textString.length() - 1 ));
                    editText.setSelection(editText.getText().length());
                }
            }
        });
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setCopyListener(OnClickListener click, OnLongClickListener longClick) {
        mButtonCopy.setOnClickListener(click);
        mButtonCopy.setOnLongClickListener(longClick);
    }

    public void setBackspaceLongClickListener(OnLongClickListener listener) {
        mButtonBackspace.setOnLongClickListener(listener);
    }

    private class Listener implements View.OnClickListener {
        final String text;
        Listener(String text)
        {
            this.text = text;
        }
        @Override
        public void onClick(View v) {
            editText.append(text);
        }
    }
}
