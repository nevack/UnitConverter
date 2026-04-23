package dev.nevack.unitconverter.converter

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.FragmentConverterBinding
import dev.nevack.unitconverter.history.HistoryActivity

@AndroidEntryPoint
class ConverterFragment : Fragment(R.layout.fragment_converter) {
    private lateinit var binding: FragmentConverterBinding

    private val viewModel: ConverterViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        binding = FragmentConverterBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_converter)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    viewModel.saveResultToHistory(binding.display.convertData)
                    true
                }

                R.id.history -> {
                    startActivity(Intent(requireContext(), HistoryActivity::class.java))
                    true
                }

                else -> {
                    false
                }
            }
        }

        if (arguments?.getBoolean(SHOW_NAV_BUTTON_ARG) == true) {
            binding.toolbar.setNavigationIcon(R.drawable.ic_menu)
            binding.toolbar.setNavigationOnClickListener { viewModel.setDrawerOpened(true) }
        }

        binding.display.onTextChanged {
            viewModel.convert(binding.display.convertData)
        }
        binding.display.setSpinnersCallback {
            viewModel.convert(binding.display.convertData)
        }
        binding.keypad.setOnCopyListeners { long ->
            requireContext().getSystemService<ClipboardManager>()?.run {
                val clipData =
                    ClipData.newPlainText("Conversion result", binding.display.getCopyResult(long))
                setPrimaryClip(clipData)
                Snackbar.make(binding.root, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.converterDisplayContainer?.applyInsetter {
            type(WindowInsetsCompat.Type.statusBars()) { padding() }
        }
        binding.keypad.applyInsetter {
            type(WindowInsetsCompat.Type.systemBars()) { padding() }
        }
        binding.keypad.setBackspaceListeners { long ->
            with(binding.display) { if (long) erase() else removeLastDigit() }
        }
        binding.keypad.setOnPasteListener {
            requireContext().getSystemService<ClipboardManager>()?.primaryClip?.run {
                if (description.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                    binding.display.appendText(getItemAt(0).text)
                }
            }
        }
        binding.keypad.setNumericListener { binding.display.appendText(it) }

        var previousState: ConverterUiState? = null
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val oldState = previousState

            if (state.backgroundColor != oldState?.backgroundColor) {
                state.backgroundColor?.let {
                    binding.background.setBackgroundColor(ContextCompat.getColor(requireContext(), it))
                }
            }

            if (state.title != oldState?.title) {
                state.title?.let(binding.toolbar::setTitle)
            }

            if (state.converter !== oldState?.converter) {
                state.converter?.let { binding.display.setUnits(it.units) }
            }

            if (state.result != oldState?.result) {
                binding.display.showResult(state.result.result)
            }

            if (state.messageResId != null && state.messageResId != oldState?.messageResId) {
                Snackbar.make(binding.root, state.messageResId, Snackbar.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }

            previousState = state
        }
    }

    companion object {
        const val SHOW_NAV_BUTTON_ARG = "showNavButton"
    }
}
