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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                else -> false
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

        viewModel.backgroundColor.observe(viewLifecycleOwner) {
            binding.background.setBackgroundColor(ContextCompat.getColor(requireContext(), it))
        }

        viewModel.title.observe(viewLifecycleOwner) {
            binding.toolbar.setTitle(it)
        }

        viewModel.converter.observe(viewLifecycleOwner) {
            binding.display.setUnits(it.units)
        }

        viewModel.result.observe(viewLifecycleOwner) {
            binding.display.showResult(it.result)
        }
    }

    companion object {
        const val SHOW_NAV_BUTTON_ARG = "showNavButton"
    }
}
