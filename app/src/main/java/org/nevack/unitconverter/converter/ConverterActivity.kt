package org.nevack.unitconverter.converter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.fragment.app.add
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import org.nevack.unitconverter.R
import org.nevack.unitconverter.databinding.ActivityConverterBinding
import org.nevack.unitconverter.model.Categories
import org.nevack.unitconverter.model.ConverterCategory

@AndroidEntryPoint
class ConverterActivity : AppCompatActivity() {
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setupNavigation(binding)
        viewModel.drawerOpened.observe(this) { opened ->
            if (opened) {
                binding.navigationDrawer.openDrawer(Gravity.START)
            } else {
                binding.navigationDrawer.closeDrawer(Gravity.START)
            }
        }

        supportFragmentManager.commit {
            add<ConverterFragment>(R.id.container)
            setReorderingAllowed(true)
        }

        viewModel.category.observe(this) {
            binding.navigationView.menu[it].isChecked = true
        }

        val category = intent.getIntExtra(CONVERTER_ID_EXTRA, 0)
        viewModel.load(Categories[category], this)
    }

    private fun setupNavigation(binding: ActivityConverterBinding) = with(binding.navigationView) {
        val units = ConverterCategory.values()
        for ((i, unit) in units.withIndex()) {
            menu.add(Menu.NONE, Menu.NONE, i, unit.categoryName)
                .setCheckable(true)
                .setIcon(unit.icon)
        }
        setNavigationItemSelectedListener { menuItem ->
            viewModel.load(Categories[menuItem.order], this@ConverterActivity)
            viewModel.setDrawerOpened(false)
            true
        }
    }

    override fun onBackPressed() {
        if (!viewModel.setDrawerOpened(false)) {
            super.onBackPressed()
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