package org.nevack.unitconverter.converter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroupOverlay
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.doAfterTextChanged
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterContract.ConvertData
import org.nevack.unitconverter.databinding.DisplayBinding
import org.nevack.unitconverter.model.ConversionUnit
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class ConverterDisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: DisplayBinding
    private var conversionUnits: MutableList<ConversionUnit> = mutableListOf()

    init {
        orientation = VERTICAL
        binding = DisplayBinding.inflate(LayoutInflater.from(context), this).apply {
            fab.setOnClickListener {
                val temp = sourceValue.text.toString()
                sourceValue.setText(resultValue.text.toString())
                resultValue.setText(temp)

                val position = sourceSpinner.selectedItemPosition
                sourceSpinner.setSelection(resultSpinner.selectedItemPosition)
                resultSpinner.setSelection(position)
            }
            sourceValueSymbol.setOnClickListener { sourceSpinner.performClick() }
            resultValueSymbol.setOnClickListener { resultSpinner.performClick() }
        }
    }

    fun setTextWatcher(watcher: (text: String) -> kotlin.Unit) {
        binding.sourceValue.doAfterTextChanged {
            val text = it?.toString() ?: return@doAfterTextChanged
            watcher(text)
        }
    }

    fun showResult(result: String?) {
        binding.resultValue.setText(result)
    }

    var convertData: ConvertData
        get() = with(binding) {
            ConvertData(
                sourceValue.text.toString(),
                resultValue.text.toString(),
                sourceSpinner.selectedItemPosition,
                resultSpinner.selectedItemPosition
            )
        }
        set(convertData) = with(binding) {
            sourceValue.setText(convertData.value)
            resultValue.setText(convertData.result)
            sourceSpinner.setSelection(convertData.from)
            resultSpinner.setSelection(convertData.to)
        }

    fun getCopyResult(withUnitSymbols: Boolean): String {
        return (binding.resultValue.text.toString()
                + if (withUnitSymbols) binding.resultValueSymbol.text.toString() else "")
    }

    fun showError() {
        binding.resultValue.setText(R.string.message_error)
    }

    private fun setSpinnerAdapter(adapter: SpinnerAdapter) {
        binding.sourceSpinner.adapter = adapter
        binding.resultSpinner.adapter = adapter
    }

    fun setUnits(conversionUnits: List<ConversionUnit>) {
        this.conversionUnits.clear()
        this.conversionUnits.addAll(conversionUnits)
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)
        this.conversionUnits.forEach { adapter.add(it.name) }
        setSpinnerAdapter(adapter)
    }

    fun erase() {
        val revealView = View(context)
        val revealAnimator: Animator
        val displayRect = Rect()
        this.getGlobalVisibleRect(displayRect)

        // Make reveal cover the display and status bar.
        revealView.bottom = displayRect.bottom
        revealView.left = displayRect.left
        revealView.right = displayRect.right
        revealView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        val groupOverlay = (context as Activity).window.decorView.overlay as ViewGroupOverlay
        groupOverlay.add(revealView)
        var (width, height) = IntArray(2).apply(::getLocationInWindow)
        width += this.width
        height += this.height
        val revealCenterX = width - revealView.left
        val revealCenterY = height - revealView.top
        val x1 = (revealView.left - revealCenterX).toDouble().pow(2)
        val x2 = (revealView.right - revealCenterX).toDouble().pow(2)
        val y = (revealView.top - revealCenterY).toDouble().pow(2)
        val revealRadius = max(sqrt(x1 + y), sqrt(x2 + y)).toFloat()
        revealAnimator = ViewAnimationUtils.createCircularReveal(
            revealView,
            revealCenterX, revealCenterY, 0.0f, revealRadius
        )
        revealAnimator.duration =
            resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        val alphaAnimator: Animator = ObjectAnimator.ofFloat(revealView, ALPHA, 0.0f)
        alphaAnimator.duration =
            resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        alphaAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                clear()
            }
        })
        val animatorSet = AnimatorSet()

        //Play Reveal Animation if Lollipop or higher, and only alpha animation for others
        animatorSet.play(revealAnimator).before(alphaAnimator)
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                groupOverlay.remove(revealView)
            }
        })
        animatorSet.start()
    }

    fun clear() {
        binding.resultValue.editableText.clear()
        binding.sourceValue.editableText.clear()
    }

    fun appendText(text: String?) {
        binding.sourceValue.append(text)
    }

    fun removeLastDigit() {
        val textString = binding.sourceValue.text.toString()
        if (textString.isNotEmpty()) {
            binding.sourceValue.setText(textString.substring(0, textString.length - 1))
            binding.sourceValue.selectAll()
        }
    }

    fun setSpinnersCallback(callback: () -> kotlin.Unit) {
        binding.resultSpinner.onItemSelectedListener =
            SpinnerListener(binding.resultValueSymbol, callback)
        binding.sourceSpinner.onItemSelectedListener =
            SpinnerListener(binding.sourceValueSymbol, callback)
    }

    inner class SpinnerListener(
        private val textView: TextView,
        private val callback: () -> kotlin.Unit
    ) : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val childAt = parent.getChildAt(0) as TextView
            childAt.setTextColor(Color.WHITE)
            val html = conversionUnits[parent.selectedItemPosition].unitSymbol
            val spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.text = spanned
            callback()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            callback()
        }
    }
}