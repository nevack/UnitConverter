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
import com.google.android.material.snackbar.Snackbar
import dev.chrisbanes.insetter.applyInsetter
import org.nevack.unitconverter.R
import org.nevack.unitconverter.converter.ConverterContract.ConvertData
import org.nevack.unitconverter.databinding.FragmentConverterBinding
import org.nevack.unitconverter.history.HistoryActivity
import org.nevack.unitconverter.model.Unit

class ConverterFragment : Fragment(R.layout.fragment_converter), ConverterContract.View {
    private var presenter: ConverterContract.Presenter? = null
    private lateinit var binding: FragmentConverterBinding

    override fun setPresenter(presenter: ConverterContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResume() {
        super.onResume()
        presenter!!.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentConverterBinding.bind(view)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
            actionBar.setDisplayHomeAsUpEnabled(true)
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_converter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> presenter!!.saveResultToHistory()
            R.id.history -> startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setTitle(title: Int) = requireActivity().setTitle(title)

    override fun setUnits(units: List<Unit>) = binding.display.setUnits(units)

    override fun getConvertData(): ConvertData = binding.display.convertData

    override fun setConvertData(data: ConvertData) {
        binding.display.convertData = data
    }

    override fun appendText(text: String) = binding.display.appendText(text)

    override fun showResult(result: String) = binding.display.showResult(result)

    override fun clear() = binding.display.clear()

    override fun showError() = binding.display.showError()

    override fun setBackgroundColor(color: Int) {
        binding.background.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun copyWithNotification(shouldIncludeUnit: Boolean) {
        presenter!!.copyResultToClipboard(binding.display.getCopyResult(shouldIncludeUnit))
        Snackbar.make(binding.background, R.string.copy_result_toast, Snackbar.LENGTH_SHORT).show()
    }
}