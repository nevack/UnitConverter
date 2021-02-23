package dev.nevack.unitconverter.categories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.converter.ConverterActivity
import dev.nevack.unitconverter.converter.ConverterFragment
import dev.nevack.unitconverter.converter.ConverterViewModel
import dev.nevack.unitconverter.databinding.ActivityCategoriesBinding
import dev.nevack.unitconverter.model.Categories

@AndroidEntryPoint
class CategoriesActivity : AppCompatActivity() {
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val converterViewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val isTablet = binding.converterContainer != null

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<CategoriesFragment>(R.id.categoriesContainer)
            if (isTablet) {
                replace<ConverterFragment>(R.id.converterContainer)
            }
        }

        categoriesViewModel.converterOpened.observe(this) {
            if (isTablet) {
                converterViewModel.load(Categories[it], this)
            } else {
                startActivity(ConverterActivity.getIntent(this, it))
            }
        }

        if (isTablet) {
            converterViewModel.load(Categories[0], this)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.toolbarLayout.applyInsetter {
            type(statusBars = true) { padding(top = true) }
            type(navigationBars = true) { margin(horizontal = true) }
        }
    }
}
