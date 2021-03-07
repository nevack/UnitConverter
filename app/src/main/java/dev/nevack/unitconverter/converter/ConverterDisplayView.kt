package dev.nevack.unitconverter.converter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewGroupOverlay
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.text.parseAsHtml
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.DisplayBinding
import dev.nevack.unitconverter.model.ConversionUnit
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

    private fun setSpinnerAdapter(adapter: ArrayAdapter<String>, isDifferent: Boolean) {
        binding.sourceSpinner.setAdapter(adapter)
        binding.resultSpinner.setAdapter(adapter)
        if (!adapter.isEmpty) {
            sourceIndex = 0
            resultIndex = if (isDifferent) 1 else 0
            binding.sourceSpinner.setText(adapter.getItem(sourceIndex), false)
            binding.resultSpinner.setText(adapter.getItem(resultIndex), false)
        } else {
            binding.sourceSpinner.isEnabled = false
            binding.resultSpinner.isEnabled = false
        }
    }

    fun setUnits(units: List<ConversionUnit>) {
        conversionUnits.clear()
        conversionUnits.addAll(units)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            conversionUnits.map { it.name }
        )
        setSpinnerAdapter(adapter, units.size > 1)
    }

    fun erase() {
        val revealView = View(context)
        // Make reveal cover the display and status bar.
        revealView.top = -y.toInt()
        revealView.bottom = bottom
        revealView.left = left
        revealView.right = right
        revealView.setBackgroundColor(context.getColor(R.color.colorSecondary))
        val groupOverlay = (parent as ViewGroup).overlay as ViewGroupOverlay
        groupOverlay.add(revealView)
        val revealCenterX = width
        val revealCenterY = height + y.toInt()
        val revealRadius = sqrt(revealCenterX.toFloat().pow(2) + revealCenterY.toFloat().pow(2))
        val revealAnimator = ViewAnimationUtils.createCircularReveal(
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

    fun appendText(text: CharSequence) {
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
