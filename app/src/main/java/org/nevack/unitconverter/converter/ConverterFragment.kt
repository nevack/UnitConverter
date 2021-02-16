package org.nevack.unitconverter.converter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import dev.chrisbanes.insetter.applyInsetter
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterContract.ConvertData
import org.nevack.unitconverter.databinding.FragmentConverterBinding
import org.nevack.unitconverter.history.HistoryActivity
import org.nevack.unitconverter.model.ConversionUnit

class ConverterFragment : Fragment(R.layout.fragment_converter), ConverterContract.View {
    private var presenter: ConverterContract.Presenter? = null
    private lateinit var binding: FragmentConverterBinding

    private val viewModel: ConverterViewModel by activityViewModels()

    fun setPresenter(presenter: ConverterContract.Presenter) {
        this.presenter = presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentConverterBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_converter)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    presenter!!.saveResultToHistory()
                    true
                }
                R.id.history -> {
                    startActivity(Intent(requireContext(), HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu)
        binding.toolbar.setNavigationOnClickListener {
            viewModel.setDrawerOpened(true)
        }

        binding.display.setTextWatcher {
            presenter?.convert(binding.display.convertData)
        }
        binding.display.setSpinnersCallback { presenter?.convert(binding.display.convertData) }
        binding.keypad.setOnCopyListeners(
            object : KeypadView.ActionListener {
                override fun longClick() {
                    copyWithNotification(true)
                }

                override fun singleClick() {
                    copyWithNotification(false)
                }
            }
        )
        binding.converterDisplayContainer?.applyInsetter {
            type(WindowInsetsCompat.Type.statusBars()) { padding() }
        }
        binding.keypad.applyInsetter {
            type(WindowInsetsCompat.Type.systemBars()) { padding() }
        }
        binding.keypad.setBackspaceListeners(
            object : KeypadView.ActionListener {
                override fun longClick() {
                    binding.display.erase()
                }

                override fun singleClick() {
                    binding.display.removeLastDigit()
                }
            }
        )
        binding.keypad.setOnPasteListener { presenter!!.pasteFromClipboard() }
        binding.keypad.setNumericListener { number -> binding.display.appendText(number.toString()) }

        viewModel.backgroundColor.observe(viewLifecycleOwner) {
            binding.background.setBackgroundColor(ContextCompat.getColor(requireContext(), it))
        }

        viewModel.title.observe(viewLifecycleOwner) {
            binding.toolbar.setTitle(it)
        }
    }

    override fun setUnits(conversionUnits: List<ConversionUnit>) = binding.display.setUnits(conversionUnits)

    override fun getConvertData(): ConvertData = binding.display.convertData

    override fun setConvertData(data: ConvertData) {
        binding.display.convertData = data
    }

    override fun appendText(digit: String) = binding.display.appendText(digit)

    override fun showResult(result: String) = binding.display.showResult(result)

    override fun clear() = binding.display.clear()

    override fun showError() = binding.display.showError()

    private fun copyWithNotification(shouldIncludeUnit: Boolean) {
        presenter!!.copyResultToClipboard(binding.display.getCopyResult(shouldIncludeUnit))
        Snackbar.make(binding.background, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show()
    }
}