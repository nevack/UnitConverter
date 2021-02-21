package dev.nevack.unitconverter.converter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroupOverlay
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.DisplayBinding
import dev.nevack.unitconverter.model.ConversionUnit
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

internal class ConverterDisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: DisplayBinding
    private var conversionUnits: MutableList<ConversionUnit> = mutableListOf()

    private var sourceIndex = 0
        set(value) {
            field = value
            updateSuffixes()
        }
    private var resultIndex = 0
        set(value) {
            field = value
            updateSuffixes()
        }

    init {
        orientation = VERTICAL
        binding = DisplayBinding.inflate(LayoutInflater.from(context), this).apply {
            fab.setOnClickListener {
                convertData = convertData.swap()
            }
            sourceValue.showSoftInputOnFocus = false
            resultValue.showSoftInputOnFocus = false
        }
    }

    fun onTextChanged(watcher: (text: String) -> Unit) {
        binding.sourceValue.doAfterTextChanged {
            it?.run { watcher(toString()) }
        }
    }

    fun showResult(result: String) {
        binding.resultValue.setText(result)
    }

    var convertData: ConvertData
        get() = with(binding) {
            ConvertData(
                sourceValue.text.toString(),
                resultValue.text.toString(),
                sourceIndex,
                resultIndex
            )
        }
        set(convertData) = with(binding) {
            sourceValue.setText(convertData.value)
            resultValue.setText(convertData.result)
            sourceIndex = convertData.from
            resultIndex = convertData.to
            sourceSpinner.setText(conversionUnits[sourceIndex].name, false)
            resultSpinner.setText(conversionUnits[resultIndex].name, false)
        }

    fun getCopyResult(withUnitSymbols: Boolean): String {
        return binding.resultValue.text.toString() +
            if (withUnitSymbols) binding.sourceValueContainer.suffixText else ""
    }

    fun showError() {
        binding.resultValue.setText(R.string.message_error)
    }

    private fun setSpinnerAdapter(adapter: ArrayAdapter<String>) {
        sourceIndex = 0
        resultIndex = 1

        binding.sourceSpinner.setAdapter(adapter)
        binding.sourceSpinner.setText(adapter.getItem(sourceIndex), false)
        binding.resultSpinner.setAdapter(adapter)
        binding.resultSpinner.setText(adapter.getItem(resultIndex), false)
    }

    fun setUnits(units: List<ConversionUnit>) {
        conversionUnits.clear()
        conversionUnits.addAll(units)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            conversionUnits.map { it.name }
        )
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
        revealView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSecondary))
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

        // Play Reveal Animation if Lollipop or higher, and only alpha animation for others
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

    fun appendText(text: String) {
        binding.sourceValue.append(text)
    }

    fun removeLastDigit() {
        val textString = binding.sourceValue.text.toString()
        if (textString.isNotEmpty()) {
            binding.sourceValue.setText(textString.substring(0, textString.length - 1))
            binding.sourceValue.selectAll()
        }
    }

    fun setSpinnersCallback(callback: () -> Unit) {
        binding.sourceSpinner.setOnItemClickListener { _, _, position, _ ->
            sourceIndex = position
            callback()
        }
        binding.resultSpinner.setOnItemClickListener { _, _, position, _ ->
            resultIndex = position
            callback()
        }
    }

    private fun updateSuffixes() {
        binding.sourceValueContainer.setHtmlSuffixText(conversionUnits[sourceIndex].unitSymbol)
        binding.resultValueContainer.setHtmlSuffixText(conversionUnits[resultIndex].unitSymbol)
    }

    private fun TextInputLayout.setHtmlSuffixText(text: String) {
        suffixText = text
        suffixTextView.text = text.parseAsHtml()
    }
}