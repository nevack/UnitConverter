package dev.nevack.unitconverter.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dev.chrisbanes.insetter.applyInsetter
import dev.nevack.unitconverter.R
import dev.nevack.unitconverter.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.commit {
            replace<CategoriesFragment>(R.id.container)
            setReorderingAllowed(true)
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.toolbarLayout.applyInsetter {
            type(statusBars = true) { padding(top = true) }
            type(navigationBars = true) { margin(horizontal = true) }
        }
    }
}
