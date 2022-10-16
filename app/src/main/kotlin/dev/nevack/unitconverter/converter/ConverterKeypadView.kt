package dev.nevack.unitconverter.converter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.KeypadBinding

internal class ConverterKeypadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    fun interface ActionListener {
        fun click(long: Boolean)
    }

    fun interface NumberListener {
        fun click(digit: String)
    }

    private val paint = Paint().apply { color = context.getColor(R.color.keypad_background_color) }
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
        KeypadBinding.inflate(LayoutInflater.from(context), this).apply {
            mButton1 = button1
            mButton2 = button2
            mButton3 = button3
            mButton4 = button4
            mButton5 = button5
            mButton6 = button6
            mButton7 = button7
            mButton8 = button8
            mButton9 = button9
            mButton0 = button0
            mButtonDot = buttonDot
            mButtonMinus = buttonMinus
            mButtonBackspace = buttonBackspace
            mButtonCopy = buttonCopy
            mButtonPaste = buttonPaste
        }
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
        val clickListener = OnClickListener { listener.click(mapper.get(it.id)) }
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
        rect.set(width - mButtonPaste.width - paddingRight, 0, width, height)
        canvas.drawRect(rect, paint)
    }

    private fun View.setActionListener(listener: ActionListener) {
        setOnClickListener { listener.click(false) }
        setOnLongClickListener {
            listener.click(true)
            true
        }
    }

    companion object {
        private val mapper = SparseArray<String>(10)

        init {
            mapper.append(R.id.button0, "0")
            mapper.append(R.id.button1, "1")
            mapper.append(R.id.button2, "2")
            mapper.append(R.id.button3, "3")
            mapper.append(R.id.button4, "4")
            mapper.append(R.id.button5, "5")
            mapper.append(R.id.button6, "6")
            mapper.append(R.id.button7, "7")
            mapper.append(R.id.button8, "8")
            mapper.append(R.id.button9, "9")
            mapper.append(R.id.button_dot, ".")
            mapper.append(R.id.button_minus, "-")
        }
    }
}
