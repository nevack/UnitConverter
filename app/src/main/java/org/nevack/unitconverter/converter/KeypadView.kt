package org.nevack.unitconverter.converter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import org.nevack.unitconverter.R

class KeypadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    interface ActionListener {
        fun longClick()
        fun singleClick()
    }

    fun interface NumberListener {
        fun click(number: Int)
    }

    private val paint = Paint().apply { color = context.getColor(R.color.keypad_light_color) }
    private val rect = Rect()

    private val mButton1: Button
    private val mButton2: Button
    private val mButton3: Button
    private val mButton4: Button
    private val mButton5: Button
    private val mButton6: Button
    private val mButton7: Button
    private val mButton8: Button
    private val mButton9: Button
    private val mButton0: Button
    private val mButtonDot: Button
    private val mButtonMinus: Button
    private val mButtonBackspace: Button
    private val mButtonCopy: ImageButton
    private val mButtonPaste: ImageButton

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.keypad, this, true)
        mButton1 = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
        mButton5 = findViewById(R.id.button5)
        mButton6 = findViewById(R.id.button6)
        mButton7 = findViewById(R.id.button7)
        mButton8 = findViewById(R.id.button8)
        mButton9 = findViewById(R.id.button9)
        mButton0 = findViewById(R.id.button0)
        mButtonDot = findViewById(R.id.button_dot)
        mButtonMinus = findViewById(R.id.button_minus)
        mButtonBackspace = findViewById(R.id.button_backspace)
        mButtonCopy = findViewById(R.id.button_copy)
        mButtonPaste = findViewById(R.id.button_paste)
    }
    fun setOnCopyListeners(listener: ActionListener) {
        mButtonCopy.setActionListener(listener)
    }

    fun setOnPasteListener(listener: OnClickListener) {
        mButtonPaste.setOnClickListener(listener)
    }

    fun setBackspaceListeners(listener: ActionListener) {
        mButtonBackspace.setActionListener(listener)
    }

    fun setNumericListener(listener: NumberListener) {
        val clickListener = OnClickListener { v: View ->
            val id = v.id
            val value = mapper[id, -1]
            if (value != -1) {
                listener.click(value)
            }
        }
        mButton1.setOnClickListener(clickListener)
        mButton2.setOnClickListener(clickListener)
        mButton3.setOnClickListener(clickListener)
        mButton4.setOnClickListener(clickListener)
        mButton5.setOnClickListener(clickListener)
        mButton6.setOnClickListener(clickListener)
        mButton7.setOnClickListener(clickListener)
        mButton8.setOnClickListener(clickListener)
        mButton9.setOnClickListener(clickListener)
        mButton0.setOnClickListener(clickListener)
        mButtonDot.setOnClickListener(clickListener)
        mButtonMinus.setOnClickListener(clickListener)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rect[width - mButtonPaste.width - paddingRight, 0, width] = height
        canvas.drawRect(rect, paint)
    }

    private fun View.setActionListener(listener: ActionListener) {
        setOnClickListener { listener.singleClick() }
        setOnLongClickListener {
            listener.longClick()
            true
        }
    }

    companion object {
        private val mapper = SparseIntArray(10)

        init {
            mapper.append(R.id.button0, 0)
            mapper.append(R.id.button1, 1)
            mapper.append(R.id.button2, 2)
            mapper.append(R.id.button3, 3)
            mapper.append(R.id.button4, 4)
            mapper.append(R.id.button5, 5)
            mapper.append(R.id.button6, 6)
            mapper.append(R.id.button7, 7)
            mapper.append(R.id.button8, 8)
            mapper.append(R.id.button9, 9)
        }
    }
}