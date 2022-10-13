package dev.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.converter.ConverterFragment.Companion.SHOW_NAV_BUTTON_ARG
import dev.nevack.unitconverter.databinding.ActivityConverterBinding
import dev.nevack.unitconverter.model.Categories

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setupNavigation(binding)

        val callback = onBackPressedDispatcher.addCallback(this) {
            viewModel.setDrawerOpened(false)
        }
        viewModel.drawerOpened.observe(this) { opened ->
            callback.isEnabled = opened
            if (opened) {
                binding.navigationDrawer.open()
            } else {
                binding.navigationDrawer.close()
            }
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ConverterFragment>(R.id.container, args = bundleOf(SHOW_NAV_BUTTON_ARG to true))
        }

        viewModel.category.observe(this) {
            binding.navigationView.menu[it].isChecked = true
        }

        val category = intent.getIntExtra(CONVERTER_ID_EXTRA, 0)
        viewModel.load(Categories[category], this)
    }

    private fun setupNavigation(binding: ActivityConverterBinding) = with(binding.navigationView) {
        for ((i, unit) in Categories.withIndex()) {
            menu.add(Menu.NONE, Menu.NONE, i, unit.categoryName)
                .setCheckable(true)
                .setIcon(unit.icon)
        }
        setNavigationItemSelectedListener { menuItem ->
            viewModel.load(Categories[menuItem.order], this@ConverterActivity)
            viewModel.setDrawerOpened(false)
            true
        }
        applyInsetter {
            type(statusBars = true) { margin(top = true) }
            type(navigationBars = true) { margin(bottom = true) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.category.value?.let { outState.putInt(CONVERTER_ID_EXTRA, it) }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val category = savedInstanceState.getInt(CONVERTER_ID_EXTRA, -1)
        if (category != -1) {
            viewModel.load(Categories[category], this)
        }
    }

    companion object {
        private const val CONVERTER_ID_EXTRA = "converter_id"

        fun getIntent(context: Context, category: Int): Intent =
            Intent(context, ConverterActivity::class.java).apply {
                putExtra(CONVERTER_ID_EXTRA, category)
            }
    }
}
